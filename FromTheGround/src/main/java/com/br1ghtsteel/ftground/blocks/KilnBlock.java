package com.br1ghtsteel.ftground.blocks;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.block_entities.KilnBlockEntity;
import com.br1ghtsteel.ftground.core.BlockEntityTypesInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class KilnBlock extends AbstractFurnaceBlock {
	public KilnBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
		return new KilnBlockEntity(blockpos, blockstate);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockstate,
			BlockEntityType<T> blockEntityType) {
		return createFurnaceTicker(level, blockEntityType, BlockEntityTypesInit.KILN.get());
	}

	protected void openContainer(Level level, BlockPos blockpos, Player player) {
		BlockEntity blockentity = level.getBlockEntity(blockpos);
		if (blockentity instanceof KilnBlockEntity) {
			player.openMenu((KilnBlockEntity) blockentity);
		}
	}

	public void animateTick(BlockState blockstate, Level level, BlockPos blockpos, RandomSource rnd) {
		if (blockstate.getValue(LIT)) {
			double d0 = (double) blockpos.getX() + 0.5D;
			double d1 = (double) blockpos.getY();
			double d2 = (double) blockpos.getZ() + 0.5D;
			double r = rnd.nextDouble();
			if (r < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
						false);
			}
			if (r < 0.6D) {
				level.addParticle(ParticleTypes.SMOKE, d0, d1 + 1.1D, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
