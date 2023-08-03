package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class GravelBlock extends FallingBlock
{
	private static int dustColor;
	
	public GravelBlock(RockType rock)
	{
		super(BlockBehaviour.Properties.of(Material.SAND, rock.getColor())
				.strength(0.6f)
				.sound(SoundType.GRAVEL)
		);
		dustColor = rock.getColor().col;
	}

	@Override
	public int getDustColor(BlockState state, BlockGetter getter, BlockPos blockPos)
	{
	   return dustColor;
	}
}
