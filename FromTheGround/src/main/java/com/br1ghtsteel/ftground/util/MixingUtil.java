package com.br1ghtsteel.ftground.util;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.EffectsInit;
import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.tags.FTGBlockTags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class MixingUtil {

	public static final Map<String, Mixture> MIXTURES = Map.ofEntries(
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.sulfuric_acid.concentration.0",
					new Mixture(11122644, new MobEffectInstance(EffectsInit.CORROSION.get(), 120, 0))),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.sulfuric_acid.concentration.1",
					new Mixture(10993548, new MobEffectInstance(EffectsInit.CORROSION.get(), 300, 0))),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.sulfuric_acid.concentration.2",
					new Mixture(10537304, new MobEffectInstance(EffectsInit.CORROSION.get(), 400, 1),
							new MobEffectInstance(EffectsInit.INSTANT_ABRASION.get(), 1, 0))),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.sulfuric_acid.concentration.3",
					new Mixture(10530086, new MobEffectInstance(EffectsInit.CORROSION.get(), 500, 2),
							new MobEffectInstance(EffectsInit.INSTANT_ABRASION.get(), 1, 1))),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.sulfuric_acid.concentration.4",
					new Mixture(11769088, new MobEffectInstance(EffectsInit.CORROSION.get(), 600, 3),
							new MobEffectInstance(EffectsInit.INSTANT_ABRASION.get(), 1, 2))),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.nitric_acid",
					new Mixture(9977908, new MobEffectInstance(EffectsInit.INSTANT_ABRASION.get(), 1, 1))),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.bitumen_fuel", new Mixture(3552822)),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.ammonium_nitricum", new Mixture(11841175)),
			new AbstractMap.SimpleEntry<String, Mixture>("item.ftground.nitrochalk_fertilizer", new Mixture(13001621)));

	public static final DamageSource ACID_DAMAGE = new DamageSource(FromTheGround.MOD_ID + "_acid").bypassArmor().bypassMagic();
	
	public static final Map<Block, Block> ACID_DAMAGED_BLOCKS = Map.ofEntries(
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.GRASS_BLOCK, BlocksInit.DECAYING_GRASS_BLOCK.get()),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.DIRT_PATH, Blocks.DIRT),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.PODZOL, BlocksInit.DECAYING_GRASS_BLOCK.get()),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.MOSSY_STONE_BRICKS, Blocks.STONE_BRICKS));

	public static final Map<Block, Block> VITRIOL_DAMAGED_BLOCKS = Map.ofEntries(
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_TILES),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.SANDSTONE, Blocks.SAND),
			new AbstractMap.SimpleEntry<Block, Block>(Blocks.DIRT, Blocks.COARSE_DIRT));
	
	public static List<MobEffectInstance> getEffects(ItemStack item) {
		return getEffects(item.getDescriptionId());
	}
	
	public static List<MobEffectInstance> getEffects(String mixture) {
		List<MobEffectInstance> list = Lists.newArrayList();
		if (MIXTURES.containsKey(mixture)) {
			list.addAll(MIXTURES.get(mixture).getEffects());
		}
		return list;
	}

	public static void addPotionTooltip(ItemStack itemstack, List<Component> tooltip, float duration) {
		List<MobEffectInstance> list = getEffects(itemstack);
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
		if (!list.isEmpty()) {
			for (MobEffectInstance mobeffectinstance : list) {
				MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
				MobEffect mobeffect = mobeffectinstance.getEffect();
				Map<Attribute, AttributeModifier> map = mobeffect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(
								attributemodifier.getName(), mobeffect
										.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier),
								attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (mobeffectinstance.getAmplifier() > 0) {
					mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent,
							Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
				}

				if (mobeffectinstance.getDuration() > 20) {
					mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent,
							MobEffectUtil.formatDuration(mobeffectinstance, duration));
				}

				tooltip.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
			}
		}

		if (!list1.isEmpty()) {
			tooltip.add(CommonComponents.EMPTY);
			tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.GRAY));

			for (Pair<Attribute, AttributeModifier> pair : list1) {
				AttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE
						&& attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					tooltip.add(Component
							.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(),
									ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
									Component.translatable(pair.getFirst().getDescriptionId()))
							.withStyle(ChatFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 *= -1.0D;
					tooltip.add(Component
							.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(),
									ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
									Component.translatable(pair.getFirst().getDescriptionId()))
							.withStyle(ChatFormatting.RED));
				}
			}
		}
	}
	
	public static boolean isAcid(ItemStack itemstack) {
		return itemstack.is(ItemsInit.SULFURIC_ACID.get()) || itemstack.is(ItemsInit.NITRIC_ACID.get());
	}
	
	public static int getSulfuricAcidColor(ItemStack itemstack, int tintIndex) {
		if (tintIndex == 0) {
			return getColor(itemstack.getDescriptionId());
		}
		return -1;
	}
	
	public static int getColor(ItemStack itemstack) {
		return getColor(itemstack.getDescriptionId());
	}
	
	public static int getColor(String mixture) {
		int color = 0;
		if (MIXTURES.containsKey(mixture)) {
			color = MIXTURES.get(mixture).getColor();
		}
		return color;
	}
	
	public static void applyAcidToRange(Level level, BlockPos sourcePos, int r, int ry, boolean vitriol, Entity sourceEntity) {
		int r2 = r * r;
		BlockPos.MutableBlockPos mutableBlockpos = new BlockPos.MutableBlockPos();
		for (int x = -r; x <= r; x++) {
			for (int z = -r; z <= r; z++) {
				if (x * x + z * z <= r2) {
					for (int y = -ry; y <= ry; y++) {
						mutableBlockpos.setWithOffset(sourcePos, x, y, z);
						applyAcidToBlock(level, mutableBlockpos, sourceEntity);
						if (vitriol) {
							applyVitriolToBlock(level, mutableBlockpos);
						}
					}					
				}
			}
		}
	}
	
	public static void applyAcidToBlock(Level level, BlockPos blockpos, Entity sourceEntity) {
		BlockState blockstate = level.getBlockState(blockpos);
		if (blockstate.is(BlockTags.LEAVES)) {
			level.setBlock(blockpos, BlocksInit.DECAYING_LEAVES.get().defaultBlockState(), 3);
		} else if (blockstate.is(BlockTags.SAPLINGS)) {
			level.setBlock(blockpos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
		} else if (blockstate.is(BlockTags.SMALL_FLOWERS)) {
			level.setBlock(blockpos, BlocksInit.DEAD_FLOWER.get().defaultBlockState(), 3);
		} else if (blockstate.is(Blocks.FARMLAND)) {
			PlantDessication.convertFarmland(level, blockpos);
		} else if (blockstate.is(BlockTags.CROPS)) {
			level.setBlock(blockpos, BlocksInit.DEAD_CROP.get().defaultBlockState(), 3);
		} else if (blockstate.is(FTGBlockTags.ACID_BREAKS)) {
			level.destroyBlock(blockpos, true, sourceEntity);
		} else {
			Block newBlock = ACID_DAMAGED_BLOCKS.get(blockstate.getBlock());
			if (newBlock != null) {
				level.setBlock(blockpos, newBlock.defaultBlockState(), 3);
			}
		}
	}
	
	public static void applyVitriolToBlock(Level level, BlockPos blockpos) {
		Block newBlock = VITRIOL_DAMAGED_BLOCKS.get(level.getBlockState(blockpos).getBlock());
		if (newBlock != null) {
			level.setBlock(blockpos, newBlock.defaultBlockState(), 3);
		}
	}

	public static class Mixture {
		private int color;
		private List<MobEffectInstance> effects = new ArrayList<>();

		public Mixture(int color, MobEffectInstance... effectsIn) {
			this.color = color;
			this.effects = Arrays.asList(effectsIn);
		}
		
		public Mixture(int color) {
			this.color = color;
		}

		public List<MobEffectInstance> getEffects() {
			return effects;
		}
		
		public int getColor() {
			return color;
		}
	}
}
