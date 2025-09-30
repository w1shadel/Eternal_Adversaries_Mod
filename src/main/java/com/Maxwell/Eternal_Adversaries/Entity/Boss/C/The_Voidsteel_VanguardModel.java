package com.Maxwell.Eternal_Adversaries.Entity.Boss.C;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.Maxwell.Eternal_Adversaries.Entity.Boss.Animation.The_Voidsteel_VanguardModelAnimation;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class The_Voidsteel_VanguardModel extends HierarchicalModel<The_VoidSteel_Vanguard> {
	@SuppressWarnings("removal")
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Eternal_Adversaries.MODID, "the_voidsteel_vanguardmodel"), "main");
	private final ModelPart waist;
    private final ModelPart head;

    public The_Voidsteel_VanguardModel(ModelPart root) {
		this.waist = root.getChild("waist");
        ModelPart body = this.waist.getChild("body");
		this.head = body.getChild("head");
        ModelPart hat = this.head.getChild("hat");
        ModelPart rightArm = body.getChild("rightArm");
        ModelPart upper = rightArm.getChild("upper");
        ModelPart under = rightArm.getChild("under");
        ModelPart rightItem = under.getChild("rightItem");
        ModelPart leftArm = body.getChild("leftArm");
        ModelPart upper4 = leftArm.getChild("upper4");
        ModelPart under4 = leftArm.getChild("under4");
        ModelPart left_sheiled = under4.getChild("left_sheiled");
        ModelPart leftItem = leftArm.getChild("leftItem");
        ModelPart rightLeg = body.getChild("rightLeg");
        ModelPart upper2 = rightLeg.getChild("upper2");
        ModelPart under2 = rightLeg.getChild("under2");
        ModelPart rightLeg2 = body.getChild("rightLeg2");
        ModelPart upper3 = rightLeg2.getChild("upper3");
        ModelPart under3 = rightLeg2.getChild("under3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 10.4F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 15).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(32, 8).addBox(4.2F, -2.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6F, 5.0F, 3.0F, 0.829F, 0.0F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 42).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 10.0F, -2.1F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(44, 39).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 10.0F, -2.1F, 0.0F, 0.0F, 0.5236F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(40, 50).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.5F, -1.8F, 0.0436F, 0.0F, 0.0F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(46, 22).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.5F, -2.0F, 0.0436F, 0.0F, 0.0F));

		PartDefinition body_r3 = body.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(46, 18).addBox(-2.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.5F, -1.9F, -0.0873F, 0.0F, 0.0F));

		PartDefinition body_r4 = body.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(32, 4).addBox(-4.0F, -1.5F, -0.5F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, -2.5F, 0.3054F, 0.0F, 0.0F));

		PartDefinition body_r5 = body.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -1.5F, -0.5F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, -2.5F, -0.3054F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition upper = rightArm.addOrReplaceChild("upper", CubeListBuilder.create().texOffs(32, 22).addBox(-2.9F, -1.3F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(36, 28).addBox(-1.9F, -2.3F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 42).addBox(-1.9F, 0.7F, -1.0F, 2.0F, 6.4F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.9F, 0.3F, 0.0F));

		PartDefinition rightArm_r1 = upper.addOrReplaceChild("rightArm_r1", CubeListBuilder.create().texOffs(46, 8).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4F, -1.8F, 0.0F, 1.5708F, 0.0F, -0.8727F));

		PartDefinition under = rightArm.addOrReplaceChild("under", CubeListBuilder.create().texOffs(36, 33).addBox(-1.0F, -0.3F, -1.0F, 2.0F, 6.6F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.7F, 0.0F));

		PartDefinition rightItem = under.addOrReplaceChild("rightItem", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.4772F, -2.0447F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -1.4772F, -3.0447F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 32).addBox(-0.5F, -0.4772F, -14.0447F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.2772F, -0.9553F));

		PartDefinition cube_r4 = rightItem.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 5).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0228F, -7.8447F, -0.192F, 0.0F, 0.0F));

		PartDefinition cube_r5 = rightItem.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1772F, -7.9447F, 0.1571F, 0.0F, 0.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition upper4 = leftArm.addOrReplaceChild("upper4", CubeListBuilder.create().texOffs(32, 42).addBox(-0.05F, 1.45F, -1.0F, 2.0F, 6.4F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 33).addBox(1.95F, -0.55F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 32).addBox(-0.05F, -1.55F, -2.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.95F, -0.45F, 0.0F));

		PartDefinition leftArm_r1 = upper4.addOrReplaceChild("leftArm_r1", CubeListBuilder.create().texOffs(46, 13).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.45F, -1.05F, 0.0F, 1.5708F, 0.0F, 0.8727F));

		PartDefinition under4 = leftArm.addOrReplaceChild("under4", CubeListBuilder.create().texOffs(24, 39).addBox(-1.0F, -0.3F, -1.0F, 2.0F, 6.6F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.7F, 0.0F));

		PartDefinition left_sheiled = under4.addOrReplaceChild("left_sheiled", CubeListBuilder.create().texOffs(2, 3).addBox(-1.0F, -0.4579F, -2.7669F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.4579F, -0.2331F));

		PartDefinition cube_r6 = left_sheiled.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 6).addBox(-3.0F, -0.5F, -9.5F, 6.0F, 1.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5421F, -3.2331F, -1.5708F, 0.0F, 0.0F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 11.8F, -1.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition upper2 = rightLeg.addOrReplaceChild("upper2", CubeListBuilder.create().texOffs(48, 45).addBox(-1.0F, 0.25F, -0.75F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 50).addBox(-1.0F, 3.25F, -0.75F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -0.25F, 0.1745F, 0.0F, 0.0F));

		PartDefinition under2 = rightLeg.addOrReplaceChild("under2", CubeListBuilder.create().texOffs(28, 50).addBox(-1.0F, -1.134F, 0.1667F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 48).addBox(-1.0F, 2.866F, 0.1667F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.6F, 0.0333F, -0.5236F, 0.0F, 0.0F));

		PartDefinition rightLeg_r1 = under2.addOrReplaceChild("rightLeg_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -1.0F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.166F, 1.6667F, 0.5236F, 0.0F, 0.0F));

		PartDefinition rightLeg2 = body.addOrReplaceChild("rightLeg2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 11.8F, -1.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition upper3 = rightLeg2.addOrReplaceChild("upper3", CubeListBuilder.create().texOffs(50, 0).addBox(-1.0F, 0.25F, -0.75F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 52).addBox(-1.0F, 3.25F, -0.75F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -0.25F, 0.1745F, 0.0F, 0.0F));

		PartDefinition under3 = rightLeg2.addOrReplaceChild("under3", CubeListBuilder.create().texOffs(34, 50).addBox(-1.0F, -1.134F, 0.1667F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 26).addBox(-1.0F, 2.866F, 0.1667F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.6F, 0.0333F, -0.5236F, 0.0F, 0.0F));

		PartDefinition rightLeg_r2 = under3.addOrReplaceChild("rightLeg_r2", CubeListBuilder.create().texOffs(10, 48).addBox(-3.0F, -1.0F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.166F, 1.6667F, 0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(The_VoidSteel_Vanguard entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.isdead)
		{
			return;
		}
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateHeadLookTarget(netHeadYaw, headPitch);
		this.animate(entity.idleAnimationState, The_Voidsteel_VanguardModelAnimation.idle, ageInTicks);
		this.animateWalk(The_Voidsteel_VanguardModelAnimation.dash, limbSwing, limbSwingAmount, 0.1F, 3.0F);
		this.animate(entity.attackAnimationState, The_Voidsteel_VanguardModelAnimation.sword_1, ageInTicks);
		this.animate(entity.sheiledAnimationState, The_Voidsteel_VanguardModelAnimation.shiled, ageInTicks);
		this.animate(entity.deadAnimationState, The_Voidsteel_VanguardModelAnimation.dead, ageInTicks);
		this.animate(entity.dashAnimationState, The_Voidsteel_VanguardModelAnimation.dash, ageInTicks);
	}

	private void animateHeadLookTarget(float yRot, float xRot) {
		this.head.xRot += xRot * ((float) Math.PI / 180F);
		this.head.yRot += yRot * ((float) Math.PI / 180F);
	}

	@Override
	public ModelPart root() {
		return this.waist; // あなたのモデルのルートパーツを返す
	}

}