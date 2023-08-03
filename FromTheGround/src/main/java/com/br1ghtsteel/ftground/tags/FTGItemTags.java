package com.br1ghtsteel.ftground.tags;

import com.br1ghtsteel.ftground.FromTheGround;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FTGItemTags
{
	public static final TagKey<Item> CORROSION_RESISTANT = MakeTag("corrosion_resistant");
	public static final TagKey<Item> MIXING_INGREDIENTS = MakeTag("mixing/ingredients");
	public static final TagKey<Item> MIXING_BASES = MakeTag("mixing/bases");
	
	static TagKey<Item> MakeTag(String name)
	{
		return ItemTags.create(new ResourceLocation(FromTheGround.MOD_ID, name));
	}
}
