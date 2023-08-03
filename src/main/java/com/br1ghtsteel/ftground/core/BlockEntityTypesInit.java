package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.block_entities.BatBoxBlockEntity;
import com.br1ghtsteel.ftground.block_entities.KilnBlockEntity;
import com.br1ghtsteel.ftground.block_entities.MixingStandBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypesInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FromTheGround.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<KilnBlockEntity>> KILN = BLOCK_ENTITY_TYPES.register("kiln", () -> BlockEntityType.Builder.of(KilnBlockEntity::new, BlocksInit.KILN.get()).build(null));
	public static final RegistryObject<BlockEntityType<MixingStandBlockEntity>> MIXING_STAND = BLOCK_ENTITY_TYPES.register("mixing_stand", () -> BlockEntityType.Builder.of(MixingStandBlockEntity::new, BlocksInit.MIXING_STAND.get()).build(null));
	public static final RegistryObject<BlockEntityType<BatBoxBlockEntity>> BAT_BOX = BLOCK_ENTITY_TYPES.register("bat_box", () -> BlockEntityType.Builder.of(BatBoxBlockEntity::new, BlocksInit.BAT_BOX.get()).build(null));
}
