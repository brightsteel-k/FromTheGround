package com.br1ghtsteel.ftground.events;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.core.EntityTypesInit;

import net.minecraft.world.entity.ambient.Bat;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromTheGround.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {

	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent e) {
		e.put(EntityTypesInit.BAT.get(), Bat.createAttributes().build());
	}
}
