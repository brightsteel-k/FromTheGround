package com.br1ghtsteel.ftground.entity_types;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.block_entities.BatBoxBlockEntity;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.entity_types.ai.FlyingMoveControl;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class Bat extends Animal implements FlyingAnimal {
	public static final float FLAP_DEGREES_PER_TICK = 74.48451F;
	public static final int TICKS_PER_FLAP = Mth.ceil(2.4166098F);
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Bat.class,
			EntityDataSerializers.BYTE);
	private static final int FLAG_RESTING = 1;
	private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0D);
	@Nullable
	private BlockPos targetPosition;
	private static final int BOX_CLOSE_ENOUGH_DISTANCE = 2;
	private static final int PATHFIND_TO_BOX_WHEN_CLOSER_THAN = 16;
	private static final int BOX_SEARCH_DISTANCE = 20;
	public static final String TAG_BOX_POS = "BoxPos";
	private int stayOutOfBoxCountdown;
	private static final int COOLDOWN_BEFORE_LOCATING_NEW_BOX = 200;
	int remainingCooldownBeforeLocatingNewBox;
	@Nullable
	BlockPos boxPos;
	Bat.BatGoToBoxGoal goToBoxGoal;
	private int underWaterTicks;

	public Bat(EntityType<? extends Bat> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new FlyingMoveControl(this, 90, true);
		this.lookControl = new LookControl(this);
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
		this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, (double) 0.9F)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.1F);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
	}

	public float getWalkTargetValue(BlockPos blockpos, LevelReader level) {
		return level.getBlockState(blockpos).isAir() ? 10.0F : 0.0F;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new Bat.BatEnterBoxGoal());
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.MELON_SLICE), false));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(5, new Bat.BatLocateBoxGoal());
		this.goToBoxGoal = new Bat.BatGoToBoxGoal();
		this.goalSelector.addGoal(5, this.goToBoxGoal);
		this.goalSelector.addGoal(8, new Bat.BatWanderGoal());
		this.goalSelector.addGoal(9, new FloatGoal(this));
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.hasBox()) {
			tag.put("BoxPos", NbtUtils.writeBlockPos(this.getBoxPos()));
		}

		tag.putInt("CannotEnterBoxTicks", this.stayOutOfBoxCountdown);
		tag.putByte("Resting", this.entityData.get(DATA_FLAGS_ID));
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		this.boxPos = null;
		if (tag.contains("BoxPos")) {
			this.boxPos = NbtUtils.readBlockPos(tag.getCompound("BoxPos"));
		}

		super.readAdditionalSaveData(tag);
		this.stayOutOfBoxCountdown = tag.getInt("CannotEnterBoxTicks");
		this.entityData.set(DATA_FLAGS_ID, tag.getByte("Resting"));
	}

	public void tick() {
		super.tick();
		if (this.isResting()) {
			this.setDeltaMovement(Vec3.ZERO);
			this.setPosRaw(this.getX(), (double) Mth.floor(this.getY()) + 1.0D - (double) this.getBbHeight(),
					this.getZ());
		} else {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
	}

	private void pathfindRandomlyTowards(BlockPos destination) {
		Vec3 vec3 = Vec3.atBottomCenterOf(destination);
		int i = 0;
		BlockPos blockpos = this.blockPosition();
		int j = (int) vec3.y - blockpos.getY();
		if (j > 2) {
			i = 4;
		} else if (j < -2) {
			i = -4;
		}

		int k = 6;
		int l = 8;
		int i1 = blockpos.distManhattan(destination);
		if (i1 < 15) {
			k = i1 / 2;
			l = i1 / 2;
		}

		Vec3 vec31 = AirRandomPos.getPosTowards(this, k, l, i, vec3, (double) ((float) Math.PI / 10F));
		if (vec31 != null) {
			this.navigation.setMaxVisitedNodesMultiplier(0.5F);
			this.navigation.moveTo(vec31.x, vec31.y, vec31.z, 1.0D);
		}
	}

	private boolean wantsToEnterBox() {
		if (this.stayOutOfBoxCountdown <= 0) {
			boolean flag = this.level.isRaining() || this.level.isNight();
			return flag && !this.isBoxNearFire();
		} else {
			return false;
		}
	}

	public void setStayOutOfBoxCountdown(int countdown) {
		this.stayOutOfBoxCountdown = countdown;
	}

	protected void customServerAiStep() {
		if (this.isInWaterOrBubble()) {
			++this.underWaterTicks;
		} else {
			this.underWaterTicks = 0;
		}

		if (this.underWaterTicks > 80) {
			this.hurt(DamageSource.DROWN, 1.0F);
		}

		BlockPos blockpos = this.blockPosition();
		BlockPos blockpos1 = blockpos.above();
		if (this.isResting()) {
			boolean flag = this.isSilent();
			if (this.level.getBlockState(blockpos1).isRedstoneConductor(this.level, blockpos)) {
				if (this.random.nextInt(200) == 0) {
					this.yHeadRot = (float) this.random.nextInt(360);
				}

				if (this.level.getNearestPlayer(BAT_RESTING_TARGETING, this) != null) {
					this.setResting(false);
					if (!flag) {
						this.level.levelEvent((Player) null, 1025, blockpos, 0);
					}
				}
			} else {
				this.setResting(false);
				if (!flag) {
					this.level.levelEvent((Player) null, 1025, blockpos, 0);
				}
			}
		}
	}

	private boolean isBoxNearFire() {
		if (this.boxPos == null) {
			return false;
		} else {
			BlockEntity blockentity = this.level.getBlockEntity(this.boxPos);
			return blockentity instanceof BatBoxBlockEntity && ((BatBoxBlockEntity) blockentity).isFireNearby();
		}
	}

	private boolean doesBoxHaveSpace(BlockPos blockpos) {
		BlockEntity blockentity = this.level.getBlockEntity(blockpos);
		if (blockentity instanceof BatBoxBlockEntity) {
			return !((BatBoxBlockEntity) blockentity).isFull();
		} else {
			return false;
		}
	}

	public void aiStep() {
		super.aiStep();
		if (!this.level.isClientSide) {
			if (this.stayOutOfBoxCountdown > 0) {
				--this.stayOutOfBoxCountdown;
			}

			if (this.remainingCooldownBeforeLocatingNewBox > 0) {
				--this.remainingCooldownBeforeLocatingNewBox;
			}

			if (this.tickCount % 20 == 0 && !this.isBoxValid()) {
				this.boxPos = null;
			}
		}

	}

	public boolean hasBox() {
		return this.boxPos != null;
	}

	@Nullable
	public BlockPos getBoxPos() {
		return this.boxPos;
	}

	boolean isBoxValid() {
		if (!this.hasBox()) {
			return false;
		} else {
			BlockEntity blockentity = this.level.getBlockEntity(this.boxPos);
			return blockentity instanceof BatBoxBlockEntity;
		}
	}

	public boolean isResting() {
		return (this.entityData.get(DATA_FLAGS_ID) & FLAG_RESTING) != 0;
	}

	public void setResting(boolean value) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (value) {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
		} else {
			this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
		}

	}

	protected PathNavigation createNavigation(Level level) {
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
			public boolean isStableDestination(BlockPos blockpos) {
				return !this.level.getBlockState(blockpos.below()).isAir();
			}
		};
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(false);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	public boolean isFood(ItemStack item) {
		return item.is(Items.MELON_SLICE);
	}

	protected void playStepSound(BlockPos p_27820_, BlockState p_27821_) {
	}

	@Nullable
	public SoundEvent getAmbientSound() {
		return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
	}

	protected float getSoundVolume() {
		return 0.1F;
	}

	public float getVoicePitch() {
		return super.getVoicePitch() * 0.95F;
	}

	protected SoundEvent getHurtSound(DamageSource p_27451_) {
		return SoundEvents.BAT_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.BAT_DEATH;
	}

	public boolean isPushable() {
		return false;
	}

	protected void doPush(Entity entity) {
	}

	protected void pushEntities() {
	}

	public Bat getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
		return EntityTypesInit.BAT.get().create(level);
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		return this.isBaby() ? entityDimensions.height * 0.5F : entityDimensions.height * 0.5F;
	}

	public boolean causeFallDamage(float x, float y, DamageSource damageSource) {
		return false;
	}

	protected void checkFallDamage(double x, boolean flag, BlockState blockstate, BlockPos blockpos) {
	}

	@Override
	public boolean isFlying() {
		return !this.isResting();
	}

	public boolean isFlapping() {
		return !this.isResting() && this.tickCount % TICKS_PER_FLAP == 0;
	}

	public boolean hurt(DamageSource damageSource, float amount) {
		if (this.isInvulnerableTo(damageSource)) {
			return false;
		} else {
			if (!this.level.isClientSide && this.isResting()) {
				this.setResting(false);
			}

			return super.hurt(damageSource, amount);
		}
	}

	private void jumpInLiquidInternal() {
		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
	}

	@Override
	public void jumpInFluid(net.minecraftforge.fluids.FluidType type) {
		this.jumpInLiquidInternal();
	}

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, (double) (0.5F * this.getEyeHeight()), (double) (this.getBbWidth() * 0.2F));
	}

	boolean isTooFarAway(BlockPos blockpos) {
		return !this.closerThan(blockpos, 32);
	}

	boolean closerThan(BlockPos blockpos, int distance) {
		return blockpos.closerThan(this.blockPosition(), (double) distance);
	}

	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	abstract class BaseBatGoal extends Goal {
		public abstract boolean canBatUse();

		public abstract boolean canBatContinueToUse();

		public boolean canUse() {
			return this.canBatUse() && !Bat.this.isResting();
		}

		public boolean canContinueToUse() {
			return this.canBatContinueToUse() && !Bat.this.isResting();
		}
	}

	class BatEnterBoxGoal extends Bat.BaseBatGoal {
		public boolean canBatUse() {
			if (Bat.this.hasBox() && Bat.this.wantsToEnterBox()
					&& Bat.this.boxPos.closerToCenterThan(Bat.this.position(), 2.0D)) {
				BlockEntity blockentity = Bat.this.level.getBlockEntity(Bat.this.boxPos);
				if (blockentity instanceof BatBoxBlockEntity) {
					BatBoxBlockEntity batBoxBlockEntity = (BatBoxBlockEntity) blockentity;
					if (!batBoxBlockEntity.isFull()) {
						return true;
					}

					Bat.this.boxPos = null;
				}
			}

			return false;
		}

		public boolean canBatContinueToUse() {
			return false;
		}

		public void start() {
	         BlockEntity blockentity = Bat.this.level.getBlockEntity(Bat.this.boxPos);
	         if (blockentity instanceof BatBoxBlockEntity) {
	        	 ((BatBoxBlockEntity)blockentity).addOccupant(Bat.this);
	         }
	    }
	}

	public class BatGoToBoxGoal extends Bat.BaseBatGoal {
		public static final int MAX_TRAVELLING_TICKS = 600;
		int travellingTicks = Bat.this.level.random.nextInt(10);
		private static final int MAX_BLACKLISTED_TARGETS = 3;
		final List<BlockPos> blacklistedTargets = Lists.newArrayList();
		@Nullable
		private Path lastPath;
		private int ticksStuck;

		BatGoToBoxGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canBatUse() {
			return Bat.this.boxPos != null && !Bat.this.hasRestriction() && Bat.this.wantsToEnterBox()
					&& !this.hasReachedTarget(Bat.this.boxPos)
					&& Bat.this.level.getBlockState(Bat.this.boxPos).is(BlocksInit.BAT_BOX.get());
		}

		public boolean canBatContinueToUse() {
			return this.canBatUse();
		}

		public void start() {
			this.travellingTicks = 0;
			this.ticksStuck = 0;
			super.start();
		}

		public void stop() {
			this.travellingTicks = 0;
			this.ticksStuck = 0;
			Bat.this.navigation.stop();
			Bat.this.navigation.resetMaxVisitedNodesMultiplier();
		}

		public void tick() {
			if (Bat.this.boxPos != null) {
				++this.travellingTicks;
				if (this.travellingTicks > this.adjustedTickDelay(600)) {
					this.dropAndBlacklistBox();
				} else if (!Bat.this.navigation.isInProgress()) {
					if (!Bat.this.closerThan(Bat.this.boxPos, 16)) {
						if (Bat.this.isTooFarAway(Bat.this.boxPos)) {
							this.dropBox();
						} else {
							Bat.this.pathfindRandomlyTowards(Bat.this.boxPos);
						}
					} else {
						boolean flag = this.pathfindDirectlyTowards(Bat.this.boxPos);
						if (!flag) {
							this.dropAndBlacklistBox();
						} else if (this.lastPath != null && Bat.this.navigation.getPath().sameAs(this.lastPath)) {
							++this.ticksStuck;
							if (this.ticksStuck > 60) {
								this.dropBox();
								this.ticksStuck = 0;
							}
						} else {
							this.lastPath = Bat.this.navigation.getPath();
						}

					}
				}
			}
		}

		private boolean pathfindDirectlyTowards(BlockPos blockpos) {
			Bat.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
			Bat.this.navigation.moveTo((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(),
					1.0D);
			return Bat.this.navigation.getPath() != null && Bat.this.navigation.getPath().canReach();
		}

		boolean isTargetBlacklisted(BlockPos blockpos) {
			return this.blacklistedTargets.contains(blockpos);
		}

		private void blacklistTarget(BlockPos blockpos) {
			this.blacklistedTargets.add(blockpos);

			while (this.blacklistedTargets.size() > 3) {
				this.blacklistedTargets.remove(0);
			}

		}

		void clearBlacklist() {
			this.blacklistedTargets.clear();
		}

		private void dropAndBlacklistBox() {
			if (Bat.this.boxPos != null) {
				this.blacklistTarget(Bat.this.boxPos);
			}

			this.dropBox();
		}

		private void dropBox() {
			Bat.this.boxPos = null;
			Bat.this.remainingCooldownBeforeLocatingNewBox = 200;
		}

		private boolean hasReachedTarget(BlockPos blockpos) {
			if (Bat.this.closerThan(blockpos, 2)) {
				return true;
			} else {
				Path path = Bat.this.navigation.getPath();
				return path != null && path.getTarget().equals(blockpos) && path.canReach() && path.isDone();
			}
		}
	}

	class BatLocateBoxGoal extends Bat.BaseBatGoal {
		public boolean canBatUse() {
			return Bat.this.remainingCooldownBeforeLocatingNewBox == 0 && !Bat.this.hasBox()
					&& Bat.this.wantsToEnterBox();
		}

		public boolean canBatContinueToUse() {
			return false;
		}

		public void start() {
			Bat.this.remainingCooldownBeforeLocatingNewBox = 200;
			List<BlockPos> list = this.findNearbyBoxesWithSpace();
			if (!list.isEmpty()) {
				for (BlockPos blockpos : list) {
					if (!Bat.this.goToBoxGoal.isTargetBlacklisted(blockpos)) {
						Bat.this.boxPos = blockpos;
						return;
					}
				}

				Bat.this.goToBoxGoal.clearBlacklist();
				Bat.this.boxPos = list.get(0);
			}
		}

		private List<BlockPos> findNearbyBoxesWithSpace() {
			return new ArrayList<>();
			/*BlockPos blockpos = Bat.this.blockPosition();
			PoiManager poimanager = ((ServerLevel) Bat.this.level).getPoiManager();

			Stream<PoiRecord> stream = poimanager.getInRange((predicate) -> {
				return predicate.is(PoiTypeTags.BEE_HOME);
			}, blockpos, 20, PoiManager.Occupancy.ANY);
			return stream.map(PoiRecord::getPos).filter(Bat.this::doesBoxHaveSpace)
					.sorted(Comparator.comparingDouble((a) -> {
						return a.distSqr(blockpos);
					})).collect(Collectors.toList());*/
		}
	}

	class BatWanderGoal extends Goal {
		private static final int WANDER_THRESHOLD = 22;

		BatWanderGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return Bat.this.navigation.isDone() && Bat.this.random.nextInt(10) == 0;
		}

		public boolean canContinueToUse() {
			return Bat.this.navigation.isInProgress();
		}

		public void start() {
			Vec3 vec3 = this.findPos();
			if (vec3 != null) {
				Bat.this.navigation.moveTo(Bat.this.navigation.createPath(new BlockPos(vec3), 1), 1.0D);
			}

		}

		@Nullable
		private Vec3 findPos() {
			Vec3 vec3;
			if (Bat.this.isBoxValid() && !Bat.this.closerThan(Bat.this.boxPos, 22)) {
				Vec3 vec31 = Vec3.atCenterOf(Bat.this.boxPos);
				vec3 = vec31.subtract(Bat.this.position()).normalize();
			} else {
				vec3 = Bat.this.getViewVector(0.0F);
			}

			int i = 8;
			Vec3 vec32 = HoverRandomPos.getPos(Bat.this, 8, 7, vec3.x, vec3.z, ((float) Math.PI / 2F), 3, 1);
			return vec32 != null ? vec32
					: AirAndWaterRandomPos.getPos(Bat.this, 8, 4, -2, vec3.x, vec3.z, (double) ((float) Math.PI / 2F));
		}
	}
}
