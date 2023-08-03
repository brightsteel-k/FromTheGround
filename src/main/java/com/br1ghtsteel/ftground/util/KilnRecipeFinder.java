package com.br1ghtsteel.ftground.util;

import java.util.List;

import com.br1ghtsteel.ftground.recipes.BakingRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;

public class KilnRecipeFinder {

	private static final List<String> STONE_RECIPE_IDS = List.of("stone_from_baking", "andesite_from_baking",
			"deepslate_from_baking", "diorite_from_baking", "dolerite_from_baking", "granite_from_baking",
			"lamprophyre_from_baking", "limestone_from_baking", "schist_from_baking", "shale_from_baking");

	public static RecipeBookCategories findRecipeBookCategory(Recipe<?> recipe) {
		if (STONE_RECIPE_IDS.contains(recipe.getId().getPath())) {
			return BakingRecipe.RECIPE_CATEGORY_STONE;
		}
		return BakingRecipe.RECIPE_CATEGORY_MISC;
	}
}
