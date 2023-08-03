package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.BlockStateProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.ticks.TickPriority;

public class IrregulatorBlock extends DiodeBlock {

	public static final BooleanProperty POLARITY = BlockStateProperties.IRREGULATOR_POLARITY;

	public IrregulatorBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
				.setValue(POLARITY, Boolean.valueOf(false)).setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	protected int getDelay(BlockState blockstate) {
		return 1;
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel level, BlockPos blockpos, RandomSource random) {
		if (!this.isLocked(level, blockpos, blockstate)) {
			boolean alreadyOn = blockstate.getValue(POWERED);
			boolean shouldTurnOn = this.shouldTurnOn(level, blockpos, blockstate);
			if (alreadyOn && !shouldTurnOn) {
				level.setBlock(blockpos, blockstate.setValue(POWERED, Boolean.valueOf(false)), 2);
			} else if (!alreadyOn) {
				level.setBlock(blockpos, blockstate.setValue(POLARITY, Boolean.valueOf(random.nextBoolean()))
						.setValue(POWERED, Boolean.valueOf(true)), 2);
				if (!shouldTurnOn) {
					level.scheduleTick(blockpos, this, this.getDelay(blockstate), TickPriority.VERY_HIGH);
				}
			}
		}
	}

	@Override
	public void onPlace(BlockState blockstate, Level level, BlockPos blockpos, BlockState prevState,
			boolean persistant) {
		this.updateNeighborsOnSides(level, blockpos, blockstate);
	}

	@Override
	public void onRemove(BlockState blockstate, Level level, BlockPos blockpos, BlockState newState,
			boolean persistant) {
		if (!persistant && !blockstate.is(newState.getBlock())) {
			super.onRemove(blockstate, level, blockpos, newState, persistant);
			this.updateNeighborsOnSides(level, blockpos, blockstate);
		}
	}

	protected void updateNeighborsOnSides(Level level, BlockPos blockpos, BlockState blockstate) {
		Direction direction = blockstate.getValue(FACING);
		Direction left = direction.getCounterClockWise();
		Direction right = direction.getClockWise();
		BlockPos leftPos = blockpos.relative(left);
		BlockPos rightPos = blockpos.relative(right);
		
		if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, blockpos,
				level.getBlockState(blockpos), java.util.EnumSet.of(left), false).isCanceled())
			return;
		level.neighborChanged(leftPos, this, blockpos);
		level.updateNeighborsAtExceptFromFacing(leftPos, this, right);
		
		if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, blockpos,
				level.getBlockState(blockpos), java.util.EnumSet.of(right), false).isCanceled())
			return;
		level.neighborChanged(rightPos, this, blockpos);
		level.updateNeighborsAtExceptFromFacing(rightPos, this, left);
	}

	@Override
	public int getSignal(BlockState blockstate, BlockGetter level, BlockPos blockpos, Direction direction) {
		if (!blockstate.getValue(POWERED)) {
			return 0;
		} else {
			boolean left = blockstate.getValue(FACING).getCounterClockWise() == direction
					&& !blockstate.getValue(POLARITY);
			boolean right = blockstate.getValue(FACING).getClockWise() == direction && blockstate.getValue(POLARITY);
			return left || right ? this.getOutputSignal(level, blockpos, blockstate) : 0;
		}
	}
	
	@Override
	protected int getAlternateSignal(LevelReader level, BlockPos blockpos, BlockState blockstate) {
	      Direction direction = blockstate.getValue(FACING).getOpposite();
	      BlockState signalState = level.getBlockState(blockpos.relative(direction));
	      if (this.isAlternateInput(blockstate)) {
	         if (blockstate.is(Blocks.REDSTONE_BLOCK)) {
	            return 15;
	         } else {
	            return blockstate.is(Blocks.REDSTONE_WIRE) ? blockstate.getValue(RedStoneWireBlock.POWER) : level.getDirectSignal(blockpos, direction);
	         }
	      } else {
	         return 0;
	      }
	   }

	@Override
	public void animateTick(BlockState blockstate, Level level, BlockPos blockpos, RandomSource rnd) {
		if (blockstate.getValue(POWERED)) {
			Direction direction = blockstate.getValue(FACING);
			double d0 = (double) blockpos.getX() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double) blockpos.getY() + 0.4D + (rnd.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double) blockpos.getZ() + 0.5D + (rnd.nextDouble() - 0.5D) * 0.2D;
			float f = -5.0F / 16.0F;
			double d3 = (double) (f * (float) direction.getStepX());
			double d4 = (double) (f * (float) direction.getStepZ());
			level.addParticle(DustParticleOptions.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(FACING, POLARITY, POWERED);
	}
}
