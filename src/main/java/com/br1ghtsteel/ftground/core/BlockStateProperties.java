package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.util.RockType;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockStateProperties {

	public static final IntegerProperty LEVEL_GUANO = IntegerProperty.create("guano_level", 0, 8);
	public static final IntegerProperty LEVEL_FERTILIZER = IntegerProperty.create("level", 0, 12);
	public static final BooleanProperty DECAYING = BooleanProperty.create("decaying");
	public static final BooleanProperty DETONATOR_PRIMED_NORTH = BooleanProperty.create("primed_north");
	public static final BooleanProperty DETONATOR_PRIMED_EAST = BooleanProperty.create("primed_east");
	public static final BooleanProperty DETONATOR_PRIMED_WEST = BooleanProperty.create("primed_west");
	public static final BooleanProperty DETONATOR_PRIMED_SOUTH = BooleanProperty.create("primed_south");
	public static final BooleanProperty IRREGULATOR_POLARITY = BooleanProperty.create("polarity");
	public static final EnumProperty<RockType> ORE_ROCK_TYPE = EnumProperty.create("rock_type", RockType.class, (rockType) -> {
		return rockType.getHardness() < 2;
	});
	public static final EnumProperty<RockType> DEEP_ORE_ROCK_TYPE = EnumProperty.create("rock_type", RockType.class, (rockType) -> {
		return rockType.getHardness() >= 2;
	});

}
