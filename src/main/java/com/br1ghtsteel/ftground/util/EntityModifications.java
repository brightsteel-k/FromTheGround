package com.br1ghtsteel.ftground.util;

import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EntityModifications {
	private static ItemStack witherSkeletonSword;
	
	public static void init() {
		setupWitherSkeletonSword();
	}
	
	public static void equipWitherSkeleton(WitherSkeleton skeleton) {
		if (skeleton.getMainHandItem().is(Items.STONE_SWORD)) {
			skeleton.setItemSlot(EquipmentSlot.MAINHAND, witherSkeletonSword.copy());
		}
	}

	private static void setupWitherSkeletonSword() {
		witherSkeletonSword = new ItemStack(ItemsInit.STONE_SWORD.get());
		CompoundTag compoundtag = new CompoundTag();
		compoundtag.putInt("RockType", 2);
		witherSkeletonSword.setTag(compoundtag);
	}
}