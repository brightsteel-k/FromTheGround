package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class StoneBlock extends Block
{
	public StoneBlock(RockType rock)
	{
		super(BlockBehaviour.Properties.of(Material.STONE, rock.getColor())
				.strength(rock.getHardness(), 6.0f)
				.sound(rock.getSoundType())
				.requiresCorrectToolForDrops());
	}
}
