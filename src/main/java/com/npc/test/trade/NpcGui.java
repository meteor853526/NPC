package com.npc.test.trade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.npc.test.NpcTestMod;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.Inventory;

public class NpcGui extends ContainerScreen<NpcContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(NpcTestMod.MOD_ID,
            "textures/gui/trade_gui.png");
	Slot target, source;
	Inventory fakeInventory;
	public NpcGui(NpcContainer npcContainer, PlayerInventory playerInventory, ITextComponent title) {
	    super(npcContainer, playerInventory, title);
	    fakeInventory = new Inventory(2);
	    fakeInventory.addItem(npcContainer.source.copy());
	    fakeInventory.addItem(npcContainer.target.copy());
	    this.source = new Slot(fakeInventory, 0, 39, 21);
	    this.target = new Slot(fakeInventory, 1, 126, 21);
	    
	}
	public static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
    	this.renderBackground(stack);
    	super.render(stack, mouseX, mouseY, partialTicks);
    	// 交易物品預覽
    	RenderSystem.pushMatrix();
    	RenderSystem.translatef((float)this.leftPos, (float)this.topPos, 0.0F);
    	this.setBlitOffset(100);
    	this.itemRenderer.renderAndDecorateItem(source.getItem(), source.x, source.y);
    	this.itemRenderer.renderGuiItemDecorations(this.font, source.getItem(), source.x, source.y);
    	this.itemRenderer.renderAndDecorateItem(target.getItem(), target.x, target.y);
    	this.itemRenderer.renderGuiItemDecorations(this.font, target.getItem(), target.x, target.y);
    	this.setBlitOffset(0);
    	if(this.isHovering(source.x, source.y, 16, 16, mouseX, mouseY))
    		this.hoveredSlot = source;
    	if(this.isHovering(target.x, target.y, 16, 16, mouseX, mouseY))
    		this.hoveredSlot = target;
    	RenderSystem.popMatrix();
    	this.renderTooltip(stack, mouseX, mouseY);
    }
    
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		if (minecraft == null) {
			return;
		}
		RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(TEXTURE);
        int posX = (this.width - this.imageWidth) / 2;
        int posY = (this.height - this.imageHeight) / 2;

        blit(matrixStack, posX, posY, 0, 0, this.imageWidth, this.imageHeight);
	}
}