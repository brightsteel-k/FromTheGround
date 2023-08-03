package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.recipes.AcidMixingRecipe;
import com.br1ghtsteel.ftground.recipes.BakingRecipe;
import com.br1ghtsteel.ftground.recipes.MixingRecipe;
import com.br1ghtsteel.ftground.recipes.StoneToolRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipesInit {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, FromTheGround.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FromTheGround.MOD_ID);
	
	public static final RegistryObject<RecipeSerializer<StoneToolRecipe>> STONE_TOOLS_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_special_stonetool", 
			() -> new StoneToolRecipe.Serializer());
	
	public static final RegistryObject<RecipeType<BakingRecipe>> BAKING_RECIPE = RECIPE_TYPES.register("baking", () -> BakingRecipe.Type.INSTANCE);
	public static final RegistryObject<RecipeSerializer<BakingRecipe>> BAKING_SERIALIZER = RECIPE_SERIALIZERS.register("baking", () -> new BakingRecipe.Serializer(BakingRecipe::new, 200));
	
	public static final RegistryObject<RecipeType<MixingRecipe>> MIXING_RECIPE = RECIPE_TYPES.register("mixing", () -> MixingRecipe.Type.INSTANCE);
	public static final RegistryObject<RecipeSerializer<MixingRecipe>> MIXING_SERIALIZER = RECIPE_SERIALIZERS.register("mixing", () -> new MixingRecipe.Serializer(MixingRecipe::new));
	
	public static final RegistryObject<RecipeType<AcidMixingRecipe>> ACID_MIXING_RECIPE = RECIPE_TYPES.register("acid_mixing", () -> AcidMixingRecipe.Type.INSTANCE);
	public static final RegistryObject<RecipeSerializer<AcidMixingRecipe>> ACID_MIXING_SERIALIZER = RECIPE_SERIALIZERS.register("acid_mixing", () -> new AcidMixingRecipe.Serializer(AcidMixingRecipe::new));
}
