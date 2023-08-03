package com.br1ghtsteel.ftground.items;

import com.br1ghtsteel.ftground.entity_types.FlareArrow;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlareArrowItem extends ArrowItem {
	   public FlareArrowItem(Item.Properties properties) {
	      super(properties);
	   }

	   public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity livingEntity) {
	      return new FlareArrow(level, livingEntity);
	   }
	}
