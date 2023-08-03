package com.br1ghtsteel.ftground.fluids.fluid_types;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.br1ghtsteel.ftground.FromTheGround;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

public class AcidFluidType extends FluidType {

	private static final ResourceLocation RENDER_OVERLAY = new ResourceLocation(FromTheGround.MOD_ID, "textures/misc/acid_overlay.png");
	private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(FromTheGround.MOD_ID, "blocks/acid_still");
	private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(FromTheGround.MOD_ID, "blocks/acid_flowing");
	private final int tintColor;
	private final Vector3f fogColor;
	
	public AcidFluidType(int tintColor, Vector3f fogColor, Properties properties) {
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
				RenderSystem.setShaderFogStart(1f);
				RenderSystem.setShaderFogEnd(6f);
			}
		});
	}

}
