package com.br1ghtsteel.ftground.client.renderers;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.client.models.ThrownFlareModel;
import com.br1ghtsteel.ftground.entity_types.ThrownFlare;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownFlareRenderer extends EntityRenderer<ThrownFlare>
{
	public static final ResourceLocation FLARE_LOCATION = new ResourceLocation(FromTheGround.MOD_ID, "textures/entity/projectiles/thrown_flare.png");
	private final ThrownFlareModel model;

	public ThrownFlareRenderer(EntityRendererProvider.Context ctx)
	{
	   super(ctx);
	   this.model = new ThrownFlareModel(ctx.bakeLayer(ThrownFlareModel.FLARE_LAYER));
	}

	public void render(ThrownFlare flare, float a, float b, PoseStack poseStack, MultiBufferSource source, int c)
	{
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(b, flare.yRotO, flare.getYRot()) - 90.0F));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(b, flare.xRotO, flare.getXRot()) + 90.0F));
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(source, this.model.renderType(this.getTextureLocation(flare)), false, false);
		this.model.renderToBuffer(poseStack, vertexconsumer, c, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
		super.render(flare, a, b, poseStack, source, c);
	}

	public ResourceLocation getTextureLocation(ThrownFlare flare)
	{
	   return FLARE_LOCATION;
	}
}
