package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DeadCropBlock extends Block {
	private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
	
	public DeadCropBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader level, BlockPos blockpos) {
		return level.getBlockState(blockpos.below()).is(FTGBlockTags.FARMLAND_BLOCKS);
	}
	
	@Override
	public VoxelShape getShape(BlockState blockstate, BlockGetter level, BlockPos blockpos,
			CollisionContext collisionContext) {
		return SHAPE;
	}
	
	@Override
	public BlockState updateShape(BlockState blockstate, Direction direction, BlockState otherState, LevelAccessor level,
			BlockPos blockpos, BlockPos otherPos) {
		return !canSurvive(blockstate, level, blockpos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockstate, direction, otherState, level, blockpos, otherPos);
	}
}
