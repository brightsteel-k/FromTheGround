package com.br1ghtsteel.ftground.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class FuelItem extends Item
{
	private int burnTime;
	
	public FuelItem(Properties p_41383_, int burnTimeIn)
	{
		super(p_41383_);
		burnTime = burnTimeIn;
	}
	
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType)
	{
		return burnTime;
	}
}
