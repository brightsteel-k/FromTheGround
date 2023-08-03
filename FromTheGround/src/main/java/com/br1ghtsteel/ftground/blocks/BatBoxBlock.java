package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.BlockStateProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BatBoxBlock extends Block {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final IntegerProperty GUANO_LEVEL = BlockStateProperties.LEVEL_GUANO;
	public static final int MAX_GUANO_LEVELS = 8;
	private static final int GUANO_COUNT = 3;

	public BatBoxBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(
				this.stateDefinition.any().setValue(GUANO_LEVEL, Integer.valueOf(0)).setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState blockstate) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockstate, Level level, BlockPos blockpos) {
		return blockstate.getValue(GUANO_LEVEL);
	}

	@Override
	public BlockState rotate(BlockState blockState, net.minecraft.world.level.block.Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, net.minecraft.world.level.block.Mirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
	}

	@Override
	public RenderShape getRenderShape(BlockState blockstate) {
		return RenderShape.MODEL;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(FACING).add(GUANO_LEVEL);
	}
}
