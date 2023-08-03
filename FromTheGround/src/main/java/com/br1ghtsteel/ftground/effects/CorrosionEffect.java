package com.br1ghtsteel.ftground.effects;

import java.util.Iterator;

import com.br1ghtsteel.ftground.tags.FTGItemTags;
import com.br1ghtsteel.ftground.util.MixingUtil;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CorrosionEffect extends MobEffect {

	public CorrosionEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ARMOR, "A0BB1C23-3435-7800-F4D4-182FC0440D16", -4D, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
		Iterator<ItemStack> armorItems = livingEntity.getArmorSlots().iterator();
		float entityDamage = 0;
		for (int i = 0; i < 4; i++) {
			if (armorItems.hasNext()) {
				ItemStack armorPiece = armorItems.next();
				if (!armorPiece.isEmpty()) {
					damageArmorPiece(armorPiece, EquipmentSlot.values()[i+2], livingEntity, amplifier);
					continue;
				}
			}
			entityDamage += 0.25F * (float)amplifier;
		}
		
		if (livingEntity.getHealth() == 1.0F) {
			return;
		}
		if (livingEntity.getHealth() - entityDamage < 1.0F) {
			livingEntity.hurt(MixingUtil.ACID_DAMAGE, livingEntity.getHealth() - 1.0F);
		} else {
			livingEntity.hurt(MixingUtil.ACID_DAMAGE, entityDamage);
		}
	}
	
	private void damageArmorPiece(ItemStack armorPiece, EquipmentSlot equipmentSlot, LivingEntity livingEntity, int amplifier) {
		if (armorPiece.is(FTGItemTags.CORROSION_RESISTANT)) {
			return;
		}
		// Damage by 1 * 2^amplifier
		armorPiece.hurtAndBreak(1 << amplifier, livingEntity, (entity) -> {
			entity.broadcastBreakEvent(equipmentSlot);
        });
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 20 == 0;
	}
}
