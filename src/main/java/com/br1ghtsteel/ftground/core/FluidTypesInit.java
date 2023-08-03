package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.fluids.fluid_types.AcidFluidType;
import com.br1ghtsteel.ftground.fluids.fluid_types.MoltenSaltpeterFluidType;
import com.mojang.math.Vector3f;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidTypesInit {
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, FromTheGround.MOD_ID);
	
	public static final RegistryObject<FluidType> ACID_TYPE = FLUID_TYPES.register("acid", () -> new AcidFluidType(0xFFFFFFFF, new Vector3f(139f / 255f, 138f / 255f, 41f / 255f), 
			FluidType.Properties.create()
			.descriptionId("block.ftground.acid")
			.fallDistanceModifier(0.5F)
			.canSwim(true)
			.canDrown(true)
			.pathType(BlockPathTypes.LAVA)
			.adjacentPathType(null)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
			.density(0)
			.viscosity(10)));
	
	public static final RegistryObject<FluidType> MOLTEN_SALTPETER_TYPE = FLUID_TYPES.register("molten_saltpeter", () -> new MoltenSaltpeterFluidType(0xFFFFFFFF, new Vector3f(252f / 255f, 109f / 255f, 109f / 255f), 
			FluidType.Properties.create()
            .descriptionId("block.ftground.molten_saltpeter")
            .canSwim(false)
            .canDrown(false)
            .canExtinguish(false)
            .pathType(BlockPathTypes.LAVA)
            .adjacentPathType(null)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
            .lightLevel(13)
            .density(3000)
            .viscosity(6000)
            .temperature(1300)));
	
	public static void registerFluidInteractions() {
		FluidInteractionRegistry.addInteraction(MOLTEN_SALTPETER_TYPE.get(), new InteractionInformation(
				ForgeMod.WATER_TYPE.get(),
				fluidState -> fluidState.isSource() ? BlocksInit.SALTPETER_BLOCK.get().defaultBlockState() : BlocksInit.LAMPROPHYRE_GRAVEL.get().defaultBlockState()));
	}
}
