package com.br1ghtsteel.ftground.items;

import com.br1ghtsteel.ftground.entity_types.ThrownFlare;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlareItem extends Item
{
	public FlareItem(Item.Properties flare)
	{
		super(flare);
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
	    if (!level.isClientSide)
	    {
	    	ThrownFlare thrownflare = new ThrownFlare(level, player);
	    	thrownflare.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 1.0F);
	        level.addFreshEntity(thrownflare);
	    }

	    player.awardStat(Stats.ITEM_USED.get(this));
	    if (!player.getAbilities().instabuild)
	    	itemstack.shrink(1);

	    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}
}