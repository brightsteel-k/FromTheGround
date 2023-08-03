package com.br1ghtsteel.ftground.entity_types;

import com.br1ghtsteel.ftground.blocks.GroundFlareBlock;
import com.br1ghtsteel.ftground.blocks.WallFlareBlock;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownFlare extends ThrowableProjectile {
	public ThrownFlare(EntityType<? extends ThrownFlare> entity, Level level) {
		super(entity, level);
	}

	public ThrownFlare(Level level, LivingEntity thrower) {
		super(EntityTypesInit.THROWN_FLARE.get(), thrower, level);
	}

	public ThrownFlare(Level level, double x, double y, double z) {
		super(EntityTypesInit.THROWN_FLARE.get(), x, y, z, level);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		// BOUNCE?
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		if (this.level.isClientSide) {
			return;
		}
		
		BlockPos blockpos = result.getBlockPos();
		BlockState blockstate = this.level.getBlockState(result.getBlockPos());
		Direction direction = result.getDirection();
		blockstate.onProjectileHit(this.level, blockstate, result, this);
		boolean success = tryPlaceFlare(blockpos, blockstate, direction);

		if (success) {
			this.playSound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 3.0f, 1.4f);
			for (int i = 0; i < 32; ++i) {
				this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + this.random.nextGaussian() / 2f,
						this.getY() + this.random.nextGaussian() / 2f, this.getZ() + this.random.nextGaussian() / 2f,
						0.05d, 0.05d, 0.05d);
			}
		} else {
			dropItem(this.level, this.blockPosition());
			this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0f, 1.0f);
			for (int i = 0; i < 32; ++i) {
				this.level.addParticle(ParticleTypes.SMOKE, this.getX() + this.random.nextGaussian() / 2f,
						this.getY() + this.random.nextGaussian() / 2f, this.getZ() + this.random.nextGaussian() / 2f, 0,
						0, 0);
			}
		}

		discard();
	}
	
	private boolean tryPlaceFlare(BlockPos blockpos, BlockState blockstate, Direction direction) {
		if (level.getBlockState(blockpos.relative(direction)).is(FTGBlockTags.FLARE_REPLACEABLE)) {
			switch (direction) {
				case DOWN:
					if (Block.canSupportCenter(this.level, blockpos, Direction.DOWN)) {
						this.level.setBlockAndUpdate(blockpos.below(), BlocksInit.FLARE.get().defaultBlockState());
						return true;
					}
					break;
				case UP:
					if (Block.canSupportCenter(this.level, blockpos, Direction.UP)) {
						this.level.setBlockAndUpdate(blockpos.above(), ((GroundFlareBlock) BlocksInit.GROUND_FLARE.get())
								.getStateFromThrownFlare(getXRot() % 360, getYRot() % 360));
						return true;
					}
					break;
				default:
					if (blockstate.isFaceSturdy(this.level, blockpos, direction)) {
						BlockState stateToPlace = ((WallFlareBlock) BlocksInit.WALL_FLARE.get()).getStateFromThrownFlare(direction, getXRot() % 360);
						level.setBlockAndUpdate(blockpos.relative(direction), stateToPlace);
						return true;
					}
					break;
			}
		}
		return false;
	}

	protected void dropItem(Level level, BlockPos pos) {
		if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			float f = EntityType.ITEM.getHeight() / 2.0F;
			double d0 = (double) ((float) pos.getX() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
			double d1 = (double) ((float) pos.getY() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D) - (double) f;
			double d2 = (double) ((float) pos.getZ() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
			ItemEntity itementity = new ItemEntity(level, d0, d1, d2, new ItemStack(() -> ItemsInit.FLARE.get()));
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}
	}

	@Override
	protected void updateRotation() {
		Vec3 vec3 = this.getDeltaMovement();
		this.setXRot(this.getXRot() - 30f);
		this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
	}

	@Override
	public void tick() {
		super.tick();

		// Particles
		if (this.level.isClientSide) {
			Vec3 vec3 = this.getDeltaMovement();
			this.level.addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
					vec3.x, vec3.y, vec3.z);
		}
	}

	@Override
	protected void defineSynchedData() {

	}
}
