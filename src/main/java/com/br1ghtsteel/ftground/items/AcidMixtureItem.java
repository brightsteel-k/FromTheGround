package com.br1ghtsteel.ftground.items;

import java.util.List;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.util.MixingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class AcidMixtureItem extends MixtureItem {

	public AcidMixtureItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack itemstack, @Nullable Level level, List<Component> tooltip,
			TooltipFlag tooltipFlag) {
		MixingUtil.addPotionTooltip(itemstack, tooltip, 1.0F);
	}
}
