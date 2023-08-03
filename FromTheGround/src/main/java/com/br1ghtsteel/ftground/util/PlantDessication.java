package com.br1ghtsteel.ftground.util;

import java.util.LinkedList;
import java.util.Queue;

import com.br1ghtsteel.ftground.blocks.FertilizerBoxBlock;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PlantDessication {

	public static final int MAX_FARMLAND_BLOCKS = 24;
	public static final int FERTILIZER_BOX_RANGE = 8;

	public static void onPlantGrows(BlockState blockstate, LevelAccessor level, BlockPos blockpos) {
		if (checkDessication(level, blockpos.below(), Direction.DOWN, new LinkedList<>(), new LinkedList<>(),
				new LinkedList<>(), new LinkedList<>())) {
			Minecraft.getInstance().player.chat("DESSICATED"); // TODO:DEBUG
		}
	}

	public static boolean checkDessication(LevelAccessor level, BlockPos blockpos, Direction dir,
			Queue<BlockPos> passed, Queue<BlockPos> found, Queue<BlockPos> toVisit, Queue<Direction> toVisitDir) {
		if (blockpos == null) { // No more farmland blocks
			return false;
		} else if (passed.contains(blockpos) || found.contains(blockpos)) { // Already checked this block
			return checkDessication(level, toVisit.poll(), toVisitDir.poll(), passed, found, toVisit, toVisitDir);
		} else {
			if (level.getBlockState(blockpos).is(FTGBlockTags.FARMLAND_BLOCKS)) {
				found.add(blockpos);

				// Check end condition
				if (found.size() >= MAX_FARMLAND_BLOCKS) {
					if (checkFertilizerBox(level, found.peek())) {
						return false;
					}
					dessicateBlocks(level, found);
					return true;
				}

				// Queue adjacent blocks
				Direction prevDirection = dir.getOpposite();
				for (Direction d : Direction.Plane.HORIZONTAL) {
					if (d != prevDirection) {
						toVisit.add(blockpos.relative(d));
						toVisitDir.add(d);
					}
				}
			} else {
				passed.add(blockpos);
			}

			// Check next block
			return checkDessication(level, toVisit.poll(), toVisitDir.poll(), passed, found, toVisit, toVisitDir);
		}
	}

	public static void dessicateBlocks(LevelAccessor level, Queue<BlockPos> farmlandBlocks) {
		RandomSource random = level.getRandom();
		for (BlockPos blockpos : farmlandBlocks) {
			if (random.nextFloat() < 0.6f) {
				convertFarmland(level, blockpos);
			}
		}
	}

	public static boolean checkFertilizerBox(LevelAccessor level, BlockPos centrePos) {
		BlockPos.MutableBlockPos mutableBlockpos = new BlockPos.MutableBlockPos();
		for (int x = -FERTILIZER_BOX_RANGE; x <= FERTILIZER_BOX_RANGE; x++) {
			for (int z = -FERTILIZER_BOX_RANGE; z <= FERTILIZER_BOX_RANGE; z++) {
				for (int y = 0; y <= 1; y++) {
					mutableBlockpos.setWithOffset(centrePos, x, y, z);
					BlockState tState = level.getBlockState(mutableBlockpos);
					if (tState.is(BlocksInit.FERTILIZER_BOX.get())) {
						if (FertilizerBoxBlock.removeSaltpeter(level, mutableBlockpos)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static void rejuvenateBlocks(LevelAccessor level, BlockPos blockpos, Direction dir, Queue<BlockPos> toRejuvenate, Queue<Direction> toRejuvenateDir, int amount) {
		if (blockpos == null) {
			return;
		} else if (level.getBlockState(blockpos).is(BlocksInit.DESSICATED_FARMLAND.get())) {
			level.setBlock(blockpos, Blocks.FARMLAND.defaultBlockState(), 3);
			amount--;
			
			// Queue adjacent blocks
			if (amount > 0) {
				Direction prevDirection = dir.getOpposite();
				for (Direction d : Direction.Plane.HORIZONTAL) {
					if (d != prevDirection) {
						toRejuvenate.add(blockpos.relative(d));
						toRejuvenateDir.add(d);
					}
				}
				rejuvenateBlocks(level, toRejuvenate.poll(), toRejuvenateDir.poll(), toRejuvenate, toRejuvenateDir, amount);
			}
		} else {
			rejuvenateBlocks(level, toRejuvenate.poll(), toRejuvenateDir.poll(), toRejuvenate, toRejuvenateDir, amount);
		}
	}

	// REQUIRES: level.getBlockState(blockpos).is(Blocks.FARMLAND)
	public static void convertFarmland(LevelAccessor level, BlockPos blockpos) {
		if (level.isClientSide()) {
			return;
		}
		
		if (level.getBlockState(blockpos.above()).is(BlockTags.CROPS)) {
			level.setBlock(blockpos.above(), BlocksInit.DEAD_CROP.get().defaultBlockState(), 3);
		}
		level.setBlock(blockpos, BlocksInit.DESSICATED_FARMLAND.get().defaultBlockState(), 3);
	}
}
