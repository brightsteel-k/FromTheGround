package com.br1ghtsteel.ftground.commands;

import com.br1ghtsteel.ftground.util.PlantDessication;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class DessicateCommand {
	public DessicateCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("dessicate").requires((source) -> {
			return source.hasPermission(2);
		}).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((command) -> {
			return dessicate(command.getSource(), BlockPosArgument.getLoadedBlockPos(command, "pos"));
		})));
	}
	
	private int dessicate(CommandSourceStack source, BlockPos blockpos) {
		LevelAccessor level = source.getLevel();
		BlockState blockstate = level.getBlockState(blockpos);
		if (blockstate.is(BlockTags.CROPS)) {
			PlantDessication.onPlantGrows(blockstate, level, blockpos);
			return 1;
		} else {
			return 0;
		}
	}
}
