package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;


public class CobbledStoneBlock extends Block
{
	public CobbledStoneBlock(RockType rock)
	{
		super(BlockBehaviour.Properties.of(Material.STONE, rock.getColor())
				.strength(rock.getHardness() + 0.5f, 6.0f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops()
		);
	}
}
