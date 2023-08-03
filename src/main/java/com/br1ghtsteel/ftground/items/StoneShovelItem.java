package com.br1ghtsteel.ftground.items;

import java.util.List;

import com.br1ghtsteel.ftground.util.StoneVariantsUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class StoneShovelItem extends ShovelItem {

	public StoneShovelItem(Tier tier, float damage, float speed, Properties properties) {
		super(tier, damage, speed, properties);
	}
	
	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		StoneVariantsUtil.appendRockTypeText(itemstack, tooltip);
		super.appendHoverText(itemstack, level, tooltip, tooltipFlag);
	}
}
