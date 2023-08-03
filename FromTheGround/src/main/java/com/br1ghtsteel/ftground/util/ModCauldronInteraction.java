package com.br1ghtsteel.ftground.util;

import java.util.Map;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.ItemsInit;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModCauldronInteraction {
	// TODO: REMOVE
	/*public static final Map<Item, CauldronInteraction> SALTPETER = newInteractionMap();
	public static final CauldronInteraction FILL_SALTPETER = (cauldron, level, blockpos, player, hand, itemstack) -> {
		return placeSaltpeter(level, blockpos, player, hand, itemstack,
				BlocksInit.SALTPETER_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, Integer.valueOf(3)),
				SoundEvents.GRAVEL_PLACE);
	};
	public static final CauldronInteraction EMPTY_SALTPETER = (cauldron, level, blockpos, player, hand, itemstack) -> {
		return emptySaltpeter(cauldron, level, blockpos, player, hand, itemstack,
				Blocks.CAULDRON.defaultBlockState(),
				SoundEvents.GRAVEL_BREAK);
	};

	public static void init() {
		CauldronInteraction.EMPTY.put(ItemsInit.SALTPETER_BLOCK.get(), FILL_SALTPETER);
		SALTPETER.put(Items.AIR, EMPTY_SALTPETER);
	}
	
	static InteractionResult placeSaltpeter(Level level, BlockPos blockpos, Player player, InteractionHand hand,
			ItemStack itemStack, BlockState blockstate, SoundEvent soundEvent) {
		if (!level.isClientSide) {
			itemStack.shrink(1);
			player.awardStat(Stats.FILL_CAULDRON);
			level.setBlockAndUpdate(blockpos, blockstate);
			level.playSound((Player) null, blockpos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockpos);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	static InteractionResult emptySaltpeter(BlockState cauldronState, Level level, BlockPos blockpos, Player player, InteractionHand hand,
			ItemStack itemStack, BlockState blockstate, SoundEvent soundEvent) {
		if (!level.isClientSide) {
			if (!(cauldronState.getBlock() instanceof SaltpeterCauldronBlock) || cauldronState.getValue(SaltpeterCauldronBlock.LEVEL) != 3) {
				return InteractionResult.FAIL;
			}
			player.setItemInHand(hand, new ItemStack(ItemsInit.SALTPETER_BLOCK.get()));
			level.setBlockAndUpdate(blockpos, blockstate);
			level.playSound((Player) null, blockpos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.gameEvent((Entity) null, GameEvent.FLUID_PICKUP, blockpos);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	static Object2ObjectOpenHashMap<Item, CauldronInteraction> newInteractionMap() {
		return Util.make(new Object2ObjectOpenHashMap<>(), (map) -> {
			map.defaultReturnValue((blockstate, level, blockpos, player, hand, itemstack) -> {
				return InteractionResult.PASS;
			});
		});
	}*/
}
