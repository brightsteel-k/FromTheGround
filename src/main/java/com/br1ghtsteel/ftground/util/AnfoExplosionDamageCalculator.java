package com.br1ghtsteel.ftground.util;

import java.util.Optional;

import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class AnfoExplosionDamageCalculator extends ExplosionDamageCalculator {
	
	public static final AnfoExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new AnfoExplosionDamageCalculator();
	
	public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos blockpos,
			BlockState blockstate, FluidState fluidState) {
		if (blockstate.isAir() && fluidState.isEmpty()) {
			return Optional.empty();
		}
		
		float factor = blockstate.is(FTGBlockTags.ANFO_BREAKS) ? 1f : 100f;
		return Optional.of(Math.max(blockstate.getExplosionResistance(level, blockpos, explosion),
						fluidState.getExplosionResistance(level, blockpos, explosion)) * factor);
	}
}