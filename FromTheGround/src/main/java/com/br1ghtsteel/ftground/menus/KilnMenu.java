package com.br1ghtsteel.ftground.menus;

import com.br1ghtsteel.ftground.core.MenuTypesInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.recipes.BakingRecipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;

public class KilnMenu extends AbstractFurnaceMenu {
	public KilnMenu(int containerId, Inventory inventory) {
		super(MenuTypesInit.KILN.get(), RecipesInit.BAKING_RECIPE.get(), BakingRecipe.RECIPE_BOOK_TYPE, containerId, inventory);
	}

	public KilnMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
		super(MenuTypesInit.KILN.get(), RecipesInit.BAKING_RECIPE.get(), BakingRecipe.RECIPE_BOOK_TYPE, containerId, inventory, container,
				containerData);
	}
}
