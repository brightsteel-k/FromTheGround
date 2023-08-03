package com.br1ghtsteel.ftground.client.screens;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.client.screens.recipebook.BakingRecipeBookComponent;
import com.br1ghtsteel.ftground.menus.KilnMenu;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<KilnMenu>{
	private static final ResourceLocation TEXTURE = new ResourceLocation(FromTheGround.MOD_ID, "textures/gui/container/kiln.png");

	public KilnScreen(KilnMenu menu, Inventory inventory, Component component) {
	   super(menu, new BakingRecipeBookComponent(), inventory, component, TEXTURE);
	}
}
