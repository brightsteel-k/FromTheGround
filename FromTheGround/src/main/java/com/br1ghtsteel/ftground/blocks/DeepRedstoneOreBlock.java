package com.br1ghtsteel.ftground.blocks;

import com.br1ghtsteel.ftground.core.BlockStateProperties;
import com.br1ghtsteel.ftground.util.RockType;
import com.br1ghtsteel.ftground.util.StoneVariantsUtil;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class DeepRedstoneOreBlock extends RedStoneOreBlock {
	
	public static final EnumProperty<RockType> ROCK_TYPE = BlockStateProperties.DEEP_ORE_ROCK_TYPE;
	public static final int[] VALID_ROCK_TYPES = new int[] { 3, 7, 10 };
	
	public DeepRedstoneOreBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)).setValue(ROCK_TYPE, RockType.DEEPSLATE));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		if (blockPlaceContext.getPlayer() == null) {
			return super.getStateForPlacement(blockPlaceContext);
		}
		
		RockType rockType = RockType.byId(StoneVariantsUtil.getRockType(blockPlaceContext.getItemInHand()));
		if (ROCK_TYPE.getPossibleValues().contains(rockType)) {
			return defaultBlockState().setValue(ROCK_TYPE, rockType);
		} else {
			return defaultBlockState();
		}
	}

	@Override
	public SoundType getSoundType(BlockState blockstate) {
		return blockstate.getValue(ROCK_TYPE).getSoundType();
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
		stateDefinition.add(LIT).add(ROCK_TYPE);
	};
}
