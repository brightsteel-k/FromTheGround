package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.BlocksInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SulfurousBlock extends Block {
	public SulfurousBlock(float h, float r) {
		super(BlockBehaviour.Properties.of(Material.STONE).strength(h, r).sound(SoundType.NETHERRACK)
				.requiresCorrectToolForDrops());
	}

	public SulfurousBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void neighborChanged(BlockState state, Level levelIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!levelIn.isClientSide && (levelIn.getBlockState(pos.above()).is(Blocks.FIRE) || levelIn.getBlockState(pos.above()).is(Blocks.SOUL_FIRE))) {
			levelIn.setBlock(pos.above(), BlocksInit.SULFUR_FIRE.get().defaultBlockState(), 3);
		}
	}
}
