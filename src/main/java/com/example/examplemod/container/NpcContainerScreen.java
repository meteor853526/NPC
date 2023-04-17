package com.example.examplemod.container;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NpcContainerScreen extends ContainerScreen<NpcContainer> {
    private ResourceLocation NPC_CONTAINER_RESOURCE = new ResourceLocation("examplemod", "textures/gui/container.png");
    private int textureWidth = 176;
    private int textureHeight = 166;

    @Override
    public void render(MatrixStack pose, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pose);
        super.render(pose, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(pose, mouseX, mouseY);
//        renderTooltip(pose, mouseX, mouseY);
    }

    public NpcContainerScreen(NpcContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
    }


//    drawGuiContainerForegroundLayer add pose
//    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack pose, int mouseX, int mouseY) {
        this.drawString(pose,this.font, Integer.toString(this.getContainer().getIntArray().get(0)), 82, 20, 0xeb0505);
    }

//    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack pose, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(pose);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(NPC_CONTAINER_RESOURCE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(pose, i, j, 0, 0, xSize, ySize, this.textureWidth, textureHeight);
    }

}