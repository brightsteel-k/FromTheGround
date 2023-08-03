package com.br1ghtsteel.ftground.recipes;

import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.items.SulfuricAcidItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class AcidMixingRecipe extends MixingRecipe {
	
	protected final boolean isSulfuric;
	
	public AcidMixingRecipe(RecipeType<?> recipeType, ResourceLocation id, Ingredient ingredient, Ingredient base) {
		super(recipeType, id, ingredient, base, ItemStack.EMPTY);
		isSulfuric = ingredient.test(new ItemStack(ItemsInit.SULFUR.get()));
	}
	
	public AcidMixingRecipe(ResourceLocation id, Ingredient ingredient, Ingredient base) {
		this(RecipesInit.MIXING_RECIPE.get(), id, ingredient, base);
	}

	@Override
	public boolean matches(Container container, Level level) {
		if (!super.matches(container, level)) {
			return false;
		}
		
		if (this.base.test(new ItemStack(ItemsInit.SULFURIC_ACID.get()))) {
			if (isSulfuric) {
				return SulfuricAcidItem.getConcentration(container.getItem(1)) < 4;
			} else {
				return SulfuricAcidItem.getConcentration(container.getItem(1)) == 1;
			}
		} else {
			return true;
		}
	}

	@Override
	public ItemStack assemble(Container container) {
		ItemStack mixture = container.getItem(1).copy();
		if (isSulfuric) {
			return assembleSulfuricAcid(mixture);
		} else {
			return assembleNitricAcid(mixture);
		}
	}
	
	private ItemStack assembleSulfuricAcid(ItemStack mixture) {
		CompoundTag tag = mixture.getOrCreateTag();
		int s = tag.contains("Sulfur") ? tag.getInt("Sulfur") : 0;
		int c = mixture.is(ItemsInit.SULFURIC_ACID.get()) ? SulfuricAcidItem.getConcentration(mixture) : -1;
		if (s >= Math.pow(2, c + 2) - 1) {
			if (c == -1) {
				mixture = new ItemStack(ItemsInit.SULFURIC_ACID.get(), 1);
			}
			tag.putInt("Concentration", ++c);
			tag.putInt("Sulfur", 0);
			mixture.setTag(tag);
			return mixture;
		} else {
			tag.putInt("Sulfur", s + 1);
			mixture.setTag(tag);
			return mixture;
		}
	}
	
	private ItemStack assembleNitricAcid(ItemStack mixture) {
		CompoundTag tag = mixture.getOrCreateTag();
		int s = tag.contains("Saltpeter") ? tag.getInt("Saltpeter") : 0;
		if (s >= 3) {
			return new ItemStack(ItemsInit.NITRIC_ACID.get());
		} else {
			tag.putInt("Saltpeter", s + 1);
			mixture.setTag(tag);
			return mixture;
		}
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipesInit.ACID_MIXING_SERIALIZER.get();
	}

	public static class Type implements RecipeType<AcidMixingRecipe> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();
		public static final String ID = "acid_mixing";
	}
	
	public static class Serializer implements RecipeSerializer<AcidMixingRecipe> {

		private final AcidMixingRecipe.CookieBaker<AcidMixingRecipe> factory;

		public Serializer(AcidMixingRecipe.CookieBaker<AcidMixingRecipe> factory) {
			this.factory = factory;
		}

		@Override
		public AcidMixingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
			JsonElement ingredientElement = (JsonElement) (GsonHelper.isArrayNode(jsonObject, "ingredient")
					? GsonHelper.getAsJsonArray(jsonObject, "ingredient")
					: GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
			JsonElement baseElement = (JsonElement) (GsonHelper.isArrayNode(jsonObject, "base")
					? GsonHelper.getAsJsonArray(jsonObject, "base")
					: GsonHelper.getAsJsonObject(jsonObject, "base"));
			Ingredient ingredient = Ingredient.fromJson(ingredientElement);
			Ingredient base = Ingredient.fromJson(baseElement);
			// Forge: Check if primitive string to keep vanilla or a object which can
			// contain a count field.
			return this.factory.create(id, ingredient, base);
		}

		@Override
		public AcidMixingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf friendlyByteBuf) {
			Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
			Ingredient base = Ingredient.fromNetwork(friendlyByteBuf);
			return this.factory.create(id, ingredient, base);
		}

		@Override
		public void toNetwork(FriendlyByteBuf friendlyByteBuf, AcidMixingRecipe recipe) {
			recipe.ingredient.toNetwork(friendlyByteBuf);
			recipe.base.toNetwork(friendlyByteBuf);
		}
	}

	public interface CookieBaker<T extends AcidMixingRecipe> {
		T create(ResourceLocation id, Ingredient ingredient, Ingredient base);
	}
}
