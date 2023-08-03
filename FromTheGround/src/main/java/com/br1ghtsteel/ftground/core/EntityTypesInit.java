package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.entity_types.FlareArrow;
import com.br1ghtsteel.ftground.entity_types.Bat;
import com.br1ghtsteel.ftground.entity_types.ExplodingBlock;
import com.br1ghtsteel.ftground.entity_types.ThrownFlare;
import com.br1ghtsteel.ftground.entity_types.ThrownMixture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypesInit
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FromTheGround.MOD_ID);
	
	public static final RegistryObject<EntityType<ThrownFlare>> THROWN_FLARE = ENTITY_TYPES.register("thrown_flare", 
			() -> EntityType.Builder.<ThrownFlare>of(ThrownFlare::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.clientTrackingRange(4)
					.updateInterval(20)
					.build(new ResourceLocation(FromTheGround.MOD_ID, "thrown_flare").toString()));
	
	public static final RegistryObject<EntityType<FlareArrow>> FLARE_ARROW = ENTITY_TYPES.register("flare_arrow", 
			() -> EntityType.Builder.<FlareArrow>of(FlareArrow::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.clientTrackingRange(4)
					.updateInterval(20)
					.build(new ResourceLocation(FromTheGround.MOD_ID, "flare_arrow").toString()));

	public static final RegistryObject<EntityType<ThrownMixture>> THROWN_MIXTURE = ENTITY_TYPES.register("thrown_mixture", 
			() -> EntityType.Builder.<ThrownMixture>of(ThrownMixture::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.clientTrackingRange(4)
					.updateInterval(20)
					.build(new ResourceLocation(FromTheGround.MOD_ID, "thrown_mixture").toString()));
	
	public static final RegistryObject<EntityType<ExplodingBlock>> EXPLODING_BLOCK = ENTITY_TYPES.register("exploding_block", 
			() -> EntityType.Builder.<ExplodingBlock>of(ExplodingBlock::new, MobCategory.MISC)
					.fireImmune()
					.sized(0.98F, 0.98F)
					.clientTrackingRange(10)
					.updateInterval(10)
					.build(new ResourceLocation(FromTheGround.MOD_ID, "exploding_block").toString()));
	
	public static final RegistryObject<EntityType<Bat>> BAT = ENTITY_TYPES.register("bat", 
			() -> EntityType.Builder.<Bat>of(Bat::new, MobCategory.CREATURE)
					.sized(0.5F, 0.9F)
					.clientTrackingRange(8)
					.build(new ResourceLocation(FromTheGround.MOD_ID, "bat").toString()));
}
