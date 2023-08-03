package com.br1ghtsteel.ftground.client.renderers;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.client.models.ThrownFlareModel;
import com.br1ghtsteel.ftground.entity_types.FlareArrow;
import com.br1ghtsteel.ftground.entity_types.ThrownFlare;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlareArrowRenderer extends ArrowRenderer<FlareArrow> {
	public static final ResourceLocation FLARE_LOCATION = new ResourceLocation(FromTheGround.MOD_ID, "textures/entity/projectiles/flare_arrow.png");

	public FlareArrowRenderer(EntityRendererProvider.Context context) {
	   super(context);
	}

	public ResourceLocation getTextureLocation(FlareArrow flareArrow) {
	   return FLARE_LOCATION;
	}
}
