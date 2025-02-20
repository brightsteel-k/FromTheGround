package com.br1ghtsteel.ftground.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SulfurTorchBlock extends Block
{
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	protected final Supplier<ParticleOptions> flameParticle;
	
	public SulfurTorchBlock(BlockBehaviour.Properties properties, Supplier<ParticleOptions> particleOptions) {
		super(properties);
		this.flameParticle = particleOptions;
	}

	public VoxelShape getShape(BlockState p_57510_, BlockGetter p_57511_, BlockPos p_57512_, CollisionContext p_57513_) {
		return AABB;
	}

	public BlockState updateShape(BlockState p_57503_, Direction p_57504_, BlockState p_57505_, LevelAccessor p_57506_, BlockPos p_57507_, BlockPos p_57508_) {
		return p_57504_ == Direction.DOWN && !this.canSurvive(p_57503_, p_57506_, p_57507_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_57503_, p_57504_, p_57505_, p_57506_, p_57507_, p_57508_);
	}

	public boolean canSurvive(BlockState p_57499_, LevelReader p_57500_, BlockPos p_57501_) {
		return canSupportCenter(p_57500_, p_57501_.below(), Direction.UP);
	}

	public void animateTick(BlockState p_222593_, Level level, BlockPos blockPos, RandomSource p_222596_) {
		double d0 = (double)blockPos.getX() + 0.5D;
		double d1 = (double)blockPos.getY() + 0.7D;
		double d2 = (double)blockPos.getZ() + 0.5D;
		level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		level.addParticle(this.flameParticle.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}
}