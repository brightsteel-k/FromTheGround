package com.br1ghtsteel.ftground.recipes;

import java.util.Arrays;
import java.util.List;

import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class MixingRecipe implements Recipe<Container> {
	protected final RecipeType<?> type;
	protected final ResourceLocation id;
	protected final Ingredient ingredient;
	protected final Ingredient base;
	protected final ItemStack result;
	
	public static final List<Potion> VALID_BASE_POTIONS = Arrays.asList(Potions.WATER, Potions.AWKWARD, Potions.MUNDANE, Potions.THICK);

	public static final RecipeBookType RECIPE_BOOK_TYPE = RecipeBookType.create("Kiln");
	public static final RecipeBookCategories RECIPE_CATEGORY_SEARCH = RecipeBookCategories.create("Search",
			new ItemStack(Items.COMPASS));

	public MixingRecipe(RecipeType<?> recipeType, ResourceLocation id, Ingredient ingredient, Ingredient base,
			ItemStack result) {
		this.type = recipeType;
		this.id = id;
		this.ingredient = ingredient;
		this.base = base;
		this.result = result;
	}
	
	public MixingRecipe(ResourceLocation id, Ingredient ingredient, Ingredient base,
			ItemStack result) {
		this.type = RecipesInit.MIXING_RECIPE.get();
		this.id = id;
		this.ingredient = ingredient;
		this.base = base;
		this.result = result;
	}

	public boolean matches(Container container, Level level) {
		if (this.base.test(new ItemStack(Items.POTION))) {
			ItemStack itemstack1 = container.getItem(1);
			if (!itemstack1.isEmpty() && !itemstack1.is(Items.POTION) && !itemstack1.is(Items.SPLASH_POTION)) {
				return false;
			} else if (!VALID_BASE_POTIONS.contains(PotionUtils.getPotion(itemstack1))) {
				return false;
			}
			return this.ingredient.test(container.getItem(0));
		} else {
			return this.ingredient.test(container.getItem(0)) && this.base.test(container.getItem(1));
		}
	}

	public ItemStack assemble(Container container) {
		return this.result.copy();
	}

	public boolean canCraftInDimensions(int x, int y) {
		return true;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		nonnulllist.add(this.base);
		return nonnulllist;
	}

	public ItemStack getResultItem() {
		return this.result;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public RecipeType<?> getType() {
		return this.type;
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ItemsInit.MIXING_STAND.get());
	}

	public RecipeSerializer<?> getSerializer() {
		return RecipesInit.MIXING_SERIALIZER.get();
	}
	
	public static ItemStack convertBasePotion(ItemStack itemstack) {
		if (itemstack.is(Items.POTION) || itemstack.is(Items.SPLASH_POTION)) {
			if (VALID_BASE_POTIONS.contains(PotionUtils.getPotion(itemstack))) {
				ItemStack itemstack1 = itemstack.is(Items.POTION) ? itemstack : new ItemStack(Items.POTION, 1);
				PotionUtils.setPotion(itemstack1, Potions.WATER);
				return itemstack1;
			}
		}
		return itemstack;
	}

	public static class Type implements RecipeType<MixingRecipe> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();
		public static final String ID = "mixing";
	}

	public static class Serializer implements RecipeSerializer<MixingRecipe> {

		private final MixingRecipe.CookieBaker<MixingRecipe> factory;

		public Serializer(MixingRecipe.CookieBaker<MixingRecipe> factory) {
			this.factory = factory;
		}

		@Override
		public MixingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
			JsonElement ingredientElement = (JsonElement) (GsonHelper.isArrayNode(jsonObject, "ingredient")
					? GsonHelper.getAsJsonArray(jsonObject, "ingredient")
					: GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
			JsonElement inputElement = (JsonElement) (GsonHelper.isArrayNode(jsonObject, "base")
					? GsonHelper.getAsJsonArray(jsonObject, "base")
					: GsonHelper.getAsJsonObject(jsonObject, "base"));
			Ingredient ingredient = Ingredient.fromJson(ingredientElement);
			Ingredient input = Ingredient.fromJson(inputElement);
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
			return this.factory.create(id, ingredient, input, itemstack);
		}

		@Override
		public MixingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf friendlyByteBuf) {
			Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
			Ingredient base = Ingredient.fromNetwork(friendlyByteBuf);
			ItemStack result = friendlyByteBuf.readItem();
			return this.factory.create(id, ingredient, base, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, MixingRecipe recipe) {
			recipe.ingredient.toNetwork(friendlyByteBuf);
			recipe.base.toNetwork(friendlyByteBuf);
			friendlyByteBuf.writeItem(recipe.result);
		}
	}

	public interface CookieBaker<T extends MixingRecipe> {
		T create(ResourceLocation id, Ingredient ingredient, Ingredient base, ItemStack result);
	}
}
