package com.br1ghtsteel.ftground.block_entities;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.core.BlockEntityTypesInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.menus.KilnMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {

	public KilnBlockEntity(BlockPos blockpos, BlockState blockstate) {
		super(BlockEntityTypesInit.KILN.get(), blockpos, blockstate, RecipesInit.BAKING_RECIPE.get());
	}

	protected Component getDefaultName() {
		return Component.translatable("container." + FromTheGround.MOD_ID + ".kiln");
	}

	protected int getBurnDuration(ItemStack itemstack) {
		return super.getBurnDuration(itemstack) / 2;
	}

	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new KilnMenu(containerId, inventory, this, this.dataAccess);
	}
}
