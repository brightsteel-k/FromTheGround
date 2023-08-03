package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.blocks.DeepOreBlock;
import com.br1ghtsteel.ftground.blocks.OreBlock;
import com.br1ghtsteel.ftground.items.AcidMixtureItem;
import com.br1ghtsteel.ftground.items.BitumenFuelItem;
import com.br1ghtsteel.ftground.items.FlareArrowItem;
import com.br1ghtsteel.ftground.items.FlareItem;
import com.br1ghtsteel.ftground.items.FuelItem;
import com.br1ghtsteel.ftground.items.HammerItem;
import com.br1ghtsteel.ftground.items.MixtureItem;
import com.br1ghtsteel.ftground.items.StoneAxeItem;
import com.br1ghtsteel.ftground.items.StoneHoeItem;
import com.br1ghtsteel.ftground.items.StonePickaxeItem;
import com.br1ghtsteel.ftground.items.StoneShovelItem;
import com.br1ghtsteel.ftground.items.StoneSwordItem;
import com.br1ghtsteel.ftground.items.StoneVariantBlockItem;
import com.br1ghtsteel.ftground.items.SulfuricAcidItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.jetbrains.annotations.Nullable;

public class ItemsInit
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FromTheGround.MOD_ID);

    public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SALTPETER = ITEMS.register("saltpeter", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> BITUMEN = ITEMS.register("bitumen", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> BITUMINOUS_COAL = ITEMS.register("bituminous_coal", () -> new FuelItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), 3200));
	public static final RegistryObject<Item> BITUMEN_TAR = ITEMS.register("bitumen_tar", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> CHARCOAL_DUST = ITEMS.register("charcoal_dust", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> BAT_GUANO = ITEMS.register("bat_guano", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> CLAY_BRICK = ITEMS.register("clay_brick", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> LIMESTONE_POWDER = ITEMS.register("limestone_powder", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> AMMONIUM_SALTPETER = ITEMS.register("ammonium_saltpeter", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> ANFO = ITEMS.register("anfo", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SULFURIC_ACID = ITEMS.register("sulfuric_acid", () -> new SulfuricAcidItem(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> NITRIC_ACID = ITEMS.register("nitric_acid", () -> new AcidMixtureItem(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> SPIRIT_OF_AMMONIUM = ITEMS.register("spirit_of_ammonium", () -> new MixtureItem(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> NITROCHALK_FERTILIZER = ITEMS.register("nitrochalk_fertilizer", () -> new MixtureItem(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> BITUMEN_FUEL = ITEMS.register("bitumen_fuel", () -> new BitumenFuelItem(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> ACID_BUCKET = ITEMS.register("acid_bucket", () -> new BucketItem(FluidsInit.ACID_SOURCE.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).craftRemainder(Items.BUCKET)));
	public static final RegistryObject<Item> MOLTEN_SALTPETER_BUCKET = ITEMS.register("molten_saltpeter_bucket", () -> new BucketItem(FluidsInit.MOLTEN_SALTPETER_SOURCE.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).craftRemainder(Items.BUCKET)));
	public static final RegistryObject<Item> MONSTROUS_HIDE = ITEMS.register("monstrous_hide", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	
	// TOOLS
	public static final RegistryObject<StoneSwordItem> STONE_SWORD = ITEMS.register("stone_sword",
			() -> new StoneSwordItem(Tiers.STONE, 3, -2.4f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<StoneShovelItem> STONE_SHOVEL = ITEMS.register("stone_shovel",
			() -> new StoneShovelItem(Tiers.STONE, 1.5f, -3.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	
	public static final RegistryObject<StonePickaxeItem> STONE_PICKAXE = ITEMS.register("stone_pickaxe",
			() -> new StonePickaxeItem(Tiers.STONE, 1, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

	public static final RegistryObject<StoneAxeItem> STONE_AXE = ITEMS.register("stone_axe",
			() -> new StoneAxeItem(Tiers.STONE, 7, -3.2f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

	public static final RegistryObject<StoneHoeItem> STONE_HOE = ITEMS.register("stone_hoe",
			() -> new StoneHoeItem(Tiers.STONE, -1, -2.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

	public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", () -> new HammerItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(238)));
	
	public static final RegistryObject<Item> FLARE = ITEMS.register("flare", () -> new FlareItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final RegistryObject<Item> FLARE_ARROW = ITEMS.register("flare_arrow", () -> new FlareArrowItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	
    // BLOCKITEMS
	public static final RegistryObject<Item> ANDESITE = ITEMS.register("andesite", () -> new BlockItem(BlocksInit.ANDESITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_ANDESITE = ITEMS.register("cobbled_andesite", () -> new BlockItem(BlocksInit.COBBLED_ANDESITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_ANDESITE = ITEMS.register("polished_andesite", () -> new BlockItem(BlocksInit.POLISHED_ANDESITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> ANDESITE_BRICKS = ITEMS.register("andesite_bricks", () -> new BlockItem(BlocksInit.ANDESITE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> ANDESITE_GRAVEL = ITEMS.register("andesite_gravel", () -> new BlockItem(BlocksInit.ANDESITE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> DIORITE = ITEMS.register("diorite", () -> new BlockItem(BlocksInit.DIORITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_DIORITE = ITEMS.register("cobbled_diorite", () -> new BlockItem(BlocksInit.COBBLED_DIORITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_DIORITE = ITEMS.register("polished_diorite", () -> new BlockItem(BlocksInit.POLISHED_DIORITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> DIORITE_BRICKS = ITEMS.register("diorite_bricks", () -> new BlockItem(BlocksInit.DIORITE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> DIORITE_GRAVEL = ITEMS.register("diorite_gravel", () -> new BlockItem(BlocksInit.DIORITE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> GRANITE = ITEMS.register("granite", () -> new BlockItem(BlocksInit.GRANITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_GRANITE = ITEMS.register("cobbled_granite", () -> new BlockItem(BlocksInit.COBBLED_GRANITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_GRANITE = ITEMS.register("polished_granite", () -> new BlockItem(BlocksInit.POLISHED_GRANITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GRANITE_BRICKS = ITEMS.register("granite_bricks", () -> new BlockItem(BlocksInit.GRANITE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GRANITE_GRAVEL = ITEMS.register("granite_gravel", () -> new BlockItem(BlocksInit.GRANITE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> DOLERITE = ITEMS.register("dolerite", () -> new BlockItem(BlocksInit.DOLERITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_DOLERITE = ITEMS.register("cobbled_dolerite", () -> new BlockItem(BlocksInit.COBBLED_DOLERITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_DOLERITE = ITEMS.register("polished_dolerite", () -> new BlockItem(BlocksInit.POLISHED_DOLERITE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> DOLERITE_BRICKS = ITEMS.register("dolerite_bricks", () -> new BlockItem(BlocksInit.DOLERITE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> DOLERITE_GRAVEL = ITEMS.register("dolerite_gravel", () -> new BlockItem(BlocksInit.DOLERITE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> GNEISS = ITEMS.register("gneiss", () -> new BlockItem(BlocksInit.GNEISS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_GNEISS = ITEMS.register("cobbled_gneiss", () -> new BlockItem(BlocksInit.COBBLED_GNEISS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_GNEISS = ITEMS.register("polished_gneiss", () -> new BlockItem(BlocksInit.POLISHED_GNEISS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GNEISS_BRICKS = ITEMS.register("gneiss_bricks", () -> new BlockItem(BlocksInit.GNEISS_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GNEISS_GRAVEL = ITEMS.register("gneiss_gravel", () -> new BlockItem(BlocksInit.GNEISS_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> LAMPROPHYRE = ITEMS.register("lamprophyre", () -> new BlockItem(BlocksInit.LAMPROPHYRE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_LAMPROPHYRE = ITEMS.register("cobbled_lamprophyre", () -> new BlockItem(BlocksInit.COBBLED_LAMPROPHYRE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_LAMPROPHYRE = ITEMS.register("polished_lamprophyre", () -> new BlockItem(BlocksInit.POLISHED_LAMPROPHYRE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LAMPROPHYRE_BRICKS = ITEMS.register("lamprophyre_bricks", () -> new BlockItem(BlocksInit.LAMPROPHYRE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LAMPROPHYRE_GRAVEL = ITEMS.register("lamprophyre_gravel", () -> new BlockItem(BlocksInit.LAMPROPHYRE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	
	public static final RegistryObject<Item> LIMESTONE = ITEMS.register("limestone", () -> new BlockItem(BlocksInit.LIMESTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_LIMESTONE = ITEMS.register("cobbled_limestone", () -> new BlockItem(BlocksInit.COBBLED_LIMESTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_LIMESTONE = ITEMS.register("polished_limestone", () -> new BlockItem(BlocksInit.POLISHED_LIMESTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LIMESTONE_BRICKS = ITEMS.register("limestone_bricks", () -> new BlockItem(BlocksInit.LIMESTONE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LIMESTONE_GRAVEL = ITEMS.register("limestone_gravel", () -> new BlockItem(BlocksInit.LIMESTONE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> SCHIST = ITEMS.register("schist", () -> new BlockItem(BlocksInit.SCHIST.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_SCHIST = ITEMS.register("cobbled_schist", () -> new BlockItem(BlocksInit.COBBLED_SCHIST.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_SCHIST = ITEMS.register("polished_schist", () -> new BlockItem(BlocksInit.POLISHED_SCHIST.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SCHIST_BRICKS = ITEMS.register("schist_bricks", () -> new BlockItem(BlocksInit.SCHIST_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SCHIST_GRAVEL = ITEMS.register("schist_gravel", () -> new BlockItem(BlocksInit.SCHIST_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	
	public static final RegistryObject<Item> SHALE = ITEMS.register("shale", () -> new BlockItem(BlocksInit.SHALE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> COBBLED_SHALE = ITEMS.register("cobbled_shale", () -> new BlockItem(BlocksInit.COBBLED_SHALE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> POLISHED_SHALE = ITEMS.register("polished_shale", () -> new BlockItem(BlocksInit.POLISHED_SHALE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SHALE_BRICKS = ITEMS.register("shale_bricks", () -> new BlockItem(BlocksInit.SHALE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SHALE_GRAVEL = ITEMS.register("shale_gravel", () -> new BlockItem(BlocksInit.SHALE_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	
	public static final RegistryObject<Item> COAL_ORE = ITEMS.register("coal_ore", () -> new StoneVariantBlockItem(BlocksInit.COAL_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_COAL_ORE = ITEMS.register("deep_coal_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_COAL_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> IRON_ORE = ITEMS.register("iron_ore", () -> new StoneVariantBlockItem(BlocksInit.IRON_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_IRON_ORE = ITEMS.register("deep_iron_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_IRON_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> COPPER_ORE = ITEMS.register("copper_ore", () -> new StoneVariantBlockItem(BlocksInit.COPPER_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_COPPER_ORE = ITEMS.register("deep_copper_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_COPPER_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> GOLD_ORE = ITEMS.register("gold_ore", () -> new StoneVariantBlockItem(BlocksInit.GOLD_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_GOLD_ORE = ITEMS.register("deep_gold_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_GOLD_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DIAMOND_ORE = ITEMS.register("diamond_ore", () -> new StoneVariantBlockItem(BlocksInit.DIAMOND_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_DIAMOND_ORE = ITEMS.register("deep_diamond_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_DIAMOND_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> LAPIS_ORE = ITEMS.register("lapis_ore", () -> new StoneVariantBlockItem(BlocksInit.LAPIS_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_LAPIS_ORE = ITEMS.register("deep_lapis_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_LAPIS_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> REDSTONE_ORE = ITEMS.register("redstone_ore", () -> new StoneVariantBlockItem(BlocksInit.REDSTONE_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_REDSTONE_ORE = ITEMS.register("deep_redstone_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_REDSTONE_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> EMERALD_ORE = ITEMS.register("emerald_ore", () -> new StoneVariantBlockItem(BlocksInit.EMERALD_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), OreBlock.VALID_ROCK_TYPES));
	public static final RegistryObject<Item> DEEP_EMERALD_ORE = ITEMS.register("deep_emerald_ore", () -> new StoneVariantBlockItem(BlocksInit.DEEP_EMERALD_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), DeepOreBlock.VALID_ROCK_TYPES));
	
	public static final RegistryObject<Item> CLAY_BRICKS = ITEMS.register("clay_bricks", () -> new BlockItem(BlocksInit.CLAY_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> WHITE_BRICKS = ITEMS.register("white_bricks", () -> new BlockItem(BlocksInit.WHITE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> ORANGE_BRICKS = ITEMS.register("orange_bricks", () -> new BlockItem(BlocksInit.ORANGE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> MAGENTA_BRICKS = ITEMS.register("magenta_bricks", () -> new BlockItem(BlocksInit.MAGENTA_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LIGHT_BLUE_BRICKS = ITEMS.register("light_blue_bricks", () -> new BlockItem(BlocksInit.LIGHT_BLUE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> YELLOW_BRICKS = ITEMS.register("yellow_bricks", () -> new BlockItem(BlocksInit.YELLOW_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LIME_BRICKS = ITEMS.register("lime_bricks", () -> new BlockItem(BlocksInit.LIME_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> PINK_BRICKS = ITEMS.register("pink_bricks", () -> new BlockItem(BlocksInit.PINK_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GRAY_BRICKS = ITEMS.register("gray_bricks", () -> new BlockItem(BlocksInit.GRAY_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> LIGHT_GRAY_BRICKS = ITEMS.register("light_gray_bricks", () -> new BlockItem(BlocksInit.LIGHT_GRAY_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> CYAN_BRICKS = ITEMS.register("cyan_bricks", () -> new BlockItem(BlocksInit.CYAN_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> PURPLE_BRICKS = ITEMS.register("purple_bricks", () -> new BlockItem(BlocksInit.PURPLE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> BLUE_BRICKS = ITEMS.register("blue_bricks", () -> new BlockItem(BlocksInit.BLUE_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GREEN_BRICKS = ITEMS.register("green_bricks", () -> new BlockItem(BlocksInit.GREEN_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> BROWN_BRICKS = ITEMS.register("brown_bricks", () -> new BlockItem(BlocksInit.BROWN_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> RED_BRICKS = ITEMS.register("red_bricks", () -> new BlockItem(BlocksInit.RED_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> BLACK_BRICKS = ITEMS.register("black_bricks", () -> new BlockItem(BlocksInit.BLACK_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	
	public static final RegistryObject<Item> SULFUR_BLOCK = ITEMS.register("sulfur_block", () -> new BlockItem(BlocksInit.SULFUR_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SULFUR_ORE = ITEMS.register("sulfur_ore", () -> new BlockItem(BlocksInit.SULFUR_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SALTPETER_BLOCK = ITEMS.register("saltpeter_block", () -> new BlockItem(BlocksInit.SALTPETER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SALTPETER_ORE = ITEMS.register("saltpeter_ore", () -> new BlockItem(BlocksInit.SALTPETER_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> BITUMEN_ORE = ITEMS.register("bitumen_ore", () -> new BlockItem(BlocksInit.BITUMEN_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> BITUMINOUS_COAL_BLOCK = ITEMS.register("bituminous_coal_block", () -> new BlockItem(BlocksInit.BITUMINOUS_COAL_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)) {
		@Override
		public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType)
		{
			return 32000;
		}
	});
	public static final RegistryObject<Item> BRIMSTONE = ITEMS.register("brimstone", () -> new BlockItem(BlocksInit.BRIMSTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> DECAYING_LEAVES = ITEMS.register("decaying_leaves", () -> new BlockItem(BlocksInit.DECAYING_LEAVES.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> DECAYING_GRASS_BLOCK = ITEMS.register("decaying_grass_block", () -> new BlockItem(BlocksInit.DECAYING_GRASS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> DEAD_FLOWER = ITEMS.register("dead_flower", () -> new BlockItem(BlocksInit.DEAD_FLOWER.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> DEAD_CROP = ITEMS.register("dead_crop", () -> new BlockItem(BlocksInit.DEAD_CROP.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> SULFUR_LANTERN = ITEMS.register("sulfur_lantern", () -> new BlockItem(BlocksInit.SULFUR_LANTERN.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> SULFUR_TORCH = ITEMS.register("sulfur_torch", () -> new StandingAndWallBlockItem(BlocksInit.SULFUR_TORCH.get(), BlocksInit.SULFUR_WALL_TORCH.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> STRENGTHENED_GLASS = ITEMS.register("strengthened_glass", () -> new BlockItem(BlocksInit.STRENGTHENED_GLASS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> KILN = ITEMS.register("kiln", () -> new BlockItem(BlocksInit.KILN.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> BAT_BOX = ITEMS.register("bat_box", () -> new BlockItem(BlocksInit.BAT_BOX.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> FERTILIZER_BOX = ITEMS.register("fertilizer_box", () -> new BlockItem(BlocksInit.FERTILIZER_BOX.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> DESSICATED_FARMLAND = ITEMS.register("dessicated_farmland", () -> new BlockItem(BlocksInit.DESSICATED_FARMLAND.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> MIXING_STAND = ITEMS.register("mixing_stand", () -> new BlockItem(BlocksInit.MIXING_STAND.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Item> IRREGULATOR = ITEMS.register("irregulator", () -> new BlockItem(BlocksInit.IRREGULATOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	public static final RegistryObject<Item> ANFO_CHARGE = ITEMS.register("anfo_charge", () -> new BlockItem(BlocksInit.ANFO_CHARGE.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	public static final RegistryObject<Item> AMMONIUM_SALTPETER_BLOCK = ITEMS.register("ammonium_saltpeter_block", () -> new BlockItem(BlocksInit.AMMONIUM_SALTPETER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> ANFO_BLOCK = ITEMS.register("anfo_block", () -> new BlockItem(BlocksInit.ANFO_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> AMATOL = ITEMS.register("amatol", () -> new BlockItem(BlocksInit.AMATOL.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	public static final RegistryObject<Item> BLASTING_CAP = ITEMS.register("blasting_cap", () -> new BlockItem(BlocksInit.BLASTING_CAP.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	public static final RegistryObject<Item> STABLE_DETONATOR = ITEMS.register("stable_detonator", () -> new BlockItem(BlocksInit.STABLE_DETONATOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
	
	public static void registerDispenserBehaviour() {
		DispenseItemBehavior bucketDispenseBehaviour = new DefaultDispenseItemBehavior() {
	         private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

	         public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
	            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem)itemStack.getItem();
	            BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
	            Level level = blockSource.getLevel();
	            if (dispensibleContainerItem.emptyContents((Player)null, level, blockpos, (BlockHitResult)null)) {
	               dispensibleContainerItem.checkExtraContent((Player)null, level, itemStack, blockpos);
	               return new ItemStack(Items.BUCKET);
	            } else {
	               return this.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
	            }
	         }
	      };
		DispenserBlock.registerBehavior(MOLTEN_SALTPETER_BUCKET.get(), bucketDispenseBehaviour);
	}

}
