package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SulfurOreBlock extends ResidualOreBlock
{
	private static Potion potion = new Potion("Sulfur",
								   				new MobEffectInstance[] { 
								   					new MobEffectInstance(MobEffect.byId(20), 300, 2, false, true)
											 });
	
	private final IntProvider xpRange = UniformInt.of(1, 4);
	
	public SulfurOreBlock()
	{
		super(RockType.SHALE);
	}
	
	@Override
	public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack itemStack, boolean b)
	{
		if (level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0 && level.random.nextFloat() > 0.6f)
		{
			this.popSulfurGasCloud(level, pos);
		}
		super.spawnAfterBreak(state, level, pos, itemStack, b);
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader level, net.minecraft.util.RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
	   return silkTouchLevel == 0 ? this.xpRange.sample(randomSource) : 0;
	}
	
	
	public void popSulfurGasCloud(ServerLevel level, BlockPos pos)
	{
		AreaEffectCloud aoecloudentity = new AreaEffectCloud(level, pos.getX(), pos.getY() + 1, pos.getZ());
		aoecloudentity.setRadius(2.5F);
		aoecloudentity.setRadiusOnUse(-0.2F);
		aoecloudentity.setWaitTime(7);
		aoecloudentity.setRadiusPerTick(-aoecloudentity.getRadius() / (float)aoecloudentity.getDuration());
		aoecloudentity.setPotion(potion);
		aoecloudentity.setFixedColor(0x878E65);
		
		level.addFreshEntity(aoecloudentity);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level levelIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
	   if (!levelIn.isClientSide)
	   {
	      if (levelIn.getBlockState(pos.above()).is(BlockTags.FIRE))
	      {
	         levelIn.setBlockAndUpdate(pos.above(), BlocksInit.SULFUR_FIRE.get().defaultBlockState());
	      }
       }
	}
}
