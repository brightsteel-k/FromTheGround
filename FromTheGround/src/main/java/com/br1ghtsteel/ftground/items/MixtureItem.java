package com.br1ghtsteel.ftground.items;

import com.br1ghtsteel.ftground.entity_types.ThrownMixture;

import net.minecraft.Util;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class MixtureItem extends Item {
	
	public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOUR = new DispenseItemBehavior() {
		public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
			return (new AbstractProjectileDispenseBehavior() {
				protected Projectile getProjectile(Level level, Position pos, ItemStack projectileItem) {
					return Util.make(new ThrownMixture(level, pos.x(), pos.y(), pos.z()), (projectile) -> {
						projectile.setItem(projectileItem);
					});
				}

				protected float getUncertainty() {
					return super.getUncertainty() * 0.5F;
				}

				protected float getPower() {
					return super.getPower() * 1.25F;
				}
			}).dispense(blockSource, itemStack);
		}
	};
	
	public MixtureItem(Item.Properties properties) {
		super(properties);
		DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOUR);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW,
				SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		ItemStack itemstack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			ThrownMixture thrownmixture = new ThrownMixture(level, player);
			thrownmixture.setItem(itemstack);
			thrownmixture.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
			level.addFreshEntity(thrownmixture);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}
}
