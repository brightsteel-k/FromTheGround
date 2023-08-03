package com.br1ghtsteel.ftground.core;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.fluids.AcidFluid;
import com.br1ghtsteel.ftground.fluids.MoltenSaltpeterFluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidsInit {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, FromTheGround.MOD_ID);
	
	public static final RegistryObject<FlowingFluid> ACID_SOURCE = FLUIDS.register("acid_source", () -> new AcidFluid.Source(FluidsInit.ACID_FLUID_PROPERTIES));
	public static final RegistryObject<FlowingFluid> ACID_FLOWING = FLUIDS.register("acid_flowing", () -> new AcidFluid.Flowing(FluidsInit.ACID_FLUID_PROPERTIES));
	
	public static final RegistryObject<FlowingFluid> MOLTEN_SALTPETER_SOURCE = FLUIDS.register("molten_saltpeter_source", () -> new MoltenSaltpeterFluid.Source(FluidsInit.MOLTEN_SALTPETER_FLUID_PROPERTIES));
	public static final RegistryObject<FlowingFluid> MOLTEN_SALTPETER_FLOWING = FLUIDS.register("molten_saltpeter_flowing", () -> new MoltenSaltpeterFluid.Flowing(FluidsInit.MOLTEN_SALTPETER_FLUID_PROPERTIES));
	
	public static final ForgeFlowingFluid.Properties ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(FluidTypesInit.ACID_TYPE, ACID_SOURCE, ACID_FLOWING)
			.block(() -> BlocksInit.ACID.get());
	public static final ForgeFlowingFluid.Properties MOLTEN_SALTPETER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(FluidTypesInit.MOLTEN_SALTPETER_TYPE, MOLTEN_SALTPETER_SOURCE, MOLTEN_SALTPETER_FLOWING)
			.block(() -> BlocksInit.MOLTEN_SALTPETER.get());
}
