package com.br1ghtsteel.ftground.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.util.RockType;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class StoneToolRecipe extends CustomRecipe {
	private ShapedRecipe baseRecipe;
	
	public StoneToolRecipe(ResourceLocation id, ShapedRecipe baseRecipe) {
		super(id);
		this.baseRecipe = baseRecipe;
	}
	
	public StoneToolRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(id);
		this.baseRecipe = new ShapedRecipe(id, group, width, height, ingredients, result);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		return baseRecipe.matches(container, level);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack output = baseRecipe.getResultItem().copy();
		
		List<Integer> rocksUsed = collectRocks(container);
		int rockType = calculateDominantRock(rocksUsed);
		CompoundTag compoundtag = new CompoundTag();
		compoundtag.putInt("RockType", rockType);
		output.setTag(compoundtag);
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return baseRecipe.canCraftInDimensions(x, y);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipesInit.STONE_TOOLS_SERIALIZER.get();
	}
	
	public int getWidth() {
		return baseRecipe.getWidth();
	}
	
	public int getHeight() {
		return baseRecipe.getHeight();
	}
	
	public String getGroup() {
		return baseRecipe.getGroup();
	}
	
	@Override
	public ItemStack getResultItem() {
		return baseRecipe.getResultItem();
	}
	
	public NonNullList<Ingredient> getIngredients() {
		return baseRecipe.getIngredients();
	}
	
	public ShapedRecipe getBaseRecipe() {
		return baseRecipe;
	}
	
	private static List<Integer> collectRocks(CraftingContainer container) {
		List<Integer> total = new ArrayList<>();
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack item = container.getItem(i);
			if (item.is(ItemTags.STONE_CRAFTING_MATERIALS)) {
				int rockid = RockType.parseRockId(item.getItem());
				if (rockid >= 0) {
					total.add(rockid);
				}
			}
		}
		return total;
	}
	
	private static int calculateDominantRock(List<Integer> rocks) {
		if (rocks.size() == 1) {
			return rocks.get(0);
		}
		
		Collections.sort(rocks);
		Collections.reverse(rocks);
		int dominantCount = 1;
		int dominantRock = rocks.get(0);
		int currentCount = 1;
		int previousRock = rocks.get(0);
		for (int i = 1; i < rocks.size(); i++) {
			int currentRock = rocks.get(i);
			if (previousRock == currentRock) {
				currentCount++;
				if (currentCount > dominantCount && dominantRock != currentRock) {
					dominantRock = currentRock;
					dominantCount = currentCount;
				}
			} else {
				currentCount = 1;
				previousRock = currentRock;
			}
		}
		return dominantRock;
	}

	public static class Serializer implements RecipeSerializer<StoneToolRecipe> {
		private ShapedRecipe.Serializer baseSerializer;
		
		public Serializer() {
			baseSerializer = new ShapedRecipe.Serializer();
		}
		
		@Override
		public StoneToolRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
			ShapedRecipe baseRecipe = baseSerializer.fromJson(id, jsonObject);
			return new StoneToolRecipe(id, baseRecipe);
		}

		@Override
		public @Nullable StoneToolRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			ShapedRecipe baseRecipe = baseSerializer.fromNetwork(id, buf);
			return new StoneToolRecipe(id, baseRecipe);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, StoneToolRecipe recipe) {
			baseSerializer.toNetwork(buf, recipe.getBaseRecipe());
		}
	}

}
