package com.br1ghtsteel.ftground.blocks;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.entity_types.ExplodingBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class ChargeBlock extends Block {
	public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
	private final float explosionPower;
	private final int fuse;
	private boolean isVolatile;

	public ChargeBlock(BlockBehaviour.Properties properties, float explosionPower, int fuse, boolean isVolatile) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.valueOf(false)));
		this.explosionPower = explosionPower;
		this.fuse = fuse;
		this.isVolatile = isVolatile;
	}

	public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable net.minecraft.core.Direction face,
			@Nullable LivingEntity igniter) {
		explode(world, pos, igniter);
	}

	public void onPlace(BlockState blockstate, Level level, BlockPos blockpos, BlockState otherState, boolean persistent) {
		if (!otherState.is(blockstate.getBlock())) {
			if (level.hasNeighborSignal(blockpos)) {
				onCaughtFire(blockstate, level, blockpos, null, null);
				level.removeBlock(blockpos, false);
			}
		}

		if (isVolatile && persistent && otherState.is(Blocks.MOVING_PISTON)) {
			explode(level, blockpos, null);
			level.removeBlock(blockpos, false);
		}
	}

	public void neighborChanged(BlockState blockstate, Level level, BlockPos blockpos, Block block, BlockPos otherpos,
			boolean b) {
		if (level.hasNeighborSignal(blockpos)) {
			onCaughtFire(blockstate, level, blockpos, null, null);
			level.removeBlock(blockpos, false);
		}
	}

	public void playerWillDestroy(Level level, BlockPos blockpos, BlockState blockstate, Player player) {
		if (!level.isClientSide() && !player.isCreative() && blockstate.getValue(UNSTABLE)) {
			onCaughtFire(blockstate, level, blockpos, null, null);
		}

		super.playerWillDestroy(level, blockpos, blockstate, player);
	}

	public void wasExploded(Level level, BlockPos blockpos, @Nullable Explosion explosion) {
		if (!level.isClientSide) {
			ExplodingBlock explodingBlock = new ExplodingBlock(level, (double) blockpos.getX() + 0.5D,
					(double) blockpos.getY(), (double) blockpos.getZ() + 0.5D, this.explosionPower, 1,
					this.defaultBlockState(), explosion == null ? null : explosion.getSourceMob());
			explodingBlock.setFuse((short) (fuse > 160 ? 20 : level.random.nextInt(fuse / 2) + fuse / 4));
			if (isVolatile) {
				explodingBlock.setVolatile(true);
			}
			level.addFreshEntity(explodingBlock);
		}
	}
	
	private void explode(Level level, BlockPos blockpos, @Nullable LivingEntity sourceEntity) {
		if (!level.isClientSide) {
			ExplodingBlock explodingBlock = new ExplodingBlock(level, (double) blockpos.getX() + 0.5D,
					(double) blockpos.getY(), (double) blockpos.getZ() + 0.5D, this.explosionPower, 1,
					this.defaultBlockState(), sourceEntity);
			explodingBlock.setFuse(fuse);
			if (isVolatile) {
				explodingBlock.setVolatile(true);
			}
			level.addFreshEntity(explodingBlock);
			level.playSound((Player) null, explodingBlock.getX(), explodingBlock.getY(), explodingBlock.getZ(),
					SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.gameEvent(sourceEntity, GameEvent.PRIME_FUSE, blockpos);
		}
	}

	public InteractionResult use(BlockState blockstate, Level level, BlockPos blockpos, Player player,
			InteractionHand hand, BlockHitResult blockHitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
			return super.use(blockstate, level, blockpos, player, hand, blockHitResult);
		} else {
			onCaughtFire(blockstate, level, blockpos, blockHitResult.getDirection(), player);
			level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 11);
			Item item = itemstack.getItem();
			if (!player.isCreative()) {
				if (itemstack.is(Items.FLINT_AND_STEEL)) {
					itemstack.hurtAndBreak(1, player, (e) -> {
						e.broadcastBreakEvent(hand);
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

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(UNSTABLE);
	}
}
