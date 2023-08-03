package com.br1ghtsteel.ftground.entity_types;

import java.util.List;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.tags.FTGBlockTags;
import com.br1ghtsteel.ftground.util.MixingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownMixture extends ThrowableItemProjectile implements ItemSupplier {
	public static final double SPLASH_RANGE = 4.0D;

	public ThrownMixture(EntityType<? extends ThrownMixture> entityType, Level level) {
		super(entityType, level);
	}

	public ThrownMixture(Level level, LivingEntity thrower) {
		super(EntityTypesInit.THROWN_MIXTURE.get(), thrower, level);
	}

	public ThrownMixture(Level level, double x, double y, double z) {
		super(EntityTypesInit.THROWN_MIXTURE.get(), x, y, z, level);
	}

	protected Item getDefaultItem() {
		return ItemsInit.SULFURIC_ACID.get();
	}

	protected float getGravity() {
		return 0.05F;
	}

	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		if (!this.level.isClientSide) {
			if (getItem().is(ItemsInit.NITRIC_ACID.get())) {
				BlockPos blockpos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
				spreadFireFromBlockPos(blockpos, 1, 1);
			}
		}
	}

	private void trySpreadFire(BlockPos source, boolean fireDetected) {
		if (renewFire(source)) {
			fireDetected = true;
		}
		for (Direction direction1 : Direction.values()) {
			if (renewFire(source.relative(direction1))) {
				fireDetected = true;
			}
		}

		if (fireDetected) {
			spreadFireFromBlockPos(source, 2, 2);
		}
	}

	private void spreadFireFromBlockPos(BlockPos source, int r, int ry) {
		boolean isHumid = level.isHumidAt(source);
		BlockPos.MutableBlockPos mutableBlockpos = new BlockPos.MutableBlockPos();

		for (int x = -r; x <= r; x++) {
			for (int z = -r; z <= r; z++) {
				if (Math.abs(x) + Math.abs(z) <= r) {
					for (int y = -ry; y <= ry; y++) {
						mutableBlockpos.setWithOffset(source, x, y, z);
						if (!level.getBlockState(mutableBlockpos).is(Blocks.FIRE)) {
							int k1 = 80;
							if (y > 1) {
								k1 += y * 80;
							}
							
							int igniteOdds = getIgniteOdds(level, mutableBlockpos);
							if (igniteOdds > 0) {
								int i2 = (igniteOdds + (isNearRain(level, mutableBlockpos) ? 40 : 80)) / 4;
								if (isHumid) {
									i2 /= 2;
								}
								
								if (i2 > 0 && random.nextInt(k1) <= i2) {
									level.setBlockAndUpdate(mutableBlockpos, Blocks.FIRE.defaultBlockState());
								}
							}
						}
					}					
				}
			}
		}
	}

	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();

			if (itemstack.is(ItemsInit.SPIRIT_OF_AMMONIUM.get())) {
				applyFertilizer(blockPosition(), false);
			} else if (itemstack.is(ItemsInit.NITROCHALK_FERTILIZER.get())) {
				applyFertilizer(blockPosition(), true);
			} else if (itemstack.is(ItemsInit.BITUMEN_FUEL.get())) {
				applyFuel();
			} else {
				List<MobEffectInstance> list = MixingUtil.getEffects(itemstack);
				if (!list.isEmpty()) {
					applySplash(list, itemstack.is(ItemsInit.NITRIC_ACID.get()),
							hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hitResult).getEntity()
									: null);
				}
				
				if (itemstack.is(ItemsInit.SULFURIC_ACID.get())) {
					applyAcid(itemstack);
				}
			}

			this.level.levelEvent(MixingUtil.isAcid(getItem()) ? 2007 : 2002, this.blockPosition(),
					MixingUtil.getColor(itemstack));
			this.discard();
		}
	}

	private void applyFertilizer(BlockPos source, boolean nitrochalk) {
		BlockPos.MutableBlockPos mutableBlockpos = new BlockPos.MutableBlockPos();
		int r = nitrochalk ? 2 : 3;
		int r2 = r * r;
		for (int x = -r; x < r; x++) {
			for (int z = -r; z < r; z++) {
				if (x * x + z * z > r2) {
					continue;
				}
				
				for (int y = -2; y < 2; y++) {
					mutableBlockpos.setWithOffset(source, x, y, z);
					BlockState blockstate = level.getBlockState(mutableBlockpos);
					if (blockstate.getBlock() instanceof BonemealableBlock && (nitrochalk
							|| blockstate.is(FTGBlockTags.AMMONIUM_NITRATE_EFFECTIVE) && random.nextFloat() > 0.3f)) {
						BonemealableBlock bonemealableblock = (BonemealableBlock) blockstate.getBlock();
						if (bonemealableblock.isValidBonemealTarget(level, mutableBlockpos, blockstate,
								level.isClientSide)) {
							if (level instanceof ServerLevel) {
								if (bonemealableblock.isBonemealSuccess(level, level.random, mutableBlockpos,
										blockstate)) {
									bonemealableblock.performBonemeal((ServerLevel) level, level.random,
											mutableBlockpos, blockstate);
								}
							}
						}
					}
				}
			}
		}
	}

	private void applyFuel() {
		AABB range = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);

		boolean fire = false;
		for (Entity entity : level.getEntitiesOfClass(Entity.class, range)) {
			if (entity.isOnFire() && distanceToSqr(entity) < 16.0D) {
				int newFireTicks = entity.getRemainingFireTicks() + random.nextIntBetweenInclusive(300, 600);
				entity.setRemainingFireTicks(Math.min(newFireTicks, 2000));
				fire = true;
			}
		}
		trySpreadFire(blockPosition(), fire);
	}

	private void applySplash(List<MobEffectInstance> effects, boolean nitric, @Nullable Entity entity) {
		AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
		if (!list.isEmpty()) {
			Entity entity1 = this.getEffectSource();
			for (LivingEntity livingentity : list) {
				double d0 = this.distanceToSqr(livingentity);
				double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
				if (livingentity == entity) {
					d1 = 1.0D;
				}
				if (d0 < 16.0D) {
					if (livingentity.isAffectedByPotions()) {
							for (MobEffectInstance mobeffectinstance : effects) {
								MobEffect mobeffect = mobeffectinstance.getEffect();
								if (mobeffect.isInstantenous()) {
									mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity,
											mobeffectinstance.getAmplifier(), d1);
								} else {
									int i = (int) (d1 * (double) mobeffectinstance.getDuration() + 0.5D);
									if (i > 20) {
										livingentity.addEffect(
												new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(),
														mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()),
												entity1);
								}
							}
						}
					}
					if (nitric) {
						livingentity.setRemainingFireTicks((int)(160 * d1) + 60);
					}
				}
			}
		}
		
		List<ItemEntity> list2 = this.level.getEntitiesOfClass(ItemEntity.class, aabb);
		for (ItemEntity itementity : list2) {
			if (this.distanceToSqr(itementity) < 16.0D) {
				itementity.setRemainingFireTicks(200);
			}
		}
	}

	private void applyAcid(ItemStack itemstack) {
		CompoundTag tag = itemstack.getTag();
		int concentration = tag != null && tag.contains("Concentration") ? tag.getInt("Concentration") : 0;
		if (concentration > 0) {
			MixingUtil.applyAcidToRange(level, blockPosition(), concentration - 1, concentration / 3 + 1, concentration >= 4, this);
		}
	}

	private boolean renewFire(BlockPos blockpos) {
		BlockState blockstate = this.level.getBlockState(blockpos);
		if (blockstate.is(Blocks.FIRE)) {
			blockstate.setValue(FireBlock.AGE, Integer.valueOf(random.nextIntBetweenInclusive(0, 2)));
			return true;
		} else if (AbstractCandleBlock.isLit(blockstate) || CampfireBlock.isLitCampfire(blockstate)
				|| blockstate.is(FTGBlockTags.FIERY)) {
			return true;
		} else {
			return false;
		}
	}

	private static int getIgniteOdds(LevelReader level, BlockPos blockpos) {
		if (!level.isEmptyBlock(blockpos)) {
			return 0;
		} else {
			int i = 0;

			for (Direction direction : Direction.values()) {
				BlockState blockstate = level.getBlockState(blockpos.relative(direction));
				i = Math.max(
						blockstate.getFireSpreadSpeed(level, blockpos.relative(direction), direction.getOpposite()), i);
			}

			return i + 100;
		}
	}

	private static boolean isNearRain(Level level, BlockPos blockpos) {
		return level.isRainingAt(blockpos) || level.isRainingAt(blockpos.west()) || level.isRainingAt(blockpos.east())
				|| level.isRainingAt(blockpos.north()) || level.isRainingAt(blockpos.south());
	}
}
