package com.br1ghtsteel.ftground.entity_types;

import com.br1ghtsteel.ftground.blocks.GroundFlareBlock;
import com.br1ghtsteel.ftground.blocks.WallFlareBlock;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FlareArrow extends AbstractArrow {
	private int duration = 20;

	public FlareArrow(EntityType<? extends FlareArrow> entityType, Level level) {
		super(entityType, level);
	}

	public FlareArrow(Level level, LivingEntity livingEntity) {
		super(EntityTypesInit.FLARE_ARROW.get(), livingEntity, level);
	}

	public FlareArrow(Level level, double x, double y, double z) {
		super(EntityTypesInit.FLARE_ARROW.get(), x, y, z, level);
	}

	public void tick() {
		super.tick();

		if (this.level.isClientSide && !this.inGround) {
			this.level.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		}
	}

	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		if (!this.level.isClientSide) {
			BlockPos blockpos = blockHitResult.getBlockPos();
			BlockState blockstate = this.level.getBlockState(blockpos);
			Direction direction = blockHitResult.getDirection();
			boolean success = tryPlaceFlare(blockpos, blockstate, direction);

			if (success) {
				this.playSound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 3.0f, 1.4f);
				for (int i = 0; i < 32; ++i) {
					this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + this.random.nextGaussian() / 2f,
							this.getY() + this.random.nextGaussian() / 2f,
							this.getZ() + this.random.nextGaussian() / 2f, 0.05d, 0.05d, 0.05d);
				}
			} else {
				dropItem();
				this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0f, 1.0f);
				for (int i = 0; i < 32; ++i) {
					this.level.addParticle(ParticleTypes.SMOKE, this.getX() + this.random.nextGaussian() / 2f,
							this.getY() + this.random.nextGaussian() / 2f,
							this.getZ() + this.random.nextGaussian() / 2f, 0, 0, 0);
				}
			}
			discard();
		}
	}

	private boolean tryPlaceFlare(BlockPos blockpos, BlockState blockstate, Direction direction) {
		if (this.level.getBlockState(blockpos.relative(direction)).is(FTGBlockTags.FLARE_REPLACEABLE)) {
			switch (direction) {
				case DOWN:
					if (Block.canSupportCenter(this.level, blockpos, Direction.DOWN)) {
						this.level.setBlockAndUpdate(blockpos.below(), BlocksInit.FLARE.get().defaultBlockState());
						return true;
					}
					break;
				case UP:
					if (Block.canSupportCenter(this.level, blockpos, Direction.UP)) {
						this.level.setBlockAndUpdate(blockpos.above(),
								((GroundFlareBlock) BlocksInit.GROUND_FLARE.get()).getStateFromFlareArrow(getYRot() % 360));
						return true;
					}
					break;
				default:
					if (blockstate.isFaceSturdy(this.level, blockpos, direction)) {
						BlockState stateToPlace = ((WallFlareBlock) BlocksInit.WALL_FLARE.get())
								.getStateFromFlareArrow(direction, getXRot() % 360);
						this.level.setBlockAndUpdate(blockpos.relative(direction), stateToPlace);
						return true;
					}
					break;
			}
		}
		return false;
	}
	
	private void dropItem() {
		if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
		    this.spawnAtLocation(this.getPickupItem(), 0.1F);
		}
	}

	protected ItemStack getPickupItem() {
		return new ItemStack(ItemsInit.FLARE_ARROW.get());
	}

	protected void doPostHurtEffects(LivingEntity target) {
		super.doPostHurtEffects(target);
		target.setSecondsOnFire(duration / 20);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Duration")) {
			this.duration = tag.getInt("Duration");
		}

	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Duration", this.duration);
	}
}
