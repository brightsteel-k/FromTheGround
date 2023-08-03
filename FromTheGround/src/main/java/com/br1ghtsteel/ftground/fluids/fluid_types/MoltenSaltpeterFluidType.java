package com.br1ghtsteel.ftground.fluids.fluid_types;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.core.FluidTypesInit;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

public class MoltenSaltpeterFluidType extends FluidType {

	public static final DamageSource MOLTEN_SALTPETER_DAMAGE = new DamageSource(
			FromTheGround.MOD_ID + "_molten_saltpeter").setIsFire();

	private static final ResourceLocation RENDER_OVERLAY = new ResourceLocation(FromTheGround.MOD_ID,
			"textures/misc/molten_saltpeter_overlay.png");
	private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(FromTheGround.MOD_ID,
			"blocks/molten_saltpeter_still");
	private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(FromTheGround.MOD_ID,
			"blocks/molten_saltpeter_flow");
	private final int tintColor;
	private final Vector3f fogColor;

	public MoltenSaltpeterFluidType(int tintColor, Vector3f fogColor, Properties properties) {
		super(properties);
		this.tintColor = tintColor;
		this.fogColor = fogColor;
	}

	public ResourceLocation getStillTexture() {
		return STILL_TEXTURE;
	}

	public ResourceLocation getFlowingTexture() {
		return FLOWING_TEXTURE;
	}

	public int getTintColor() {
		return tintColor;
	}

	public Vector3f getFogColor() {
		return fogColor;
	}

	public static void checkBurnEntity(Entity e) {
		if (e.isInFluidType(FluidTypesInit.MOLTEN_SALTPETER_TYPE.get())) {
			if (!e.fireImmune()) {
				e.setSecondsOnFire(1);
				if (e.hurt(MOLTEN_SALTPETER_DAMAGE, 4.0F)) {
					e.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + e.level.random.nextFloat() * 0.4F);
				}
			}
		}
	}

	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			@Override
			public ResourceLocation getStillTexture() {
				return STILL_TEXTURE;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return FLOWING_TEXTURE;
			}

			@Override
			public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
				return RENDER_OVERLAY;
			}

			@Override
			public int getTintColor() {
				return tintColor;
			}

			@Override
			public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
					int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
				return fogColor;
			}

			@Override
			public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
					float nearDistance, float farDistance, FogShape shape) {
				RenderSystem.setShaderFogStart(0f);
				RenderSystem.setShaderFogEnd(1f);
			}
		});
	}

}
