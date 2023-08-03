package com.br1ghtsteel.ftground;

import com.br1ghtsteel.ftground.core.BlockEntityTypesInit;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.EffectsInit;
import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.core.FluidTypesInit;
import com.br1ghtsteel.ftground.core.FluidsInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.core.MenuTypesInit;
import com.br1ghtsteel.ftground.core.ParticlesInit;
import com.br1ghtsteel.ftground.core.RecipesInit;
import com.br1ghtsteel.ftground.util.EntityModifications;
import com.br1ghtsteel.ftground.util.RockType;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FromTheGround.MOD_ID)
public class FromTheGround {
    public static final String MOD_ID = "ftground";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FromTheGround() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        BlocksInit.BLOCKS.register(modEventBus);
        ItemsInit.ITEMS.register(modEventBus);

        BlockEntityTypesInit.BLOCK_ENTITY_TYPES.register(modEventBus);
        ParticlesInit.PARTICLES.register(modEventBus);
        EntityTypesInit.ENTITY_TYPES.register(modEventBus);
        RecipesInit.RECIPE_SERIALIZERS.register(modEventBus);
        RecipesInit.RECIPE_TYPES.register(modEventBus);
        EffectsInit.EFFECTS.register(modEventBus);
        FluidsInit.FLUIDS.register(modEventBus);
        FluidTypesInit.FLUID_TYPES.register(modEventBus);
        MenuTypesInit.MENU_TYPES.register(modEventBus);
        

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    	RockType.initializeRockTypes();
    	EntityModifications.init();
    	FluidTypesInit.registerFluidInteractions();
    	ItemsInit.registerDispenserBehaviour();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
    	@SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
