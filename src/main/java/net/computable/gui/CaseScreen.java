package net.computable.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.computable.ModMain;
import net.computable.networking.ModMessages;
import net.computable.networking.client.CaseButtonPacket;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CaseScreen extends AbstractContainerScreen<CaseMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MODID, "textures/gui/case.png");

    private ImageButton powerButton;

    public CaseScreen(CaseMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        powerButton = new ImageButton(0, 0, 18, 18, 176, 0, TEXTURE, button -> {
            ModMessages.sendToServer(new CaseButtonPacket(menu.entity.getBlockPos()));
        });
        addRenderableWidget(powerButton);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        powerButton.setPosition(x + 43, y + 34);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(pPoseStack, powerButton.x, powerButton.y, 194, menu.data.get(0) == 0 ? 0 : 18, 18, 18);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}
