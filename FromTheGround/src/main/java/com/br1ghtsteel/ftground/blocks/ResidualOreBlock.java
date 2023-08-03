package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class ResidualOreBlock extends FallingBlock
{
	private static int dustColor;
	
	public ResidualOreBlock(RockType baseRock)
	{
		super(BlockBehaviour.Properties.of(Material.SAND, baseRock.getColor())
				.strength(3.0f, 3.0f)
				.sound(SoundType.GRAVEL)
				.requiresCorrectToolForDrops()
		);
		dustColor = baseRock.getColor().col;
	}
	
	public ResidualOreBlock(BlockBehaviour.Properties properties, RockType baseRock)
	{
		super(properties);
		dustColor = baseRock.getColor().col;
	}

	@Override
	public int getDustColor(BlockState state, BlockGetter getter, BlockPos blockPos)
	{
	   return dustColor;
	}
}
