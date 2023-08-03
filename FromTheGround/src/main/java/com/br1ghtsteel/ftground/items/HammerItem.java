package com.br1ghtsteel.ftground.items;

import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends Item
{
	public HammerItem(Item.Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level levelIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
        if (!levelIn.isClientSide && !state.is(BlockTags.FIRE))
        {
           stack.hurtAndBreak(1, entityLiving, (e) -> {
              e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
           });
        }
        
        return !state.is(FTGBlockTags.HAMMERABLE) && !state.is(FTGBlockTags.GLASS) ? super.mineBlock(stack, levelIn, state, pos, entityLiving) : true;
    }
	
	@Override
	public boolean isCorrectToolForDrops(BlockState state)
	{
	   return state.is(FTGBlockTags.HAMMERABLE) || state.is(FTGBlockTags.GLASS);
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
	   if (!state.is(FTGBlockTags.GLASS))
	   {
	      return state.is(FTGBlockTags.HAMMERABLE) ? 5.5F : super.getDestroySpeed(stack, state);
	   }
	   else
	   {
	      return 15.0F;
	   }
	}
}