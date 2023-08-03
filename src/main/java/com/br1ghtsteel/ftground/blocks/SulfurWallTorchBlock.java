package com.br1ghtsteel.ftground.blocks;

import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SulfurWallTorchBlock extends SulfurTorchBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	   protected static final float AABB_OFFSET = 2.5F;
	   private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D), Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D), Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D), Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D)));

	   public SulfurWallTorchBlock(BlockBehaviour.Properties properties, Supplier<ParticleOptions> flameParticle) {
	      super(properties, flameParticle);
	      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	   }

	   public String getDescriptionId() {
	      return this.asItem().getDescriptionId();
	   }

	   public VoxelShape getShape(BlockState blockstate, BlockGetter blockGetter, BlockPos blockpos, CollisionContext collisionContext) {
	      return getShape(blockstate);
	   }

	   public static VoxelShape getShape(BlockState blockstate) {
	      return AABBS.get(blockstate.getValue(FACING));
	   }

	   public boolean canSurvive(BlockState blockstate, LevelReader levelReader, BlockPos blockpos) {
	      Direction direction = blockstate.getValue(FACING);
	      BlockPos blockpos1 = blockpos.relative(direction.getOpposite());
	      BlockState blockstate1 = levelReader.getBlockState(blockpos1);
	      return blockstate1.isFaceSturdy(levelReader, blockpos1, direction);
	   }

	   @Nullable
	   public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
	      BlockState blockstate = this.defaultBlockState();
	      LevelReader levelreader = blockPlaceContext.getLevel();
	      BlockPos blockpos = blockPlaceContext.getClickedPos();
	      Direction[] adirection = blockPlaceContext.getNearestLookingDirections();

	      for(Direction direction : adirection) {
	         if (direction.getAxis().isHorizontal()) {
	            Direction direction1 = direction.getOpposite();
	            blockstate = blockstate.setValue(FACING, direction1);
	            if (blockstate.canSurvive(levelreader, blockpos)) {
	               return blockstate;
	            }
	         }
	      }

	      return null;
	   }

	   public BlockState updateShape(BlockState blockstate, Direction direction, BlockState otherstate, LevelAccessor levelAcc, BlockPos blockpos, BlockPos otherpos) {
	      return direction.getOpposite() == blockstate.getValue(FACING) && !blockstate.canSurvive(levelAcc, blockpos) ? Blocks.AIR.defaultBlockState() : blockstate;
	   }

	   @Override
	   public void animateTick(BlockState blockstate, Level level, BlockPos blockpos, RandomSource rnd) {
	      Direction direction = blockstate.getValue(FACING);
	      double d0 = (double)blockpos.getX() + 0.5D;
	      double d1 = (double)blockpos.getY() + 0.7D;
	      double d2 = (double)blockpos.getZ() + 0.5D;
	      Direction direction1 = direction.getOpposite();
	      level.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
	      level.addParticle(this.flameParticle.get(), d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
	   }

	   public BlockState rotate(BlockState blockstate, Rotation rotation) {
	      return blockstate.setValue(FACING, rotation.rotate(blockstate.getValue(FACING)));
	   }

	   public BlockState mirror(BlockState blockstate, Mirror mirror) {
	      return blockstate.rotate(mirror.getRotation(blockstate.getValue(FACING)));
	   }

	   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
	      stateDefinition.add(FACING);
	   }
}
