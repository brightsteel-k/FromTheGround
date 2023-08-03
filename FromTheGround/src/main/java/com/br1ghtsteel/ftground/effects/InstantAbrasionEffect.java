package com.br1ghtsteel.ftground.effects;

import java.util.Iterator;

import com.br1ghtsteel.ftground.tags.FTGItemTags;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InstantAbrasionEffect extends InstantenousMobEffect {

	public InstantAbrasionEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
		Iterator<ItemStack> armorItems = livingEntity.getArmorSlots().iterator();
		if (!armorItems.hasNext()) {
			return;
		}
		ItemStack armorPiece = ItemStack.EMPTY;
		for (int i = 0; i < 4; i++) {
			if (armorItems.hasNext()) {
				ItemStack newArmorPiece = armorItems.next();
				if (!newArmorPiece.isEmpty()) {
					armorPiece = newArmorPiece;
				}
			}
			damageArmorPiece(armorPiece, EquipmentSlot.values()[i+2], livingEntity, amplifier);
		}
	}
	
	private void damageArmorPiece(ItemStack armorPiece, EquipmentSlot equipmentSlot, LivingEntity livingEntity, int amplifier) {
		if (armorPiece.is(FTGItemTags.CORROSION_RESISTANT)) {
			return;
		}
		// Damage by 6 * 2^amplifier
		armorPiece.hurtAndBreak(6 << amplifier, livingEntity, (entity) -> {
			entity.broadcastBreakEvent(equipmentSlot);
        });
	}
}
