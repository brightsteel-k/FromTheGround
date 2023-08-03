package com.br1ghtsteel.ftground.client.renderers;

import com.br1ghtsteel.ftground.entity_types.ExplodingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExplodingBlockRenderer extends EntityRenderer<ExplodingBlock> {
	private final BlockRenderDispatcher blockRenderer;

	public ExplodingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5f;
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	public void render(ExplodingBlock explosiveBlock, float a, float b, PoseStack poseStack,
			MultiBufferSource source, int c) {
		poseStack.pushPose();
		poseStack.translate(0.0D, 0.5D, 0.0D);
		int i = explosiveBlock.getFuse();
		if (explosiveBlock.shouldBurstOnExplode() && (float) i - b + 1.0F < 10.0F) {
			float f = 1.0F - ((float) i - b + 1.0F) / 10.0F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f *= f;
			f *= f;
			float f1 = 1.0F + f * 0.3F;
			poseStack.scale(f1, f1, f1);
		}

		poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
		poseStack.translate(-0.5D, -0.5D, 0.5D);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, explosiveBlock.getBlockstate(), poseStack, source,
				c, i / 5 % 2 == 0);
		poseStack.popPose();
		super.render(explosiveBlock, a, b, poseStack, source, c);
	}

	public ResourceLocation getTextureLocation(ExplodingBlock explosiveBlock) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
