package com.npc.test.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.npc.test.NpcTestMod;
import com.npc.test.client.renderer.entity.ChatBubbleRenderer;
import com.npc.test.client.renderer.entity.DeliveryChatBubbleRenderer;
import com.npc.test.client.resource.pojo.MaidModelInfo;
import com.npc.test.entity.model.DeliveryModel;
import com.npc.test.entity.model.NpcModel;
import com.npc.test.passive.DeliveryEntity;
import com.npc.test.passive.NpcEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;


public class DeliveryRenderer extends MobRenderer<DeliveryEntity, DeliveryModel<DeliveryEntity>>
{
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(NpcTestMod.MOD_ID, "textures/entity/entity_villager_farmer_png.png");
    private MaidModelInfo mainInfo;
    public DeliveryRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DeliveryModel<>(1F), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DeliveryEntity entity) {
        return TEXTURE;
    }
    @Override
    public void render(DeliveryEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
//        renderHomeTips(entity, matrixStackIn, bufferIn, packedLightIn);
//
//         //读取默认模型，用于清除不存在模型的缓存残留
//        CustomPackLoader.MAID_MODELS.getModel(DEFAULT_MODEL_ID).ifPresent(model -> this.model = model);
//        CustomPackLoader.MAID_MODELS.getInfo(DEFAULT_MODEL_ID).ifPresent(info -> this.mainInfo = info);
//        //CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).ifPresent(animations -> this.mainAnimations = animations);
//
//        MaidModels.ModelData eventModelData = new MaidModels.ModelData(model, mainInfo, mainAnimations);
//        if (MinecraftForge.EVENT_BUS.post(new RenderMaidEvent(entity, eventModelData))) {
//            this.model = eventModelData.getModel();
//            this.mainInfo = eventModelData.getInfo();
//            this.mainAnimations = eventModelData.getAnimations();
//        } else {
//            // 通过模型 id 获取对应数据
//            CustomPackLoader.MAID_MODELS.getModel(entity.getModelId()).ifPresent(model -> this.model = model);
//            CustomPackLoader.MAID_MODELS.getInfo(entity.getModelId()).ifPresent(info -> this.mainInfo = (MaidModelInfo) info);
//            CustomPackLoader.MAID_MODELS.getAnimation(entity.getModelId()).ifPresent(animations -> this.mainAnimations = animations);
//        }

        // 模型动画设置
        //this.model.setAnimations(this.mainAnimations);
        // 渲染聊天气泡
        //if (InGameMaidConfig.INSTANCE.isShowChatBubble()) {
        DeliveryChatBubbleRenderer.renderChatBubble(this, entity, matrixStackIn, bufferIn, packedLightIn);
        //}
        // 渲染女仆模型本体
        //GlWrapper.setMatrixStack(matrixStackIn);
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        //GlWrapper.clearMatrixStack();
    }

//    private void renderHomeTips(DeliveryEntity entity, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
//        Minecraft mc = Minecraft.getInstance();
//        PlayerEntity player = mc.player;
//        if (entity.hasRestriction() && player != null && extraCondition(entity, mc, player)) {
//            BlockPos restrictCenter = entity.getRestrictCenter();
//            Vector3f home = new Vector3f(restrictCenter.getX(), restrictCenter.getY(), restrictCenter.getZ());
//            home.add(0.5f, 0.25f, 0.5f);
//            home.add((float) -entity.getX(), (float) -entity.getY(), (float) -entity.getZ());
//
//            matrixStackIn.pushPose();
//            matrixStackIn.translate(home.x(), home.y() + 0.25, home.z());
//            matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
//            matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
//            Matrix4f pose = matrixStackIn.last().pose();
//            FontRenderer fontrenderer = this.getFont();
//            String text = String.format("[%d, %d, %d]", restrictCenter.getX(), restrictCenter.getY(), restrictCenter.getZ());
//            float x = (float) (-fontrenderer.width(text) / 2);
//            fontrenderer.drawInBatch(text, x, 0, -1, false, pose, bufferIn, false, 0, packedLightIn);
//            matrixStackIn.popPose();
//
//
//            IVertexBuilder buffer = bufferIn.getBuffer(RenderType.lines());
//            Matrix4f matrix4f = matrixStackIn.last().pose();
//            buffer.vertex(matrix4f, 0, 0.5f, 0).color(0xff, 0x00, 0x00, 0xff).endVertex();
//            buffer.vertex(matrix4f, home.x(), home.y(), home.z()).color(0xff, 0x00, 0x00, 0xff).endVertex();
//        }
//    }
    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }
}
