package com.br1ghtsteel.ftground.blocks;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GroundFlareBlock extends FlareBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final Map<Direction, VoxelShape> AABBS = Maps
			.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.5D, 0.0D, 3.0D, 10.5D, 5.0D, 13.0D),
					Direction.SOUTH, Block.box(5.5D, 0.0D, 3.0D, 10.5D, 5.0D, 13.0D), Direction.WEST,
					Block.box(3.0D, 0.0D, 5.5D, 13.0D, 5D, 10.5D), Direction.EAST,
					Block.box(3.0D, 0.0D, 5.5D, 13.0D, 5D, 10.5D)));

	public GroundFlareBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public String getDescriptionId() {
		return this.asItem().getDescriptionId();
	}

	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return getShape(state);
	}

	public static VoxelShape getShape(BlockState state) {
		return AABBS.get(state.getValue(FACING));
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSupportCenter(level, pos.below(), Direction.UP);
	}

	public BlockState getStateFromThrownFlare(float xRot, float yRot) {
		BlockState blockstate = this.defaultBlockState();

		Direction direction1 = calcDirection(yRot);

		if ((xRot > -290 && xRot < -110))
			direction1 = direction1.getOpposite();

		return blockstate.setValue(FACING, direction1);
	}

	public BlockState getStateFromFlareArrow(float yRot) {
		BlockState blockstate = this.defaultBlockState();
		return blockstate.setValue(FACING, calcDirection(yRot));
	}

	public static Direction calcDirection(float r) {
		r %= 360f;
		if (r < 0)
			r += 360f;

		if (r < 45.0f || r >= 315.0f)
			return Direction.SOUTH;
		if (r >= 225.0f && r < 315.0f)
			return Direction.WEST;
		if (r >= 135.0f && r < 225.0f)
			return Direction.NORTH;
		if (r >= 45.0f && r < 135.0f)
			return Direction.EAST;

		return Direction.SOUTH;
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level,
			BlockPos pos, BlockPos otherPos) {
		return direction == Direction.DOWN && !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState()
				: state;
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mir) {
		return state.rotate(mir.getRotation(state.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(FACING);
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rnd) {
		Direction direction = state.getValue(FACING);
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.15D;
		double d2 = (double) pos.getZ() + 0.5D;
		Direction direction1 = direction.getOpposite();
		level.addParticle(ParticleTypes.SMOKE, d0 - 0.27D * (double) direction1.getStepX(), d1,
				d2 - 0.27D * (double) direction1.getStepZ(), randomVel(rnd, true, direction), 0.0D,
				randomVel(rnd, false, direction));
		for (int k = 0; k < 2; k++)
			level.addParticle(ParticleTypes.FIREWORK, d0 - 0.27D * (double) direction1.getStepX(), d1,
					d2 - 0.27D * (double) direction1.getStepZ(), randomVel(rnd, true, direction), rnd.nextDouble() / 12,
					randomVel(rnd, false, direction));
	}
}