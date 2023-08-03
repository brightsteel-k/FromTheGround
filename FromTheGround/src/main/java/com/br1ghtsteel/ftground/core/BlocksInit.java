package com.br1ghtsteel.ftground.core;

import java.util.function.ToIntFunction;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.blocks.BatBoxBlock;
import com.br1ghtsteel.ftground.blocks.BlastingCapBlock;
import com.br1ghtsteel.ftground.blocks.BrickBlock;
import com.br1ghtsteel.ftground.blocks.ChargeBlock;
import com.br1ghtsteel.ftground.blocks.CobbledStoneBlock;
import com.br1ghtsteel.ftground.blocks.DeadCropBlock;
import com.br1ghtsteel.ftground.blocks.DecayingBlock;
import com.br1ghtsteel.ftground.blocks.DeepOreBlock;
import com.br1ghtsteel.ftground.blocks.DeepRedstoneOreBlock;
import com.br1ghtsteel.ftground.blocks.DessicatedFarmBlock;
import com.br1ghtsteel.ftground.blocks.ExplosiveBlock;
import com.br1ghtsteel.ftground.blocks.FertilizerBoxBlock;
import com.br1ghtsteel.ftground.blocks.FlareBlock;
import com.br1ghtsteel.ftground.blocks.GravelBlock;
import com.br1ghtsteel.ftground.blocks.GroundFlareBlock;
import com.br1ghtsteel.ftground.blocks.IrregulatorBlock;
import com.br1ghtsteel.ftground.blocks.KilnBlock;
import com.br1ghtsteel.ftground.blocks.MixingStandBlock;
import com.br1ghtsteel.ftground.blocks.OreBlock;
import com.br1ghtsteel.ftground.blocks.RedstoneOreBlock;
import com.br1ghtsteel.ftground.blocks.ResidualOreBlock;
import com.br1ghtsteel.ftground.blocks.SaltpeterBlock;
import com.br1ghtsteel.ftground.blocks.StableDetonatorBlock;
import com.br1ghtsteel.ftground.blocks.StoneBlock;
import com.br1ghtsteel.ftground.blocks.SulfurFireBlock;
import com.br1ghtsteel.ftground.blocks.SulfurOreBlock;
import com.br1ghtsteel.ftground.blocks.SulfurTorchBlock;
import com.br1ghtsteel.ftground.blocks.SulfurWallTorchBlock;
import com.br1ghtsteel.ftground.blocks.SulfurousBlock;
import com.br1ghtsteel.ftground.blocks.WallFlareBlock;
import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlocksInit
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FromTheGround.MOD_ID);
	
	// MATERIALS - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public static final Material ON_FARMLAND = (new Material.Builder(MaterialColor.NONE)).nonSolid().build();
	
	// BLOCKS - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public static final RegistryObject<Block> ANDESITE = BLOCKS.register("andesite", () -> new StoneBlock(RockType.ANDESITE));
	public static final RegistryObject<Block> COBBLED_ANDESITE = BLOCKS.register("cobbled_andesite", () -> new CobbledStoneBlock(RockType.ANDESITE));
	public static final RegistryObject<Block> POLISHED_ANDESITE = BLOCKS.register("polished_andesite", () -> new StoneBlock(RockType.ANDESITE));
	public static final RegistryObject<Block> ANDESITE_BRICKS = BLOCKS.register("andesite_bricks", () -> new StoneBlock(RockType.ANDESITE));
	public static final RegistryObject<Block> ANDESITE_GRAVEL = BLOCKS.register("andesite_gravel", () -> new GravelBlock(RockType.ANDESITE));

	public static final RegistryObject<Block> DIORITE = BLOCKS.register("diorite", () -> new StoneBlock(RockType.DIORITE));
	public static final RegistryObject<Block> COBBLED_DIORITE = BLOCKS.register("cobbled_diorite", () -> new CobbledStoneBlock(RockType.DIORITE));
	public static final RegistryObject<Block> POLISHED_DIORITE = BLOCKS.register("polished_diorite", () -> new StoneBlock(RockType.DIORITE));
	public static final RegistryObject<Block> DIORITE_BRICKS = BLOCKS.register("diorite_bricks", () -> new StoneBlock(RockType.DIORITE));
	public static final RegistryObject<Block> DIORITE_GRAVEL = BLOCKS.register("diorite_gravel", () -> new GravelBlock(RockType.DIORITE));

	public static final RegistryObject<Block> GRANITE = BLOCKS.register("granite", () -> new StoneBlock(RockType.GRANITE));
	public static final RegistryObject<Block> COBBLED_GRANITE = BLOCKS.register("cobbled_granite", () -> new CobbledStoneBlock(RockType.GRANITE));
	public static final RegistryObject<Block> POLISHED_GRANITE = BLOCKS.register("polished_granite", () -> new StoneBlock(RockType.GRANITE));
	public static final RegistryObject<Block> GRANITE_BRICKS = BLOCKS.register("granite_bricks", () -> new StoneBlock(RockType.GRANITE));
	public static final RegistryObject<Block> GRANITE_GRAVEL = BLOCKS.register("granite_gravel", () -> new GravelBlock(RockType.GRANITE));

	public static final RegistryObject<Block> DOLERITE = BLOCKS.register("dolerite", () -> new StoneBlock(RockType.DOLERITE));
	public static final RegistryObject<Block> COBBLED_DOLERITE = BLOCKS.register("cobbled_dolerite", () -> new CobbledStoneBlock(RockType.DOLERITE));
	public static final RegistryObject<Block> POLISHED_DOLERITE = BLOCKS.register("polished_dolerite", () -> new StoneBlock(RockType.DOLERITE));
	public static final RegistryObject<Block> DOLERITE_BRICKS = BLOCKS.register("dolerite_bricks", () -> new StoneBlock(RockType.DOLERITE));
	public static final RegistryObject<Block> DOLERITE_GRAVEL = BLOCKS.register("dolerite_gravel", () -> new GravelBlock(RockType.DOLERITE));

	public static final RegistryObject<Block> GNEISS = BLOCKS.register("gneiss", () -> new StoneBlock(RockType.GNEISS));
	public static final RegistryObject<Block> COBBLED_GNEISS = BLOCKS.register("cobbled_gneiss", () -> new CobbledStoneBlock(RockType.GNEISS));
	public static final RegistryObject<Block> POLISHED_GNEISS = BLOCKS.register("polished_gneiss", () -> new StoneBlock(RockType.GNEISS));
	public static final RegistryObject<Block> GNEISS_BRICKS = BLOCKS.register("gneiss_bricks", () -> new StoneBlock(RockType.GNEISS));
	public static final RegistryObject<Block> GNEISS_GRAVEL = BLOCKS.register("gneiss_gravel", () -> new GravelBlock(RockType.GNEISS));
	
	public static final RegistryObject<Block> LAMPROPHYRE = BLOCKS.register("lamprophyre", () -> new StoneBlock(RockType.LAMPROPHYRE));
	public static final RegistryObject<Block> COBBLED_LAMPROPHYRE = BLOCKS.register("cobbled_lamprophyre", () -> new CobbledStoneBlock(RockType.LAMPROPHYRE));
	public static final RegistryObject<Block> POLISHED_LAMPROPHYRE = BLOCKS.register("polished_lamprophyre", () -> new StoneBlock(RockType.LAMPROPHYRE));
	public static final RegistryObject<Block> LAMPROPHYRE_BRICKS = BLOCKS.register("lamprophyre_bricks", () -> new StoneBlock(RockType.LAMPROPHYRE));
	public static final RegistryObject<Block> LAMPROPHYRE_GRAVEL = BLOCKS.register("lamprophyre_gravel", () -> new GravelBlock(RockType.LAMPROPHYRE));
	
	public static final RegistryObject<Block> LIMESTONE = BLOCKS.register("limestone", () -> new StoneBlock(RockType.LIMESTONE));
	public static final RegistryObject<Block> COBBLED_LIMESTONE = BLOCKS.register("cobbled_limestone", () -> new CobbledStoneBlock(RockType.LIMESTONE));
	public static final RegistryObject<Block> POLISHED_LIMESTONE = BLOCKS.register("polished_limestone", () -> new StoneBlock(RockType.LIMESTONE));
	public static final RegistryObject<Block> LIMESTONE_BRICKS = BLOCKS.register("limestone_bricks", () -> new StoneBlock(RockType.LIMESTONE));
	public static final RegistryObject<Block> LIMESTONE_GRAVEL = BLOCKS.register("limestone_gravel", () -> new GravelBlock(RockType.LIMESTONE));

	public static final RegistryObject<Block> SCHIST = BLOCKS.register("schist", () -> new StoneBlock(RockType.SCHIST));
	public static final RegistryObject<Block> COBBLED_SCHIST = BLOCKS.register("cobbled_schist", () -> new CobbledStoneBlock(RockType.SCHIST));
	public static final RegistryObject<Block> POLISHED_SCHIST = BLOCKS.register("polished_schist", () -> new StoneBlock(RockType.SCHIST));
	public static final RegistryObject<Block> SCHIST_BRICKS = BLOCKS.register("schist_bricks", () -> new StoneBlock(RockType.SCHIST));
	public static final RegistryObject<Block> SCHIST_GRAVEL = BLOCKS.register("schist_gravel", () -> new GravelBlock(RockType.SCHIST));
	
	public static final RegistryObject<Block> SHALE = BLOCKS.register("shale", () -> new StoneBlock(RockType.SHALE));
	public static final RegistryObject<Block> COBBLED_SHALE = BLOCKS.register("cobbled_shale", () -> new CobbledStoneBlock(RockType.SHALE));
	public static final RegistryObject<Block> POLISHED_SHALE = BLOCKS.register("polished_shale", () -> new StoneBlock(RockType.SHALE));
	public static final RegistryObject<Block> SHALE_BRICKS = BLOCKS.register("shale_bricks", () -> new StoneBlock(RockType.SHALE));
	public static final RegistryObject<Block> SHALE_GRAVEL = BLOCKS.register("shale_gravel", () -> new GravelBlock(RockType.SHALE));
	
	public static final RegistryObject<Block> COAL_ORE = BLOCKS.register("coal_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE).noOcclusion(), UniformInt.of(0, 2)));
	public static final RegistryObject<Block> DEEP_COAL_ORE = BLOCKS.register("deep_coal_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COAL_ORE), UniformInt.of(0, 2)));
	public static final RegistryObject<Block> IRON_ORE = BLOCKS.register("iron_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> DEEP_IRON_ORE = BLOCKS.register("deep_iron_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)));
	public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)));
	public static final RegistryObject<Block> DEEP_COPPER_ORE = BLOCKS.register("deep_copper_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COPPER_ORE)));
	public static final RegistryObject<Block> GOLD_ORE = BLOCKS.register("gold_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> DEEP_GOLD_ORE = BLOCKS.register("deep_gold_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_GOLD_ORE)));
	public static final RegistryObject<Block> DIAMOND_ORE = BLOCKS.register("diamond_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE), UniformInt.of(3, 7)));
	public static final RegistryObject<Block> DEEP_DIAMOND_ORE = BLOCKS.register("deep_diamond_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_DIAMOND_ORE), UniformInt.of(3, 7)));
	public static final RegistryObject<Block> LAPIS_ORE = BLOCKS.register("lapis_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE), UniformInt.of(2, 5)));
	public static final RegistryObject<Block> DEEP_LAPIS_ORE = BLOCKS.register("deep_lapis_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE), UniformInt.of(2, 5)));
	public static final RegistryObject<Block> REDSTONE_ORE = BLOCKS.register("redstone_ore", () -> new RedstoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE)));
	public static final RegistryObject<Block> DEEP_REDSTONE_ORE = BLOCKS.register("deep_redstone_ore", () -> new DeepRedstoneOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_REDSTONE_ORE)));
	public static final RegistryObject<Block> EMERALD_ORE = BLOCKS.register("emerald_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE), UniformInt.of(3, 7)));
	public static final RegistryObject<Block> DEEP_EMERALD_ORE = BLOCKS.register("deep_emerald_ore", () -> new DeepOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_EMERALD_ORE), UniformInt.of(3, 7)));
	
	
	public static final RegistryObject<Block> CLAY_BRICKS = BLOCKS.register("clay_bricks",
			() -> new BrickBlock(MaterialColor.SAND));
	public static final RegistryObject<Block> WHITE_BRICKS = BLOCKS.register("white_bricks",
			() -> new BrickBlock(DyeColor.WHITE));
	public static final RegistryObject<Block> ORANGE_BRICKS = BLOCKS.register("orange_bricks",
			() -> new BrickBlock(DyeColor.ORANGE));
	public static final RegistryObject<Block> MAGENTA_BRICKS = BLOCKS.register("magenta_bricks",
			() -> new BrickBlock(DyeColor.MAGENTA));
	public static final RegistryObject<Block> LIGHT_BLUE_BRICKS = BLOCKS.register("light_blue_bricks",
			() -> new BrickBlock(DyeColor.LIGHT_BLUE));
	public static final RegistryObject<Block> YELLOW_BRICKS = BLOCKS.register("yellow_bricks",
			() -> new BrickBlock(DyeColor.YELLOW));
	public static final RegistryObject<Block> LIME_BRICKS = BLOCKS.register("lime_bricks",
			() -> new BrickBlock(DyeColor.LIME));
	public static final RegistryObject<Block> PINK_BRICKS = BLOCKS.register("pink_bricks",
			() -> new BrickBlock(DyeColor.PINK));
	public static final RegistryObject<Block> GRAY_BRICKS = BLOCKS.register("gray_bricks",
			() -> new BrickBlock(DyeColor.GRAY));
	public static final RegistryObject<Block> LIGHT_GRAY_BRICKS = BLOCKS.register("light_gray_bricks",
			() -> new BrickBlock(DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Block> CYAN_BRICKS = BLOCKS.register("cyan_bricks",
			() -> new BrickBlock(DyeColor.CYAN));
	public static final RegistryObject<Block> PURPLE_BRICKS = BLOCKS.register("purple_bricks",
			() -> new BrickBlock(DyeColor.PURPLE));
	public static final RegistryObject<Block> BLUE_BRICKS = BLOCKS.register("blue_bricks",
			() -> new BrickBlock(DyeColor.BLUE));
	public static final RegistryObject<Block> BROWN_BRICKS = BLOCKS.register("brown_bricks",
			() -> new BrickBlock(DyeColor.BROWN));
	public static final RegistryObject<Block> GREEN_BRICKS = BLOCKS.register("green_bricks",
			() -> new BrickBlock(DyeColor.GREEN));
	public static final RegistryObject<Block> RED_BRICKS = BLOCKS.register("red_bricks",
			() -> new BrickBlock(DyeColor.RED));
	public static final RegistryObject<Block> BLACK_BRICKS = BLOCKS.register("black_bricks",
			() -> new BrickBlock(DyeColor.BLACK));
	
	public static final RegistryObject<Block> SULFUR_BLOCK = BLOCKS.register("sulfur_block",
			() -> new SulfurousBlock(4.0f, 4.0f));
	public static final RegistryObject<Block> SULFUR_ORE = BLOCKS.register("sulfur_ore", SulfurOreBlock::new);
	public static final RegistryObject<Block> SALTPETER_BLOCK = BLOCKS.register("saltpeter_block", () -> new SaltpeterBlock(BlockBehaviour.Properties.of(Material.SAND, DyeColor.WHITE).strength(0.5f, 1.0f).sound(SoundType.SAND).randomTicks()));
	public static final RegistryObject<Block> SALTPETER_ORE = BLOCKS.register("saltpeter_ore", () -> new ResidualOreBlock(RockType.LAMPROPHYRE));
	public static final RegistryObject<Block> BITUMEN_ORE = BLOCKS.register("bitumen_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f, 6.0f).sound(SoundType.STONE), UniformInt.of(1, 3)));
	public static final RegistryObject<Block> BITUMINOUS_COAL_BLOCK = BLOCKS.register("bituminous_coal_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).strength(5.0f, 6.0f).sound(SoundType.STONE)));
	public static final RegistryObject<Block> BRIMSTONE = BLOCKS.register("brimstone", () -> new SulfurousBlock(1.5f, 6.0f));
	public static final RegistryObject<Block> SULFUR_FIRE = BLOCKS.register("sulfur_fire", SulfurFireBlock::new);
	public static final RegistryObject<Block> DECAYING_LEAVES = BLOCKS.register("decaying_leaves", () -> new DecayingBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.05F).sound(SoundType.GRASS).noOcclusion().isValidSpawn(BlocksInit::never).isSuffocating(BlocksInit::never).isViewBlocking(BlocksInit::never), Blocks.AIR));
	public static final RegistryObject<Block> DECAYING_GRASS_BLOCK = BLOCKS.register("decaying_grass_block", () -> new DecayingBlock(BlockBehaviour.Properties.of(Material.GRASS).strength(0.6F).sound(SoundType.GRASS), Blocks.DIRT));
	public static final RegistryObject<Block> DEAD_FLOWER = BLOCKS.register("dead_flower", () -> new FlowerBlock(MobEffects.HUNGER, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final RegistryObject<Block> DEAD_CROP = BLOCKS.register("dead_crop", () -> new DeadCropBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.CROP)));
	public static final RegistryObject<Block> SULFUR_LANTERN = BLOCKS.register("sulfur_lantern", () -> new LanternBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5f).sound(SoundType.LANTERN).lightLevel(value -> {
		return 13;
	}).noOcclusion()));
	public static final RegistryObject<Block> SULFUR_TORCH = BLOCKS.register("sulfur_torch", () -> new SulfurTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_50876_) -> {
	      return 10;
	   }).sound(SoundType.WOOD), () -> ParticlesInit.SULFUR_FIRE_FLAME.get()));
	public static final RegistryObject<Block> SULFUR_WALL_TORCH = BLOCKS.register("sulfur_wall_torch", () -> new SulfurWallTorchBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
		return 11;
	}).sound(SoundType.WOOD).lootFrom(() -> SULFUR_TORCH.get()), () -> ParticlesInit.SULFUR_FIRE_FLAME.get()));
	
	public static final RegistryObject<Block> FLARE = BLOCKS.register("flare", () -> new FlareBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
		return 14;
	}).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WALL_FLARE = BLOCKS.register("wall_flare", () -> new WallFlareBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
		return 14;
	}).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GROUND_FLARE = BLOCKS.register("ground_flare", () -> new GroundFlareBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
		return 14;
	}).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> STRENGTHENED_GLASS = BLOCKS.register("strengthened_glass", () -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(3.0F, 500.0F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(BlocksInit::never).isRedstoneConductor(BlocksInit::never).isSuffocating(BlocksInit::never).isViewBlocking(BlocksInit::never)));
	public static final RegistryObject<LiquidBlock> ACID = BLOCKS.register("acid", () -> new LiquidBlock(FluidsInit.ACID_SOURCE, BlockBehaviour.Properties.of(Material.WATER).noCollission().randomTicks().strength(100.0F).noLootTable()));
	public static final RegistryObject<Block> KILN = BLOCKS.register("kiln", () -> new KilnBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5f).lightLevel(litBlockEmission(13))));
	public static final RegistryObject<Block> BAT_BOX = BLOCKS.register("bat_box", () -> new BatBoxBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(1.0f).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> DESSICATED_FARMLAND = BLOCKS.register("dessicated_farmland", () -> new DessicatedFarmBlock(BlockBehaviour.Properties.of(Material.DIRT).randomTicks().strength(0.6f).sound(SoundType.GRAVEL).isViewBlocking(BlocksInit::always).isSuffocating(BlocksInit::always)));
	public static final RegistryObject<Block> FERTILIZER_BOX = BLOCKS.register("fertilizer_box", () -> new FertilizerBoxBlock(BlockBehaviour.Properties.of(ON_FARMLAND).strength(0.6f).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> MIXING_STAND = BLOCKS.register("mixing_stand", () -> new MixingStandBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5f).noOcclusion()));
	public static final RegistryObject<Block> ANFO_CHARGE = BLOCKS.register("anfo_charge", () -> new ChargeBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).instabreak().sound(SoundType.GRASS), -4.0f, 80, false));
	public static final RegistryObject<Block> AMMONIUM_SALTPETER_BLOCK = BLOCKS.register("ammonium_saltpeter_block", () -> new ExplosiveBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).strength(1.0F).sound(SoundType.GRAVEL), false, 2.0f, 2));
	public static final RegistryObject<Block> ANFO_BLOCK = BLOCKS.register("anfo_block", () -> new ExplosiveBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).strength(1.0F).sound(SoundType.GRAVEL), true, -6.0f, 3));
	public static final RegistryObject<Block> AMATOL = BLOCKS.register("amatol", () -> new ChargeBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).strength(0.5F, 0.0F).sound(SoundType.GRASS), 30.0f, 200, true));
	public static final RegistryObject<Block> BLASTING_CAP = BLOCKS.register("blasting_cap", () -> new BlastingCapBlock(BlockBehaviour.Properties.of(Material.STONE).strength(25.0F, 3000.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> STABLE_DETONATOR = BLOCKS.register("stable_detonator", () -> new StableDetonatorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F, 0.0F)));
	public static final RegistryObject<LiquidBlock> MOLTEN_SALTPETER = BLOCKS.register("molten_saltpeter", () -> new LiquidBlock(FluidsInit.MOLTEN_SALTPETER_SOURCE, BlockBehaviour.Properties.of(Material.LAVA).noCollission().randomTicks().strength(100.0F).lightLevel((p_50755_) -> {
	      return 14;
	   }).noLootTable()));
	public static final RegistryObject<Block> IRREGULATOR = BLOCKS.register("irregulator", () -> new IrregulatorBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD)));
	
	public static boolean always(BlockState blockstate, BlockGetter getter, BlockPos blockpos) {
		return true;
	}
		
	public static boolean never(BlockState blockstate, BlockGetter getter, BlockPos blockpos) {
		return false;
	}
	
	public static boolean never(BlockState blockstate, BlockGetter getter, BlockPos blockpos, EntityType<?> entityType) {
		return false;
	}
	
	private static ToIntFunction<BlockState> litBlockEmission(int maxLightLevel) {
		return (blockstate) -> {
			return blockstate.getValue(BlockStateProperties.LIT) ? maxLightLevel : 0;
		};
	}
}
