package net.computable.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.computable.ModMain;
import net.computable.block.CaseBlock;
import net.computable.block.entity.CaseBlockEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CaseBlockEntityRenderer implements BlockEntityRenderer<CaseBlockEntity> {
    private final ResourceLocation TEXTURE_POWER = new ResourceLocation(ModMain.MODID, "textures/block/case_power.png");
    private final ResourceLocation TEXTURE_INDICATOR = new ResourceLocation(ModMain.MODID, "textures/block/case_indicator.png");

    public CaseBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CaseBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.powered) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5f, 0.5f, 0.5f);
            switch (pBlockEntity.getBlockState().getValue(CaseBlock.FACING)) {
                case NORTH -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(0));
                case WEST -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                case SOUTH -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                case EAST -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
            }
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1, 1, 1, 1);
            {
                RenderSystem.setShaderTexture(0, TEXTURE_POWER);
                BufferBuilder builder = Tesselator.getInstance().getBuilder();
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(pPoseStack.last().pose(), 0.5f, 0.5f, -0.501f).uv(0f, 0f).endVertex();
                builder.vertex(pPoseStack.last().pose(), 0.5f, -0.5f, -0.501f).uv(0f, 1f).endVertex();
                builder.vertex(pPoseStack.last().pose(), -0.5f, -0.5f, -0.501f).uv(1f, 1f).endVertex();
                builder.vertex(pPoseStack.last().pose(), -0.5f, 0.5f, -0.501f).uv(1f, 0f).endVertex();
                BufferUploader.drawWithShader(builder.end());
            }
            if (pBlockEntity.indicator) {
                RenderSystem.setShaderTexture(0, TEXTURE_INDICATOR);
                BufferBuilder builder = Tesselator.getInstance().getBuilder();
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(pPoseStack.last().pose(), 0.5f, 0.5f, -0.501f).uv(0f, 0f).endVertex();
                builder.vertex(pPoseStack.last().pose(), 0.5f, -0.5f, -0.501f).uv(0f, 1f).endVertex();
                builder.vertex(pPoseStack.last().pose(), -0.5f, -0.5f, -0.501f).uv(1f, 1f).endVertex();
                builder.vertex(pPoseStack.last().pose(), -0.5f, 0.5f, -0.501f).uv(1f, 0f).endVertex();
                BufferUploader.drawWithShader(builder.end());
            }
            pPoseStack.popPose();
        }
    }
}
