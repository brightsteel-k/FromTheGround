package com.br1ghtsteel.ftground.blocks;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.block_entities.MixingStandBlockEntity;
import com.br1ghtsteel.ftground.core.BlockEntityTypesInit;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MixingStandBlock extends BaseEntityBlock {

	public static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[] { BlockStateProperties.HAS_BOTTLE_0,
			BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2 };
	protected static final VoxelShape SHAPE = Shapes.or(Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D),
			Block.box(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D));

	public MixingStandBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(HAS_BOTTLE[0], Boolean.valueOf(false))
				.setValue(HAS_BOTTLE[1], Boolean.valueOf(false)).setValue(HAS_BOTTLE[2], Boolean.valueOf(false)));
	}

	public RenderShape getRenderShape(BlockState blockstate) {
		return RenderShape.MODEL;
	}

	public VoxelShape getShape(BlockState blockstate, BlockGetter blockGetter, BlockPos blockpos,
			CollisionContext collisionContext) {
		return SHAPE;
	}

	public BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
		return new MixingStandBlockEntity(blockpos, blockstate);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockstate,
			BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null
				: createTickerHelper(blockEntityType, BlockEntityTypesInit.MIXING_STAND.get(),
						MixingStandBlockEntity::serverTick);
	}

	public InteractionResult use(BlockState blockstate, Level level, BlockPos blockpos, Player player,
			InteractionHand hand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockentity = level.getBlockEntity(blockpos);
			if (blockentity instanceof MixingStandBlockEntity) {
				player.openMenu((MixingStandBlockEntity) blockentity);
			}

			return InteractionResult.CONSUME;
		}
	}

	public void setPlacedBy(Level level, BlockPos blockpos, BlockState blockstate, LivingEntity entity,
			ItemStack itemstack) {
		if (itemstack.hasCustomHoverName()) {
			BlockEntity blockentity = level.getBlockEntity(blockpos);
			if (blockentity instanceof MixingStandBlockEntity) {
				((MixingStandBlockEntity) blockentity).setCustomName(itemstack.getHoverName());
			}
		}
	}

	public void onRemove(BlockState blockstate, Level level, BlockPos blockpos, BlockState newState,
			boolean destroy) {
		if (!blockstate.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(blockpos);
			if (blockentity instanceof MixingStandBlockEntity) {
				Containers.dropContents(level, blockpos, (MixingStandBlockEntity) blockentity);
			}

			super.onRemove(blockstate, level, blockpos, newState, destroy);
		}
	}

	public boolean hasAnalogOutputSignal(BlockState p_50919_) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState blockstate, Level level, BlockPos blockpos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockpos));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2]);
	}

	public boolean isPathfindable(BlockState blockstate, BlockGetter world, BlockPos blockpos,
			PathComputationType pathComputationType) {
		return false;
	}
}
