package com.br1ghtsteel.ftground.recipes;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class BakingRecipe extends AbstractCookingRecipe {
	
	public static final RecipeBookType RECIPE_BOOK_TYPE = RecipeBookType.create("Kiln");
	public static final RecipeBookCategories RECIPE_CATEGORY_SEARCH = RecipeBookCategories.create("Search", new ItemStack(Items.COMPASS));
	public static final RecipeBookCategories RECIPE_CATEGORY_STONE = RecipeBookCategories.create("Stones", new ItemStack(ItemsInit.SHALE.get()));
	public static final RecipeBookCategories RECIPE_CATEGORY_MISC = RecipeBookCategories.create("Miscellaneous", new ItemStack(ItemsInit.CLAY_BRICK.get()));
	
	public BakingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience,
			int cookingTime) {
		super(RecipesInit.BAKING_RECIPE.get(), id, group, ingredient, result, experience, cookingTime);
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(BlocksInit.KILN.get());
	}

	public RecipeSerializer<?> getSerializer() {
		return RecipesInit.BAKING_SERIALIZER.get();
	}

	public static class Type implements RecipeType<BakingRecipe> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();
		public static final String ID = "baking";
	}

	public static class Serializer implements RecipeSerializer<BakingRecipe> {

		private final int defaultCookingTime;
		private final BakingRecipe.CookieBaker<BakingRecipe> factory;

		public Serializer(BakingRecipe.CookieBaker<BakingRecipe> factory, int defaultCookingTimes) {
			this.defaultCookingTime = defaultCookingTimes;
			this.factory = factory;
		}

		@Override
		public BakingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
			String s = GsonHelper.getAsString(jsonObject, "group", "");
			JsonElement jsonelement = (JsonElement) (GsonHelper.isArrayNode(jsonObject, "ingredient")
					? GsonHelper.getAsJsonArray(jsonObject, "ingredient")
					: GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			// Forge: Check if primitive string to keep vanilla or a object which can
			// contain a count field.
			if (!jsonObject.has("result"))
				throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
			ItemStack itemstack;
			if (jsonObject.get("result").isJsonObject())
				itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
			else {
				String s1 = GsonHelper.getAsString(jsonObject, "result");
				ResourceLocation resourcelocation = new ResourceLocation(s1);
				itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
					return new IllegalStateException("Item: " + s1 + " does not exist");
				}));
			}
			float f = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
			int i = GsonHelper.getAsInt(jsonObject, "cookingtime", this.defaultCookingTime);
			return this.factory.create(id, s, ingredient, itemstack, f, i);
		}

		@Override
		public BakingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf friendlyByteBuf) {
			String s = friendlyByteBuf.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
			ItemStack itemstack = friendlyByteBuf.readItem();
			float f = friendlyByteBuf.readFloat();
			int i = friendlyByteBuf.readVarInt();
			return this.factory.create(id, s, ingredient, itemstack, f, i);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, BakingRecipe recipe) {
			friendlyByteBuf.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(friendlyByteBuf);
			friendlyByteBuf.writeItem(recipe.result);
			friendlyByteBuf.writeFloat(recipe.experience);
			friendlyByteBuf.writeVarInt(recipe.cookingTime);
		}
	}

	public interface CookieBaker<T extends BakingRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experiences,
				int cookingTime);
	}
}
