package com.br1ghtsteel.ftground.blocks;

import java.util.Map;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallFlareBlock extends FlareBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty UPSIDE_DOWN = BooleanProperty.create("upside_down");
	protected static final float AABB_OFFSET = 2.5F;
	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(
			ImmutableMap.of(Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D), 
							Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D), 
							Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D), 
							Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D)));

	public WallFlareBlock(BlockBehaviour.Properties p_58123_)
	{
		super(p_58123_);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(UPSIDE_DOWN, true));
	}

	public String getDescriptionId()
	{
		return this.asItem().getDescriptionId();
	}

	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		return getShape(state);
	}

	public static VoxelShape getShape(BlockState state)
	{
		return AABBS.get(state.getValue(FACING));
	}
	
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction.getOpposite());
		BlockState blockstate = level.getBlockState(blockpos);
		return blockstate.isFaceSturdy(level, blockpos, direction);
	}

	public BlockState getStateFromThrownFlare(Direction direction, float xRot)
	{
		BlockState blockstate = this.defaultBlockState();
		boolean upsideDown = -210 < xRot && xRot < -50;
		
		return blockstate.setValue(UPSIDE_DOWN, upsideDown).setValue(FACING, direction);
	}
	
	public BlockState getStateFromFlareArrow(Direction direction, float xRot)
	{
		BlockState blockstate = this.defaultBlockState();
		return blockstate.setValue(UPSIDE_DOWN, xRot > 0).setValue(FACING, direction);
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
	}

	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mir)
	{
		return state.rotate(mir.getRotation(state.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
	{
		stateBuilder.add(FACING);
		stateBuilder.add(UPSIDE_DOWN);
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rnd)
	{
		Direction direction = state.getValue(FACING);
		boolean upsideDown = state.getValue(UPSIDE_DOWN);
		double d0 = (double)pos.getX() + 0.5D;
		double d1 = (double)pos.getY() + (upsideDown ? 0.3D : 0.7D);
		double d2 = (double)pos.getZ() + 0.5D;
		double v1 = (rnd.nextDouble() / 12D + 0.08D) * (upsideDown ? -1D : 1D);
		Direction direction1 = direction.getOpposite();
		
		level.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getStepX(), d1, d2 + 0.27D * (double)direction1.getStepZ(), randomVel(rnd, true, direction) / 2, 0.0D, randomVel(rnd, false, direction) / 2);
		for (int k = 0; k < 2; k++)
			level.addParticle(ParticleTypes.FIREWORK, d0 + 0.27D * (double)direction1.getStepX(), d1, d2 + 0.27D * (double)direction1.getStepZ(), randomVel(rnd, true, direction), v1, randomVel(rnd, false, direction));
	}
}
