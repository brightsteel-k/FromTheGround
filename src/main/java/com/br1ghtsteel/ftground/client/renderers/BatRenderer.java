package com.br1ghtsteel.ftground.client.renderers;

import com.br1ghtsteel.ftground.client.models.BatModel;
import com.br1ghtsteel.ftground.entity_types.Bat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BatRenderer extends MobRenderer<Bat, BatModel> {
   private static final ResourceLocation BAT_LOCATION = new ResourceLocation("textures/entity/bat.png");

   public BatRenderer(EntityRendererProvider.Context context) {
      super(context, new BatModel(context.bakeLayer(ModelLayers.BAT)), 0.25F);
   }

   public ResourceLocation getTextureLocation(Bat bat) {
      return BAT_LOCATION;
   }

   protected void scale(Bat bat, PoseStack poseStack, float x) {
      poseStack.scale(0.35F, 0.35F, 0.35F);
   }

   protected void setupRotations(Bat bat, PoseStack poseStack, float x, float y, float z) {
      if (bat.isResting()) {
         poseStack.translate(0.0D, (double)-0.1F, 0.0D);
      } else {
         poseStack.translate(0.0D, (double)(Mth.cos(x * 0.3F) * 0.1F), 0.0D);
      }

      super.setupRotations(bat, poseStack, x, y, z);
   }
}