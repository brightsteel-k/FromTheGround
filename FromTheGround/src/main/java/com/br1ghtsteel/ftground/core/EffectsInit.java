package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.effects.CorrosionEffect;
import com.br1ghtsteel.ftground.effects.InstantAbrasionEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectsInit {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FromTheGround.MOD_ID);
	
	public static final RegistryObject<MobEffect> CORROSION = EFFECTS.register("corrosion", () -> new CorrosionEffect(MobEffectCategory.HARMFUL, 8016416));
	public static final RegistryObject<MobEffect> INSTANT_ABRASION = EFFECTS.register("instant_abrasion", () -> new InstantAbrasionEffect(MobEffectCategory.HARMFUL, 4529940));
}
