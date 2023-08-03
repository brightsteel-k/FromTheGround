package com.br1ghtsteel.ftground.util;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StoneVariantsUtil {
	
	public static float getRockTypeProperty(ItemStack itemstack, @Nullable ClientLevel level, LivingEntity entity, int entityId) {
		return getRockType(itemstack);
	}
	
	public static void appendRockTypeText(ItemStack itemstack, List<Component> tooltip) {
		int rockType = getRockType(itemstack);
		tooltip.add(Component.translatable("rocktype.ftground." + RockType.byId(rockType).getName()).withStyle(ChatFormatting.DARK_GRAY));
	}
	
	public static void appendRockTypeText(ItemStack itemstack, List<Component> tooltip, int[] validRockTypes) {
		int rockType = getRockType(itemstack);
		rockType = getNearestValidInt(validRockTypes, rockType);
		tooltip.add(Component.translatable("rocktype.ftground." + RockType.byId(rockType).getName()).withStyle(ChatFormatting.DARK_GRAY));
	}
	
	public static int getRockType(ItemStack itemstack) {
		CompoundTag compoundtag = itemstack.getTag();
		return compoundtag != null && compoundtag.contains("RockType") ? compoundtag.getInt("RockType") : 0;
	}
	
	// REQUIRES: arr is a non-empty sorted ascending list
	public static int getNearestValidInt(int[] arr, int item) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == item) {
				return item;
			} else if (arr[i] > item) {
				return arr[Math.max(0, i - 1)];
			}
		}
		return arr[arr.length - 1];
	}
}
