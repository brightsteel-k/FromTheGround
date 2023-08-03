package com.br1ghtsteel.ftground.fluids;

import java.util.Optional;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.FluidsInit;
import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class AcidFluid extends ForgeFlowingFluid {
	protected AcidFluid(Properties properties) {
		super(properties);
	}

	@Override
	public Fluid getFlowing() {
		return FluidsInit.ACID_FLOWING.get();
	}

	@Override
	public Fluid getSource() {
		return FluidsInit.ACID_SOURCE.get();
	}

	@Override
	public Item getBucket() {
		return ItemsInit.ACID_BUCKET.get();
	}

	@Override
	public void animateTick(Level level, BlockPos blockpos, FluidState fluidState, RandomSource rnd) {
		if (!fluidState.isSource() && !fluidState.getValue(FALLING)) {
			if (rnd.nextInt(64) == 0) {
				level.playLocalSound((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
						(double) blockpos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS,
						rnd.nextFloat() * 0.25F + 0.75F, rnd.nextFloat() + 0.5F, false);
			}
		} else if (rnd.nextInt(10) == 0) {
			level.addParticle(ParticleTypes.UNDERWATER, (double) blockpos.getX() + rnd.nextDouble(),
					(double) blockpos.getY() + rnd.nextDouble(), (double) blockpos.getZ() + rnd.nextDouble(), 0.0D,
					0.0D, 0.0D);
		}
	}

	@Override
	@Nullable
	public ParticleOptions getDripParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}

	@Override
	protected boolean canConvertToSource() {
		return false;
	}

	@Override
	protected void beforeDestroyingBlock(LevelAccessor level, BlockPos blockpos, BlockState blockstate) {
		BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(blockpos) : null;
		Block.dropResources(blockstate, level, blockpos, blockentity);
	}

	@Override
	public int getSlopeFindDistance(LevelReader level) {
		return 3;
	}

	@Override
	public BlockState createLegacyBlock(FluidState fluidState) {
		return BlocksInit.ACID.get().defaultBlockState().setValue(LiquidBlock.LEVEL,
				Integer.valueOf(getLegacyLevel(fluidState)));
	}

	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == FluidsInit.ACID_SOURCE.get() || fluid == FluidsInit.ACID_FLOWING.get();
	}

	@Override
	public int getDropOff(LevelReader level) {
		return 2;
	}

	@Override
	public int getTickDelay(LevelReader level) {
		return 5;
	}

	@Override
	public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockpos, Fluid fluid,
			Direction direction) {
		return direction == Direction.DOWN && !fluidState.is(FluidsInit.ACID_FLOWING.get())
				&& !fluidState.is(FluidsInit.ACID_SOURCE.get());
	}

	@Override
	protected float getExplosionResistance() {
		return 100.0F;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL);
	}

	public static class Flowing extends AcidFluid {
		public Flowing(Properties properties) {
			super(properties);
		}

		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> stateBuilder) {
			super.createFluidStateDefinition(stateBuilder);
			stateBuilder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState fluidState) {
			return fluidState.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState fluidState) {
			return false;
		}
	}

	public static class Source extends AcidFluid {
		public Source(Properties properties) {
			super(properties);
		}

		@Override
		public int getAmount(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState fluidState) {
			return true;
		}
	}
}