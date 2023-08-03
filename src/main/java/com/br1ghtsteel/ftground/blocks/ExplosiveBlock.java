package com.br1ghtsteel.ftground.blocks;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.entity_types.ExplodingBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class ExplosiveBlock extends Block {

	private final boolean flammable;
	private final float explosionPower;
	private final int particleType;

	public ExplosiveBlock(BlockBehaviour.Properties properties, boolean flammable, float explosionPower,
			int particleType) {
		super(properties);
		this.flammable = flammable;
		this.explosionPower = explosionPower;
		this.particleType = particleType;
	}

	public void wasExploded(Level level, BlockPos blockpos, Explosion explosion) {
		if (!level.isClientSide) {
			ExplodingBlock explodingBlock = new ExplodingBlock(level, (double) blockpos.getX() + 0.5D,
					(double) blockpos.getY(), (double) blockpos.getZ() + 0.5D, this.explosionPower, this.particleType,
					this.defaultBlockState(), explosion.getSourceMob());
			explodingBlock.setFuse((short) (level.random.nextInt(5) + 15));
			level.addFreshEntity(explodingBlock);
		}
	}

	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable net.minecraft.core.Direction face,
			@Nullable LivingEntity igniter) {
		explode(level, pos, igniter);
	}

	public void neighborChanged(BlockState blockstate, Level level, BlockPos blockpos, Block block, BlockPos otherpos,
			boolean b) {
		if (flammable && (level.getBlockState(otherpos).is(BlockTags.FIRE) || level.getBlockState(otherpos).is(Blocks.LAVA))) {
			onCaughtFire(blockstate, level, blockpos, null, null);
			level.removeBlock(blockpos, false);
		}
	}

	public InteractionResult use(BlockState blockstate, Level level, BlockPos blockpos, Player player,
			InteractionHand hand, BlockHitResult blockHitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!flammable || (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE))) {
			return super.use(blockstate, level, blockpos, player, hand, blockHitResult);
		} else {
			onCaughtFire(blockstate, level, blockpos, blockHitResult.getDirection(), player);
			level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 11);
			Item item = itemstack.getItem();
			if (!player.isCreative()) {
				if (itemstack.is(Items.FLINT_AND_STEEL)) {
					itemstack.hurtAndBreak(1, player, (p_57425_) -> {
						p_57425_.broadcastBreakEvent(hand);
					});
				} else {
					itemstack.shrink(1);
				}
			}

			player.awardStat(Stats.ITEM_USED.get(item));
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public void onProjectileHit(Level level, BlockState blockstate, BlockHitResult blockHitResult,
			Projectile projectile) {
		if (!flammable) {
			return;
		}
		
		if (!level.isClientSide) {
			BlockPos blockpos = blockHitResult.getBlockPos();
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire() && projectile.mayInteract(level, blockpos)) {
				onCaughtFire(blockstate, level, blockpos, null,
						entity instanceof LivingEntity ? (LivingEntity) entity : null);
				level.removeBlock(blockpos, false);
			}
		}
	}

	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}

	private void explode(Level level, BlockPos blockpos, @Nullable LivingEntity sourceEntity) {
		if (!level.isClientSide) {
			ExplodingBlock explodingBlock = new ExplodingBlock(level, (double) blockpos.getX() + 0.5D,
					(double) blockpos.getY(), (double) blockpos.getZ() + 0.5D, this.explosionPower, this.particleType,
					this.defaultBlockState(), sourceEntity);
			explodingBlock.setFuse(10);
			level.addFreshEntity(explodingBlock);
			level.gameEvent(sourceEntity, GameEvent.PRIME_FUSE, blockpos);
		}
	}
}
