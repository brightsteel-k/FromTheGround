package com.br1ghtsteel.ftground.fluids;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.FluidTypesInit;
import com.br1ghtsteel.ftground.core.FluidsInit;
import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class MoltenSaltpeterFluid extends ForgeFlowingFluid {
	protected MoltenSaltpeterFluid(Properties properties) {
		super(properties);
	}

	@Override
	public Fluid getFlowing() {
		return FluidsInit.MOLTEN_SALTPETER_FLOWING.get();
	}

	@Override
	public Fluid getSource() {
		return FluidsInit.MOLTEN_SALTPETER_SOURCE.get();
	}

	@Override
	public Item getBucket() {
		return ItemsInit.MOLTEN_SALTPETER_BUCKET.get();
	}

	@Override
	public void animateTick(Level level, BlockPos blockpos, FluidState fluidState, RandomSource rnd) {
		if (!fluidState.isSource() && !fluidState.getValue(FALLING)) {
			if (rnd.nextInt(100) == 0) {
				level.playLocalSound((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
						(double) blockpos.getZ() + 0.5D, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
						rnd.nextFloat() * 0.25F + 0.75F, rnd.nextFloat() + 0.5F, false);
			}
		}

		if (rnd.nextInt(200) == 0) {
			level.playLocalSound((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(),
					SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F,
					0.9F + rnd.nextFloat() * 0.15F, false);
		}
	}

	public void randomTick(Level level, BlockPos blockpos, FluidState fluidState, RandomSource rnd) {
		if (fluidState.isSource()) {
			checkForAdjacentGlass(level, blockpos, rnd);
		}
		
		if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
			int i = rnd.nextInt(3);
			if (i > 0) {
				BlockPos newBlockpos = blockpos;

				for (int j = 0; j < i; ++j) {
					newBlockpos = newBlockpos.offset(rnd.nextInt(3) - 1, 1, rnd.nextInt(3) - 1);
					if (!level.isLoaded(newBlockpos)) {
						return;
					}

					BlockState blockstate = level.getBlockState(newBlockpos);
					if (blockstate.isAir()) {
						if (this.hasFlammableNeighbours(level, newBlockpos)) {
							level.setBlockAndUpdate(newBlockpos,
									net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(level,
											newBlockpos, blockpos, Blocks.FIRE.defaultBlockState()));
							return;
						}
					} else if (blockstate.getMaterial().blocksMotion()) {
						return;
					}
				}
			} else {
				for (int k = 0; k < 3; ++k) {
					BlockPos blockpos1 = blockpos.offset(rnd.nextInt(3) - 1, 0, rnd.nextInt(3) - 1);
					if (!level.isLoaded(blockpos1)) {
						return;
					}

					if (level.isEmptyBlock(blockpos1.above()) && this.isFlammable(level, blockpos1, Direction.UP)) {
						level.setBlockAndUpdate(blockpos1.above(),
								net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(level,
										blockpos1.above(), blockpos, Blocks.FIRE.defaultBlockState()));
					}
				}
			}
		}
	}

	private boolean hasFlammableNeighbours(LevelReader level, BlockPos blockpos) {
		for (Direction direction : Direction.values()) {
			if (this.isFlammable(level, blockpos.relative(direction), direction.getOpposite())) {
				return true;
			}
		}

		return false;
	}

	private boolean isFlammable(LevelReader level, BlockPos pos, Direction face) {
		return pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight()
				&& !level.hasChunkAt(pos) ? false : level.getBlockState(pos).isFlammable(level, pos, face);
	}
	
	private static void checkForAdjacentGlass(Level level, BlockPos blockpos, RandomSource rnd) {
		if (level.isClientSide || rnd.nextBoolean()) return;
		
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		List<Vec3i> glassPositions = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -1; y <= 1; y++) {
					mutableBlockPos.setWithOffset(blockpos, x, y, z);
					if (level.getBlockState(mutableBlockPos).is(Blocks.GLASS) || level.getBlockState(mutableBlockPos).is(Blocks.GLASS_PANE)) {
						glassPositions.add(mutableBlockPos.immutable());
					}
				}
			}
		}
		if (glassPositions.isEmpty()) return;
		
		BlockPos target = new BlockPos(glassPositions.get(rnd.nextInt(glassPositions.size())));
		if (tryStrengthenGlass(level, target, rnd)) {
			level.setBlock(blockpos, BlocksInit.MOLTEN_SALTPETER.get().defaultBlockState().setValue(LiquidBlock.LEVEL, 2), 3);
		}
	}
	
	private static boolean tryStrengthenGlass(Level level, BlockPos blockpos, RandomSource rnd) {
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		int saltpeterBlocks = 0;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = 0; y <= 1; y++) {
					mutableBlockPos.setWithOffset(blockpos, x, y, z);
					if (level.getBlockState(mutableBlockPos).is(BlocksInit.MOLTEN_SALTPETER.get())) {
						saltpeterBlocks++;
					}
				}
			}
		}
		
		if (saltpeterBlocks > 8) {
			level.setBlock(blockpos, level.getBlockState(blockpos).is(Blocks.GLASS_PANE) ? BlocksInit.STRENGTHENED_GLASS.get().defaultBlockState() : BlocksInit.STRENGTHENED_GLASS.get().defaultBlockState(), 3);
			level.playSound((Player)null, blockpos, SoundEvents.GLASS_HIT, SoundSource.BLOCKS, 5.0f, rnd.nextFloat() / 2f + 0.75f);
			return rnd.nextInt(100) < 40;
		} else {
			return false;
		}
	}

	@Override
	@Nullable
	public ParticleOptions getDripParticle() {
		return null;
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
		return 2;
	}

	@Override
	public BlockState createLegacyBlock(FluidState fluidState) {
		return BlocksInit.MOLTEN_SALTPETER.get().defaultBlockState().setValue(LiquidBlock.LEVEL,
				Integer.valueOf(getLegacyLevel(fluidState)));
	}

	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == FluidsInit.MOLTEN_SALTPETER_SOURCE.get() || fluid == FluidsInit.MOLTEN_SALTPETER_FLOWING.get();
	}

	@Override
	public int getDropOff(LevelReader level) {
		return 2;
	}

	@Override
	public int getTickDelay(LevelReader level) {
		return 30;
	}

	public int getSpreadDelay(Level level, BlockPos blockpos, FluidState fluidState1, FluidState fluidState2) {
		int i = this.getTickDelay(level);
		if (!fluidState1.isEmpty() && !fluidState2.isEmpty() && !fluidState1.getValue(FALLING)
				&& !fluidState2.getValue(FALLING)
				&& fluidState2.getHeight(level, blockpos) > fluidState1.getHeight(level, blockpos)
				&& level.getRandom().nextInt(4) != 0) {
			i *= 4;
		}

		return i;
	}

	@Override
	public boolean canBeReplacedWith(FluidState fluidState, BlockGetter level, BlockPos blockpos, Fluid fluid,
			Direction direction) {
		return false;
	}

	@Override
	protected float getExplosionResistance() {
		return 100.0F;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
	}

	protected boolean isRandomlyTicking() {
		return true;
	}
	
	@Override
	public void tick(Level p_75995_, BlockPos p_75996_, FluidState p_75997_) {
		super.tick(p_75995_, p_75996_, p_75997_);
	}

	public static class Flowing extends MoltenSaltpeterFluid {
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

	public static class Source extends MoltenSaltpeterFluid {
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