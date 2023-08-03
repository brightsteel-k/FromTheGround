package com.br1ghtsteel.ftground.menus;

import com.br1ghtsteel.ftground.core.MenuTypesInit;
import com.br1ghtsteel.ftground.tags.FTGItemTags;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MixingStandMenu extends AbstractContainerMenu {
	private static final int BOTTLE_SLOT_START = 0;
	private static final int BOTTLE_SLOT_END = 2;
	private static final int INGREDIENT_SLOT = 3;
	private static final int SLOT_COUNT = 4;
	private static final int DATA_COUNT = 5;
	private static final int INV_SLOT_START = 4;
	private static final int INV_SLOT_END = 31;
	private static final int USE_ROW_SLOT_START = 31;
	private static final int USE_ROW_SLOT_END = 40;
	private final Container mixingStand;
	private final ContainerData mixingStandData;
	private final Slot ingredientSlot;

	public MixingStandMenu(int containerId, Inventory inventory) {
	      this(containerId, inventory, new SimpleContainer(4), new SimpleContainerData(5));
	}

	public MixingStandMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
	      super(MenuTypesInit.MIXING_STAND.get(), containerId);
	      checkContainerSize(container, SLOT_COUNT);
	      checkContainerDataCount(containerData, DATA_COUNT);
	      this.mixingStand = container;
	      this.mixingStandData = containerData;
	      this.addSlot(new MixingStandMenu.MixtureSlot(container, 0, 46, 51));
	      this.addSlot(new MixingStandMenu.MixtureSlot(container, 1, 79, 58));
	      this.addSlot(new MixingStandMenu.MixtureSlot(container, 2, 112, 51));
	      this.ingredientSlot = this.addSlot(new MixingStandMenu.IngredientsSlot(container, 3, 79, 17));
	      this.addDataSlots(containerData);

	      for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 9; ++j) {
	            this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	         }
	      }

	      for(int k = 0; k < 9; ++k) {
	         this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
	      }

	   }

	public boolean stillValid(Player player) {
		return this.mixingStand.stillValid(player);
	}

	public ItemStack quickMoveStack(Player player, int slotId) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotId);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if ((slotId < 0 || slotId > 2) && slotId != 3) {
				if (this.ingredientSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, 3, 4, false)) {
						return ItemStack.EMPTY;
					}
				} else if (MixingStandMenu.MixtureSlot.mayPlaceItem(itemstack)) {
					if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotId >= INV_SLOT_START && slotId < INV_SLOT_END) {
					if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotId >= USE_ROW_SLOT_START && slotId < USE_ROW_SLOT_END) {
					if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	public int getMixingTicks() {
		return this.mixingStandData.get(0);
	}
	
	public int getCurrentMixtures() {
		return this.mixingStandData.get(1);
	}
	
	public int getTotalAcidityOfSlot(int slot) {
		return this.mixingStandData.get(2 + slot);
	}

	static class IngredientsSlot extends Slot {
		public IngredientsSlot(Container container, int x, int y, int z) {
			super(container, x, y, z);
		}

		@Override
		public boolean mayPlace(ItemStack itemstack) {
			return itemstack.is(FTGItemTags.MIXING_INGREDIENTS);
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}

	static class MixtureSlot extends Slot {
		public MixtureSlot(Container container, int x, int y, int z) {
			super(container, x, y, z);
		}

		@Override
		public boolean mayPlace(ItemStack itemstack) {
			return mayPlaceItem(itemstack);
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
		
		public static boolean mayPlaceItem(ItemStack itemstack) {
			return itemstack.is(FTGItemTags.MIXING_BASES);
		}
	}
}
