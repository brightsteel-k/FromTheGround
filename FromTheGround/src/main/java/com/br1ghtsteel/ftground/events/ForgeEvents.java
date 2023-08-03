package com.br1ghtsteel.ftground.events;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.commands.DessicateCommand;
import com.br1ghtsteel.ftground.fluids.fluid_types.MoltenSaltpeterFluidType;
import com.br1ghtsteel.ftground.util.EntityModifications;
import com.br1ghtsteel.ftground.util.PlantDessication;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = FromTheGround.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEvents {

	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinLevelEvent e) {
		if (e.getEntity() instanceof WitherSkeleton) {
			EntityModifications.equipWitherSkeleton((WitherSkeleton) e.getEntity());
		}
	}

	@SubscribeEvent
	public static void onCropGrows(BlockEvent.CropGrowEvent event) {
		LevelAccessor level = event.getLevel();
		Block block = event.getState().getBlock();
		if (!(block instanceof IPlantable)
				|| ((IPlantable) block).getPlantType(event.getLevel(), event.getPos()) != PlantType.CROP) {
			return;
		}

		if (level.getRandom().nextFloat() <= 0.001f) {
			PlantDessication.onPlantGrows(event.getState(), level, event.getPos());
		}
	}

	@SubscribeEvent
	public static void onCommandsRegister(RegisterCommandsEvent e) {
		new DessicateCommand(e.getDispatcher());
		ConfigCommand.register(e.getDispatcher());
	}

	@SubscribeEvent
	public static void onEntityUpdate(LivingTickEvent e) {
		MoltenSaltpeterFluidType.checkBurnEntity(e.getEntity());
	} 
}
