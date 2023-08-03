package com.br1ghtsteel.ftground.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.br1ghtsteel.ftground.core.BlocksInit;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SaltpeterBlock extends FallingBlock
{
	private static final List<Direction> HEAT_DIRECTIONS = Arrays.asList(Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
	
	public SaltpeterBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}
	
	@Override
	public void randomTick(BlockState blockstate, ServerLevel level, BlockPos blockpos, RandomSource random) {
		int lavaSides = 0;
		for (Direction direction : HEAT_DIRECTIONS) {
			BlockState blockstate1 = level.getBlockState(blockpos.relative(direction));
			if (blockstate1.is(Blocks.LAVA) || blockstate1.is(BlocksInit.MOLTEN_SALTPETER.get())) {
				if (++lavaSides >= 2) {
					melt(blockstate, level, blockpos, random);
					break;
				}
			}
			
		}
	}
	
	private void melt(BlockState blockstate, ServerLevel level, BlockPos blockpos, RandomSource random) {
		if (!blockstate.is(BlocksInit.SALTPETER_BLOCK.get())) {
			return;
		}
		
		level.setBlock(blockpos, BlocksInit.MOLTEN_SALTPETER.get().defaultBlockState(), 3);
		level.playSound((Player)null, blockpos, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 5.0f, random.nextFloat() / 2f + 0.75f);
	}
	
	@Override
	public int getDustColor(BlockState p_53238_, BlockGetter p_53239_, BlockPos p_53240_) {
	   return 0xffffff;
	}
}
