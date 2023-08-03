package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.BlockStateProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class DecayingBlock extends Block
{
	private final Block decaysTo;
	private static final BooleanProperty DECAYING = BlockStateProperties.DECAYING;
	
	public DecayingBlock(BlockBehaviour.Properties properties, Block decaysTo) {
		super(properties);
		this.decaysTo = decaysTo;
		this.registerDefaultState(this.stateDefinition.any().setValue(DECAYING, Boolean.valueOf(true)));
	}
	
	@Override
	public void onPlace(BlockState blockstate, Level level, BlockPos blockpos, BlockState prevState, boolean b) {
		if (blockstate.getValue(DECAYING) && level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING) > 0) {
			level.scheduleTick(blockpos, this, getDecayingTickDelay(level.random));
		}
	}
	
	@Override
	public void tick(BlockState blockstate, ServerLevel level, BlockPos blockpos, RandomSource rnd) {
		if (rnd.nextFloat() > 0.6f) {
			if (this.decaysTo.equals(Blocks.AIR)) {
				dropResources(blockstate, level, blockpos);
				level.removeBlock(blockpos, false);
			} else {
				level.setBlock(blockpos, decaysTo.defaultBlockState(), 3);
			}
		} else {
			level.scheduleTick(blockpos, this, getDecayingTickDelay(level.random));
		}
	}
	
	private static int getDecayingTickDelay(RandomSource rnd) {
		return 20 + rnd.nextInt(60);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(DECAYING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return context.getPlayer() == null ? defaultBlockState() : defaultBlockState().setValue(DECAYING, Boolean.valueOf(false));
	}
}
