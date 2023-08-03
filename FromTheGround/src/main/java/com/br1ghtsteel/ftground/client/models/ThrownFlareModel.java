package com.br1ghtsteel.ftground.client.models;

import com.br1ghtsteel.ftground.FromTheGround;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ThrownFlareModel extends Model
{
	public static ModelLayerLocation FLARE_LAYER = new ModelLayerLocation(new ResourceLocation(FromTheGround.MOD_ID, "flare"), "main");
	private final ModelPart root;
	
	public ThrownFlareModel(ModelPart part)
	{
		super(RenderType::entitySolid);
		this.root = part;
	}
	
	public static LayerDefinition createLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0f)), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 12, 16);
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}