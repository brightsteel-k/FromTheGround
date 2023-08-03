package com.br1ghtsteel.ftground.blocks;

import java.util.Map;

import com.br1ghtsteel.ftground.core.BlockStateProperties;
import com.br1ghtsteel.ftground.entity_types.ExplodingBlock;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class StableDetonatorBlock extends Block {

	public static final BooleanProperty NORTH = BlockStateProperties.DETONATOR_PRIMED_NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.DETONATOR_PRIMED_EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.DETONATOR_PRIMED_SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.DETONATOR_PRIMED_WEST;
	public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap
			.copyOf(Util.make(Maps.newEnumMap(Direction.class), (map) -> {
				map.put(Direction.NORTH, NORTH);
				map.put(Direction.EAST, EAST);
				map.put(Direction.SOUTH, SOUTH);
				map.put(Direction.WEST, WEST);
			}));

	public StableDetonatorBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)));
	}

	@Override
	public void onBlockStateChange(LevelReader level, BlockPos blockpos, BlockState oldState, BlockState newState) {
		if (level instanceof ServerLevel) {
			boolean primed = false;
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				if (newState.getValue(PROPERTY_BY_DIRECTION.get(dir))) {
					primed = true;
				}
			}
			if (!primed) {
				return;
			}
			
			if ((newState.getValue(NORTH) && newState.getValue(SOUTH)) || (newState.getValue(EAST) && newState.getValue(WEST))) {
				detonate((ServerLevel)level, blockpos);
			} else {
				((ServerLevel)level).scheduleTick(blockpos, this, 2);
			}
		}
	}
	
	@Override
	public void tick(BlockState blockstate, ServerLevel level, BlockPos blockpos, RandomSource random) {
		level.setBlock(blockpos, defaultBlockState(), 3);
	}
	
	public void detonate(Level level, BlockPos blockpos) {
		DetonatorExplosionDamageCalculator explosionDamageCalculator = new DetonatorExplosionDamageCalculator(blockpos);
		ExplodingBlock explodingBlock = new ExplodingBlock(level, blockpos.getX(), blockpos.getY() + 0.5d, blockpos.getZ(), 6.0f, 0, defaultBlockState(), null);
		level.explode(explodingBlock, (DamageSource)null, explosionDamageCalculator, blockpos.getX() + 0.5d, blockpos.getY() + 0.5d, blockpos.getZ() + 0.5d, 6.0f, false, BlockInteraction.BREAK);
		level.setBlock(blockpos, defaultBlockState(), 3);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(NORTH, EAST, SOUTH, WEST);
	}
	
	// REQUIRES: blockstate.is(BlocksInit.STABLE_DETONATOR.get())
	public static void primeSide(Level level, BlockPos blockpos, BlockState blockstate, Direction side) {
		level.setBlock(blockpos, blockstate.setValue(PROPERTY_BY_DIRECTION.get(side), Boolean.valueOf(true)), 3);
	}
	
	public static class DetonatorExplosionDamageCalculator extends ExplosionDamageCalculator {
		private final BlockPos origin;
		
		public DetonatorExplosionDamageCalculator(BlockPos origin) {
			super();
			this.origin = origin;
		}
		
		@Override
		public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos blockpos,
				BlockState blockstate, float distance) {
			return !blockpos.equals(origin) && super.shouldBlockExplode(explosion, level, blockpos, blockstate, distance);
		}
	}
}
