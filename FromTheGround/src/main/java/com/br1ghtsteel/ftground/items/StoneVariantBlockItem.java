package com.br1ghtsteel.ftground.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.br1ghtsteel.ftground.util.StoneVariantsUtil;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class StoneVariantBlockItem extends BlockItem {

	private final int[] validRockTypes;

	public StoneVariantBlockItem(Block block, Item.Properties properties, int[] rockTypes) {
		super(block, properties);
		this.validRockTypes = rockTypes;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (tab == CreativeModeTab.TAB_BUILDING_BLOCKS || tab == CreativeModeTab.TAB_SEARCH) {
			for (int i = 0; i < validRockTypes.length; i++) {
				ItemStack item = new ItemStack(this);
				CompoundTag tag = item.getOrCreateTag();
				tag.putInt("RockType", validRockTypes[i]);
				item.setTag(tag);
				items.add(item);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		StoneVariantsUtil.appendRockTypeText(itemstack, tooltip, this.validRockTypes);
		super.appendHoverText(itemstack, level, tooltip, tooltipFlag);
	}
}
