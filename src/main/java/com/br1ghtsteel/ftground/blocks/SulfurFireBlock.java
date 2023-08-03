package com.br1ghtsteel.ftground.blocks;

import java.util.List;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.tags.FTGBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;

public class SulfurFireBlock extends BaseFireBlock {
	public SulfurFireBlock() {
		super(BlockBehaviour.Properties.of(Material.FIRE, MaterialColor.COLOR_PURPLE).noCollission().instabreak()
				.noLootTable().lightLevel(x -> {
					return 13;
				}), 2.0f);
	}

	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean b) {
		level.scheduleTick(pos, this, getFireTickDelay(level.random));
		super.onPlace(state, level, pos, state2, b);
	}

	public BlockState updateShape(BlockState state1, Direction direction, BlockState state2, LevelAccessor level,
			BlockPos pos1, BlockPos pos2) {
		return this.canSurvive(state1, level, pos1) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSurviveOnBlock(level.getBlockState(pos.below()));
	}

	public static boolean canSurviveOnBlock(BlockState state) {
		return state.is(FTGBlockTags.SULFUR_FIRE_BASES);
	}

	protected boolean canBurn(BlockState blockstate) {
		return true;
	}

	protected boolean isNearRain(Level level, BlockPos pos) {
		return level.isRainingAt(pos) || level.isRainingAt(pos.west()) || level.isRainingAt(pos.east())
				|| level.isRainingAt(pos.north()) || level.isRainingAt(pos.south());
	}

	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rnd) {
		// Set up next tick
		level.scheduleTick(pos, this, getFireTickDelay(rnd));

		// Check for rain
		if (level.isRaining() && isNearRain(level, pos) && rnd.nextBoolean()) {
			level.removeBlock(pos, false);
			return;
		}
		BlockPos.MutableBlockPos mutableBlockpos = new BlockPos.MutableBlockPos();

		// Affect blocks
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				int x2 = x * x;
				int z2 = z * z;
				if (x2 + z2 > 16) {
					continue;
				}
				
				for (int y = -2; y <= 8; y++) {
					mutableBlockpos.setWithOffset(pos, x, y, z);
					BlockState tState = level.getBlockState(mutableBlockpos);
					if (tState.is(BlockTags.LEAVES)) {
						// Decay leaves
						if (rnd.nextFloat() * 20 >= x2 + z2)
							level.setBlockAndUpdate(mutableBlockpos, BlocksInit.DECAYING_LEAVES.get().defaultBlockState());
					}

					if (tState.is(FTGBlockTags.INFESTED_BLOCKS)) {
						if (rnd.nextBoolean())
							level.destroyBlock(mutableBlockpos, true);
					}
				}
			}
		}

		// Affect entities
		burnSulfurVulnerableEntities(level, new AABB(pos.east(7).north(7).below(2), pos.west(7).south(7).above(5)));
		burnAllEntities(level, new AABB(pos.east(1).north(1).below(1), pos.west(1).south(1).above(3)));
	}

	private static void burnSulfurVulnerableEntities(Level level, AABB range) {
		List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, range);
		for (LivingEntity entity : entityList) {
			if (entity.getType().equals(EntityType.SILVERFISH) || entity.getType().equals(EntityType.ENDERMITE)
					|| entity.getType().equals(EntityType.CAVE_SPIDER)) {
				entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 4));
			}
		}
	}

	private static void burnAllEntities(Level level, AABB range) {
		List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, range);
		for (LivingEntity entity : entityList) {
			if (!entity.getItemBySlot(EquipmentSlot.HEAD).is(Items.TURTLE_HELMET)) {
				entity.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 0));
			}
		}
	}

	private static int getFireTickDelay(RandomSource rnd) {
		return 40 + rnd.nextInt(40);
	}
}