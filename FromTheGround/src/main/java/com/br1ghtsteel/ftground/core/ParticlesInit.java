package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.particles.SulfurFlameParticle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FromTheGround.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ParticlesInit
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FromTheGround.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> SULFUR_FIRE_FLAME = PARTICLES.register("sulfur_fire_flame", () -> new SimpleParticleType(false));
	
	@SubscribeEvent
	public static void registerProviders(final RegisterParticleProvidersEvent event)
	{
		ParticleEngine particles = Minecraft.getInstance().particleEngine;
		particles.register(ParticlesInit.SULFUR_FIRE_FLAME.get(), SulfurFlameParticle.Provider::new);
	}
}