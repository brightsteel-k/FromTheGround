package com.br1ghtsteel.ftground.events;

import java.util.List;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.client.models.ThrownFlareModel;
import com.br1ghtsteel.ftground.client.renderers.BatRenderer;
import com.br1ghtsteel.ftground.client.renderers.ExplodingBlockRenderer;
import com.br1ghtsteel.ftground.client.renderers.FlareArrowRenderer;
import com.br1ghtsteel.ftground.client.renderers.ThrownFlareRenderer;
import com.br1ghtsteel.ftground.client.screens.KilnScreen;
import com.br1ghtsteel.ftground.client.screens.MixingStandScreen;
import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.core.FluidsInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.core.MenuTypesInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.recipes.BakingRecipe;
import com.br1ghtsteel.ftground.util.KilnRecipeFinder;
import com.br1ghtsteel.ftground.util.MixingUtil;
import com.br1ghtsteel.ftground.util.StoneVariantsUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

// Use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@Mod.EventBusSubscriber(modid = FromTheGround.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClientEvents
{
	@SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            // Register fluid render types
            ItemBlockRenderTypes.setRenderLayer(FluidsInit.ACID_SOURCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FluidsInit.ACID_FLOWING.get(), RenderType.translucent());

            // Register GUI screens
            MenuScreens.register(MenuTypesInit.KILN.get(), KilnScreen::new);
            MenuScreens.register(MenuTypesInit.MIXING_STAND.get(), MixingStandScreen::new);
        	
            // Register changeable stone tools
        	ItemProperties.register(ItemsInit.STONE_AXE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.STONE_HOE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.STONE_PICKAXE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.STONE_SHOVEL.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.STONE_SWORD.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	
        	// Register changeable stone ores
        	ItemProperties.register(ItemsInit.COAL_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_COAL_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.IRON_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_IRON_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.COPPER_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_COPPER_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.GOLD_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_GOLD_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DIAMOND_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_DIAMOND_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.LAPIS_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_LAPIS_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.REDSTONE_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_REDSTONE_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.EMERALD_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        	ItemProperties.register(ItemsInit.DEEP_EMERALD_ORE.get(), new ResourceLocation(FromTheGround.MOD_ID, "rock_type"), (item, level, entity, entityId) -> StoneVariantsUtil.getRockTypeProperty(item, level, entity, entityId));
        });
    }
    
    @SubscribeEvent
    public static void onRecipeBookCategoriesSetup(RegisterRecipeBookCategoriesEvent event) {
    	event.registerBookCategories(BakingRecipe.RECIPE_BOOK_TYPE, List.of(BakingRecipe.RECIPE_CATEGORY_SEARCH, BakingRecipe.RECIPE_CATEGORY_STONE, BakingRecipe.RECIPE_CATEGORY_MISC));
    	event.registerAggregateCategory(BakingRecipe.RECIPE_CATEGORY_SEARCH, List.of(BakingRecipe.RECIPE_CATEGORY_STONE, BakingRecipe.RECIPE_CATEGORY_MISC));
    	event.registerRecipeCategoryFinder(RecipesInit.BAKING_RECIPE.get(), recipe -> KilnRecipeFinder.findRecipeBookCategory(recipe));
    }
    
	@SubscribeEvent
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(ThrownFlareModel.FLARE_LAYER, ThrownFlareModel::createLayer);
	}
	
	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(EntityTypesInit.THROWN_FLARE.get(), ThrownFlareRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.FLARE_ARROW.get(), FlareArrowRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.THROWN_MIXTURE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.EXPLODING_BLOCK.get(), ExplodingBlockRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.BAT.get(), BatRenderer::new);
	}
	
	@SubscribeEvent
	public static void onRegisterItemColor(RegisterColorHandlersEvent.Item event) {
		event.getItemColors().register(MixingUtil::getSulfuricAcidColor, ItemsInit.SULFURIC_ACID.get());
	}
}
