package com.br1ghtsteel.ftground.blocks;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.BlockStateProperties;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class FertilizerBoxBlock extends Block implements IPlantable, WorldlyContainerHolder {

	private static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_FERTILIZER;
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);

	public FertilizerBoxBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)));
	}

	public boolean canSurvive(BlockState blockstate, LevelReader level, BlockPos blockpos) {
		BlockState belowState = level.getBlockState(blockpos.below());
		return belowState.is(Blocks.FARMLAND) || belowState.is(BlocksInit.DESSICATED_FARMLAND.get());
	}

	public BlockState updateShape(BlockState blockstate, Direction direction, BlockState otherState,
			LevelAccessor level, BlockPos blockpos, BlockPos otherPos) {
		return !blockstate.canSurvive(level, blockpos) ? Blocks.AIR.defaultBlockState()
				: super.updateShape(blockstate, direction, otherState, level, blockpos, otherPos);
	}

	public InteractionResult use(BlockState blockstate, Level level, BlockPos blockpos, Player player,
			InteractionHand hand, BlockHitResult blockHitResult) {
		int i = blockstate.getValue(LEVEL);
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(ItemsInit.SALTPETER.get()) && i < 12) {
			if (!level.isClientSide) {
				addSaltpeter(blockstate, level, blockpos);
				level.playSound((Player) null, blockpos, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else if (itemstack.isEmpty() && i > 0 && hand.equals(InteractionHand.MAIN_HAND)) {
			if (!level.isClientSide) {
				ItemStack saltpeter = new ItemStack(ItemsInit.SALTPETER.get());
				saltpeter.setCount(i);
				player.setItemInHand(hand, saltpeter);
			}

			level.playSound((Player) null, blockpos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
			empty(blockstate, level, blockpos);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	static void empty(BlockState blockstate, LevelAccessor world, BlockPos blockpos) {
		BlockState newState = blockstate.setValue(LEVEL, Integer.valueOf(0));
		world.setBlock(blockpos, newState, 3);
	}

	static void addSaltpeter(BlockState blockstate, LevelAccessor world, BlockPos blockpos) {
		int i = blockstate.getValue(LEVEL);
		BlockState newState = blockstate.setValue(LEVEL, Integer.valueOf(i + 1));
		world.setBlock(blockpos, newState, 3);
	}

	public static boolean removeSaltpeter(LevelAccessor level, BlockPos blockpos) {
		BlockState blockstate = level.getBlockState(blockpos);
		int i = blockstate.getValue(LEVEL);
		if (i > 0) {
			if (!level.isClientSide()) {
				level.setBlock(blockpos, blockstate.setValue(LEVEL, Integer.valueOf(i - 1)), 3);
				level.levelEvent(1500, blockpos, 1);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState blockstate) {
		return true;
	}

	public boolean hasAnalogOutputSignal(BlockState blockstate) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState blockstate, Level level, BlockPos blockpos) {
		return blockstate.getValue(LEVEL);
	}

	public VoxelShape getShape(BlockState blockstate, BlockGetter blockGetter, BlockPos blockpos,
			CollisionContext collisionContext) {
		return SHAPE;
	}

	public boolean isPathfindable(BlockState blockstate, BlockGetter blockGetter, BlockPos blockpos,
			PathComputationType pathComputationType) {
		return false;
	}

	public WorldlyContainer getContainer(BlockState blockstate, LevelAccessor level, BlockPos blockpos) {
		int i = blockstate.getValue(LEVEL);
		return (WorldlyContainer) (i < 12 ? new FertilizerBoxBlock.InputContainer(blockstate, level, blockpos)
				: new FertilizerBoxBlock.EmptyContainer());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(LEVEL);
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.CROP;
	}

	@Override
	public BlockState getPlant(BlockGetter level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() != this)
			return defaultBlockState();
		return state;
	}

	static class EmptyContainer extends SimpleContainer implements WorldlyContainer {
		public EmptyContainer() {
			super(0);
		}

		public int[] getSlotsForFace(Direction direction) {
			return new int[0];
		}

		public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable Direction direction) {
			return false;
		}

		public boolean canTakeItemThroughFace(int i, ItemStack itemstack, Direction direction) {
			return false;
		}
	}

	static class InputContainer extends SimpleContainer implements WorldlyContainer {
		private final BlockState state;
		private final LevelAccessor level;
		private final BlockPos pos;
		private boolean changed;

		public InputContainer(BlockState blockstate, LevelAccessor level, BlockPos blockpos) {
			super(1);
			this.state = blockstate;
			this.level = level;
			this.pos = blockpos;
		}

		public int getMaxStackSize() {
			return 1;
		}

		public int[] getSlotsForFace(Direction direction) {
			return isDirectionHorizontal(direction) ? new int[] { 0 } : new int[0];
		}

		public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable Direction direction) {
			return !this.changed && isDirectionHorizontal(direction) && itemstack.is(ItemsInit.SALTPETER.get());
		}

		public boolean canTakeItemThroughFace(int p_52034_, ItemStack p_52035_, Direction p_52036_) {
			return false;
		}
		
		private boolean isDirectionHorizontal(Direction direction) {
			return direction == Direction.NORTH || direction == Direction.EAST || direction == Direction.SOUTH || direction == Direction.WEST;
		}

		public void setChanged() {
			ItemStack itemstack = this.getItem(0);
			if (!itemstack.isEmpty()) {
				this.changed = true;
				FertilizerBoxBlock.addSaltpeter(this.state, this.level, this.pos);
				this.level.levelEvent(1500, this.pos, 1);
				this.removeItemNoUpdate(0);
			}
		}
	}

}
