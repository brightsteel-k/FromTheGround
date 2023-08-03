package com.br1ghtsteel.ftground.block_entities;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.blocks.MixingStandBlock;
import com.br1ghtsteel.ftground.core.BlockEntityTypesInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.menus.MixingStandMenu;
import com.br1ghtsteel.ftground.recipes.MixingRecipe;
import com.br1ghtsteel.ftground.tags.FTGItemTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MixingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
	private static final int INGREDIENT_SLOT = 3;
	private static final int[] SLOTS_FOR_UP = new int[] { 3 };
	private static final int[] SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3 };
	private static final int[] SLOTS_FOR_SIDES = new int[] { 0, 1, 2 };
	public static final int DATA_MIX_TIME = 0;
	public static final int DATA_CURRENT_MIXTURES = 1;
	public static final int NUM_DATA_VALUES = 5;
	private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
	int mixTime;
	boolean[] currentMixtures = new boolean[3];
	int[] baseAcidities = new int[3];
	private boolean[] lastBottleCount;
	private ItemStack[] lastBases = new ItemStack[3];
	private final RecipeManager.CachedCheck<Container, MixingRecipe> quickCheck;
	private Item ingredient;
	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int id) {
			switch (id) {
				case 0:
					return MixingStandBlockEntity.this.mixTime;
				case 1:
					return MixingStandBlockEntity.this.fetchCurrentMixtures();
				case 2:
					return MixingStandBlockEntity.this.baseAcidities[0];
				case 3:
					return MixingStandBlockEntity.this.baseAcidities[1];
				case 4:
					return MixingStandBlockEntity.this.baseAcidities[2];
				default:
					return 0;
			}
		}

		public void set(int id, int value) {
			switch (id) {
				case 0:
					MixingStandBlockEntity.this.mixTime = value;
					break;
			}

		}

		public int getCount() {
			return 5;
		}
	};

	public MixingStandBlockEntity(BlockPos blockpos, BlockState blockstate) {
		super(BlockEntityTypesInit.MIXING_STAND.get(), blockpos, blockstate);
		this.quickCheck = RecipeManager.createCheck(RecipesInit.MIXING_RECIPE.get());
	}

	protected Component getDefaultName() {
		return Component.translatable("container." + FromTheGround.MOD_ID + ".mixing_stand");
	}

	public int getContainerSize() {
		return this.items.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static void serverTick(Level level, BlockPos blockpos, BlockState blockstate,
			MixingStandBlockEntity blockEntity) {

		boolean hasValidIngredient = isMixable(level, blockEntity, blockEntity.items);
		boolean isMixing = blockEntity.mixTime > 0;
		ItemStack ingredient = blockEntity.items.get(3);
		if (isMixing) {
			--blockEntity.mixTime;
			boolean isCompleted = blockEntity.mixTime == 0;
			if (isCompleted && hasValidIngredient) {
				completeMix(level, blockEntity, blockpos, blockEntity.items);
				setChanged(level, blockpos, blockstate);
			} else if (!hasValidIngredient || !ingredient.is(blockEntity.ingredient)) {
				blockEntity.mixTime = 0;
				setChanged(level, blockpos, blockstate);
			}
		} else if (hasValidIngredient) {
			blockEntity.mixTime = 300;
			blockEntity.ingredient = ingredient.getItem();
			setChanged(level, blockpos, blockstate);
		}
		blockEntity.trackBaseAcidities();

		boolean[] bottleCount = blockEntity.countBottles();
		if (!Arrays.equals(bottleCount, blockEntity.lastBottleCount)) {
			blockEntity.lastBottleCount = bottleCount;
			BlockState newState = blockstate;
			if (!(blockstate.getBlock() instanceof MixingStandBlock)) {
				return;
			}

			for (int i = 0; i < MixingStandBlock.HAS_BOTTLE.length; ++i) {
				newState = newState.setValue(MixingStandBlock.HAS_BOTTLE[i], Boolean.valueOf(bottleCount[i]));
			}

			level.setBlock(blockpos, newState, 2);
		}
	}

	private boolean[] countBottles() {
		boolean[] aboolean = new boolean[3];

		for (int i = 0; i < 3; ++i) {
			if (!this.items.get(i).isEmpty()) {
				aboolean[i] = true;
			}
		}

		return aboolean;
	}
	
	private void trackBaseAcidities() {
		boolean changed = false;
		for (int i = 0; i < 3; i++) {
			if (items.get(i) != lastBases[i]) {
				changed = true;
				lastBases[i] = items.get(i);
			}
		}
		
		if (changed) {
			for (int i = 0; i < 3; i++) {
				baseAcidities[i] = calculateAcidity(items.get(i));
			}
		}
	}
	
	private static int calculateAcidity(ItemStack item) {
		CompoundTag tag = item.getTag();
		
		if (item.is(ItemsInit.NITRIC_ACID.get())) {
			return 104;
		} else if (tag == null) {
			return -1;
		} else if (tag.contains("Saltpeter")) {
			return 100 + Math.max(Math.min(tag.getInt("Saltpeter"), 4), 0);
		} else if (tag.contains("Sulfur")) {
			int sulfur = Math.max(Math.min(tag.getInt("Sulfur"), 32), 0);
			if (!tag.contains("Concentration")) {
				return sulfur;
			} else {
				int c = Math.max(Math.min(tag.getInt("Concentration"), 4), 0);
				return sulfur + (int)Math.pow(2, c + 2) - 2;
			}
		}
		
		return -1;
	}

	private static boolean isMixable(Level level, MixingStandBlockEntity blockEntity, NonNullList<ItemStack> items) {
		ItemStack ingredient = items.get(3);
		boolean mixable = false;
		if (!ingredient.isEmpty()) {
			for (int i = 0; i < 3; i++) {
				ItemStack base = items.get(i);
				if (!base.isEmpty() && blockEntity.quickCheck.getRecipeFor(new SimpleContainer(ingredient, base), level).isPresent()) {
					mixable = true;
					blockEntity.currentMixtures[i] = true;
				} else if (blockEntity.currentMixtures[i]) {
					blockEntity.currentMixtures[i] = false;
				}
			}
		}
		return mixable;
	}

	private static void completeMix(Level level, MixingStandBlockEntity blockEntity, BlockPos blockpos, NonNullList<ItemStack> items) {
		ItemStack ingredient = items.get(3);

		// Form resulting mixtures:
		for (int i : SLOTS_FOR_SIDES) {
        	ItemStack base = MixingRecipe.convertBasePotion(items.get(i));
            Optional<MixingRecipe> output = blockEntity.quickCheck.getRecipeFor(new SimpleContainer(ingredient, base), level);
            
            if (output.isPresent()) {
                items.set(i, output.get().assemble(new SimpleContainer(ingredient, items.get(i))));
            }
        }
		
		// Deal with ingredient
		if (ingredient.hasCraftingRemainingItem()) {
			ItemStack itemstack1 = ingredient.getCraftingRemainingItem();
			ingredient.shrink(1);
			if (ingredient.isEmpty()) {
				ingredient = itemstack1;
			} else {
				Containers.dropItemStack(level, (double) blockpos.getX(), (double) blockpos.getY(),
						(double) blockpos.getZ(), itemstack1);
			}
		} else {
			ingredient.shrink(1);
		}

		items.set(3, ingredient);
		level.levelEvent(1035, blockpos, 0);
	}

	public void load(CompoundTag tag) {
		super.load(tag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.items);
		this.mixTime = tag.getShort("MixTime");
	}

	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putShort("MixTime", (short) this.mixTime);
		ContainerHelper.saveAllItems(tag, this.items);
	}
	
	public int fetchCurrentMixtures() {
		int a = currentMixtures[0] ? 1 : 0;
		int b = currentMixtures[1] ? 2 : 0;
		int c = currentMixtures[2] ? 4 : 0;
		return a + b + c;
	}

	public ItemStack getItem(int slot) {
		return slot >= 0 && slot < this.items.size() ? this.items.get(slot) : ItemStack.EMPTY;
	}

	public ItemStack removeItem(int slot, int e) {
		return ContainerHelper.removeItem(this.items, slot, e);
	}

	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.items, slot);
	}

	public void setItem(int slot, ItemStack newItem) {
		if (slot >= 0 && slot < this.items.size()) {
			this.items.set(slot, newItem);
		}

	}

	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
					(double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	public boolean canPlaceItem(int slot, ItemStack itemstack) {
		if (slot == 3) {
			return itemstack.is(FTGItemTags.MIXING_INGREDIENTS);
		} else {
			return itemstack.is(FTGItemTags.MIXING_BASES) && this.getItem(slot).isEmpty();
		}
	}

	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.UP) {
			return SLOTS_FOR_UP;
		} else {
			return direction == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
		}
	}

	public boolean canPlaceItemThroughFace(int slot, ItemStack itemstack, @Nullable Direction direction) {
		return this.canPlaceItem(slot, itemstack);
	}

	public boolean canTakeItemThroughFace(int slot, ItemStack itemstack, Direction direction) {
		return slot == 3 ? itemstack.is(Items.GLASS_BOTTLE) : true;
	}

	public void clearContent() {
		this.items.clear();
	}

	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new MixingStandMenu(id, inventory, this, this.dataAccess);
	}

	// No idea what this does... here be dragons beyond this point
	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers = net.minecraftforge.items.wrapper.SidedInvWrapper
			.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(
			net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null
				&& capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == Direction.UP)
				return handlers[0].cast();
			else if (facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[2].cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (int x = 0; x < handlers.length; x++)
			handlers[x].invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN,
				Direction.NORTH);
	}

}
