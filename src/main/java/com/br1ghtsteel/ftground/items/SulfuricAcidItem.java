package com.br1ghtsteel.ftground.items;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SulfuricAcidItem extends AcidMixtureItem {

	public SulfuricAcidItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public String getDescriptionId(ItemStack itemstack) {
		return this.getDescriptionId() + ".concentration." + getConcentration(itemstack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < 5; i++) {
			if (tab == CreativeModeTab.TAB_BREWING || tab == CreativeModeTab.TAB_SEARCH) {
				items.add(getItemByConcentration(i));
			}
		}
	}

	public static int getConcentration(ItemStack itemstack) {
		return getConcentration(itemstack.getTag());
	}

	public static int getConcentration(@Nullable CompoundTag compoundtag) {
		return compoundtag == null || !compoundtag.contains("Concentration")? 0 : Math.max(Math.min(compoundtag.getInt("Concentration"), 4), 0);
	}
	
	public static ItemStack getItemByConcentration(int concentration) {
		ItemStack item = new ItemStack(ItemsInit.SULFURIC_ACID.get());
		CompoundTag tag = item.getOrCreateTag();
		tag.putInt("Concentration", concentration);
		return item;
	}
}
