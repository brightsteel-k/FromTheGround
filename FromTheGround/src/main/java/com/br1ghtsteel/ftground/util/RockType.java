package com.br1ghtsteel.ftground.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.br1ghtsteel.ftground.core.ItemsInit;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;

public enum RockType implements StringRepresentable {
	STONE(0, "stone", 1.5f, MaterialColor.STONE, SoundType.STONE),
	ANDESITE(1, "andesite", 1.5f, MaterialColor.TERRACOTTA_LIGHT_BLUE, SoundType.STONE),
	BLACKSTONE(2, "blackstone", 1.5f, MaterialColor.COLOR_BLACK, SoundType.STONE),
	DEEPSLATE(3, "deepslate", 3.0f, MaterialColor.DEEPSLATE, SoundType.DEEPSLATE),
	DIORITE(4, "diorite", 1.5f, MaterialColor.QUARTZ, SoundType.STONE),
	GRANITE(5, "granite", 1.5f, MaterialColor.TERRACOTTA_RED, SoundType.STONE),
	DOLERITE(6, "dolerite", 1.5f, MaterialColor.TERRACOTTA_GREEN, SoundType.STONE),
	GNEISS(7, "gneiss", 3.0f, MaterialColor.TERRACOTTA_BLUE, SoundType.DEEPSLATE),
	LAMPROPHYRE(8, "lamprophyre", 1.4f, MaterialColor.TERRACOTTA_BLACK, SoundType.STONE),
	LIMESTONE(9, "limestone", 1.1f, MaterialColor.COLOR_LIGHT_GREEN, SoundType.STONE),
	SCHIST(10, "schist", 2.5f, MaterialColor.COLOR_LIGHT_BLUE, SoundType.DEEPSLATE),
	SHALE(11, "shale", 1.5f, MaterialColor.TERRACOTTA_WHITE, SoundType.STONE);

	private static final RockType[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(RockType::getId))
			.toArray((type) -> {
				return new RockType[type];
			});

	private static List<Item> rockItems;

	private final int id;
	private final String name;
	private final MaterialColor color;
	private final float hardness;
	private final SoundType soundType;

	private RockType(int id, String name, float hardness, MaterialColor color, SoundType soundType) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.hardness = hardness;
		this.soundType = soundType;
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

	public MaterialColor getColor() {
		return this.color;
	}

	public float getHardness() {
		return this.hardness;
	}
	
	public SoundType getSoundType() {
		return this.soundType;
	}

	public int getId() {
		return this.id;
	}

	public static RockType byId(int id) {
		if (id < 0 || id >= VALUES.length) {
			id = 0;
		}
		return VALUES[id];
	}

	public String getSerializedName() {
		return this.name;
	}

	public static void initializeRockTypes() {
		rockItems = Arrays.asList(Items.COBBLESTONE, ItemsInit.COBBLED_ANDESITE.get(), Items.BLACKSTONE,
				Items.COBBLED_DEEPSLATE, ItemsInit.COBBLED_DIORITE.get(), ItemsInit.COBBLED_GRANITE.get(),
				ItemsInit.COBBLED_DOLERITE.get(), ItemsInit.COBBLED_GNEISS.get(),
				ItemsInit.COBBLED_LAMPROPHYRE.get(), ItemsInit.COBBLED_LIMESTONE.get(), ItemsInit.COBBLED_SCHIST.get(),
				ItemsInit.COBBLED_SHALE.get());
	}

	public static int parseRockId(Item item) {
		int id = rockItems.indexOf(item);
		if (item.equals(Items.TUFF)) {
			id = 0;
		}
		return id;
	}
}
