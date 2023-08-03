package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.menus.KilnMenu;
import com.br1ghtsteel.ftground.menus.MixingStandMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypesInit {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FromTheGround.MOD_ID);
	
	public static final RegistryObject<MenuType<KilnMenu>> KILN = MENU_TYPES.register("kiln", () -> new MenuType<>(KilnMenu::new));
	public static final RegistryObject<MenuType<MixingStandMenu>> MIXING_STAND = MENU_TYPES.register("mixing_stand", () -> new MenuType<>(MixingStandMenu::new));
}
