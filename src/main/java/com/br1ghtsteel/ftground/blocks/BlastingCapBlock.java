package com.br1ghtsteel.ftground.blocks;

import java.util.Arrays;
import java.util.List;

import com.br1ghtsteel.ftground.core.BlocksInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BlastingCapBlock extends Block {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final List<Block> IGNITABLE_BLOCKS = Arrays.asList(BlocksInit.AMMONIUM_SALTPETER_BLOCK.get(), BlocksInit.ANFO_BLOCK.get(), BlocksInit.ANFO_CHARGE.get(), BlocksInit.AMATOL.get(), Blocks.TNT);

	public BlastingCapBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public void onPlace(BlockState blockstate, Level level, BlockPos blockpos, BlockState otherState,
			boolean persistent) {
		if (!persistent || !otherState.is(Blocks.MOVING_PISTON)) {
			return;
		}

		Direction facing = blockstate.getValue(FACING);
		BlockState pushingState = level.getBlockState(blockpos.relative(facing.getOpposite()));
		if (!pushingState.is(Blocks.MOVING_PISTON) || pushingState.getValue(MovingPistonBlock.FACING) != facing) {
			return;
		}

		BlockState target = level.getBlockState(blockpos.relative(facing));
		if (IGNITABLE_BLOCKS.contains(target.getBlock())) {
			Explosion smallExplosion = new Explosion(level, (Entity)null, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), 0f);
			target.getBlock().wasExploded(level, blockpos.relative(facing), smallExplosion);
			level.removeBlock(blockpos.relative(facing), false);
		} else if (target.is(BlocksInit.STABLE_DETONATOR.get())) {
			StableDetonatorBlock.primeSide(level, blockpos.relative(facing), target, facing.getOpposite());
		}
	}

	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState blockstate, Rotation rotation) {
		return blockstate.setValue(FACING, rotation.rotate(blockstate.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(FACING);
	}
}
