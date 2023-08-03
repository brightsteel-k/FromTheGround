package com.br1ghtsteel.ftground.blocks;

import java.util.LinkedList;

import com.br1ghtsteel.ftground.core.ItemsInit;
import com.br1ghtsteel.ftground.util.PlantDessication;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DessicatedFarmBlock extends Block {

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public DessicatedFarmBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState blockstate) {
		return true;
	}

	public VoxelShape getShape(BlockState blockstate, BlockGetter blockGetter, BlockPos blockpos,
			CollisionContext collisionContext) {
		return SHAPE;
	}
	
	@Override
	public InteractionResult use(BlockState blockstate, Level level, BlockPos blockpos, Player player,
			InteractionHand hand, BlockHitResult blockHitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(ItemsInit.SALTPETER.get())) {
			if (!level.isClientSide) {
				PlantDessication.rejuvenateBlocks(level, blockpos, Direction.DOWN, new LinkedList<>(), new LinkedList<>(), 5);
				level.levelEvent(1500, blockpos, 1);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	public boolean isPathfindable(BlockState blockstate, BlockGetter blockGetter, BlockPos blockpos,
			PathComputationType pathComputationType) {
		return false;
	}
}
