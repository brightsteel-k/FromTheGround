package com.br1ghtsteel.ftground.blocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class BrickBlock extends Block
{
	public BrickBlock(MaterialColor matColor)
	{
		super(BlockBehaviour.Properties.of(Material.STONE, matColor)
				.strength(2.0f, 6.0f)
				.sound(SoundType.NETHER_BRICKS)
				.requiresCorrectToolForDrops()
		);
	}
	
	public BrickBlock(DyeColor dyeColor)
	{
		super(BlockBehaviour.Properties.of(Material.STONE, dyeColor)
				.strength(2.0f, 6.0f)
				.sound(SoundType.NETHER_BRICKS)
				.requiresCorrectToolForDrops()
		);
	}
}
