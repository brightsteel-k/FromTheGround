package com.br1ghtsteel.ftground.tags;

import com.br1ghtsteel.ftground.FromTheGround;

import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public class FTGBlockTags
{
	public static final TagKey<Block> ACID_BREAKS = MakeTag("acid_breaks");
	public static final TagKey<Block> ANFO_BREAKS = MakeTag("anfo_breaks");
	public static final TagKey<Block> AMMONIUM_NITRATE_EFFECTIVE = MakeTag("ammonium_nitrate_effective");
	public static final TagKey<Block> CONCRETE = MakeTag("concrete");
	public static final TagKey<Block> CONCRETE_POWDER = MakeTag("concrete_powder");
	public static final TagKey<Block> FARMLAND_BLOCKS = MakeTag("farmland_blocks");
	public static final TagKey<Block> FIERY = MakeTag("fiery");
	public static final TagKey<Block> FLARE_REPLACEABLE = MakeTag("flare_replaceable");
	public static final TagKey<Block> GLASS = MakeTag("glass");
	public static final TagKey<Block> HAMMERABLE = BlockTags.create(new ResourceLocation("minecraft", "mineable/hammer"));
	public static final TagKey<Block> INFESTED_BLOCKS = MakeTag("infested_blocks");
	public static final TagKey<Block> SULFUR_FIRE_BASES = MakeTag("sulfur_fire_bases");
	
	static TagKey<Block> MakeTag(String name)
	{
		return BlockTags.create(new ResourceLocation(FromTheGround.MOD_ID, name));
	}
}
