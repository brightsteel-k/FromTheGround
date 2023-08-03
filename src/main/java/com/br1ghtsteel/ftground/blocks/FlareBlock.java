package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlareBlock extends Block
{
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);
	
	public FlareBlock(Properties properties) {
		super(properties);
	}

	public VoxelShape getShape(BlockState p_57510_, BlockGetter p_57511_, BlockPos p_57512_, CollisionContext p_57513_) {
		return AABB;
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
		return direction == Direction.UP && !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, otherState, level, pos, otherPos);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSupportCenter(level, pos.above(), Direction.DOWN);
	}
	
	protected double randomVel(RandomSource rnd) {
		return rnd.nextDouble() / 10 - 0.05D;
	}
	
	protected double randomVel(RandomSource rnd, boolean x, Direction direction) {
		double d0 = rnd.nextDouble() / 12 + 0.005f;
		switch (direction)
		{
			case EAST:
				if (x)
					return d0;
				break;
			case WEST:
				if (x)
					return -d0;
				break;
			case NORTH:
				if (!x)
					return -d0;
				break;
			case SOUTH:
				if (!x)
					return d0;
				break;
			default:
				break;
		}
		
		return rnd.nextBoolean() ? d0 * 0.1D : d0 * -0.1D;
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rnd) {
		double d0 = (double)pos.getX() + 0.5D;
		double d1 = (double)pos.getY() + 0.4D;
		double d2 = (double)pos.getZ() + 0.5D;
		level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, randomVel(rnd), -0.1D, randomVel(rnd));
		for (int k = 0; k < 2; k++)
			level.addParticle(ParticleTypes.FIREWORK, d0, d1, d2, randomVel(rnd), -0.05D, randomVel(rnd));
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos blockpos, BlockState blockstate) {
		return new ItemStack(ItemsInit.FLARE.get());
	}
}