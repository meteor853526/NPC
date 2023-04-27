package com.npc.test.trade;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.npc.test.NpcTestMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NpcGui extends Screen {
    TextFieldWidget textFieldWidget;
    Button button;
    OptionSlider optionSlider;
    ResourceLocation NPC_TEXTURE = new ResourceLocation(NpcTestMod.MOD_ID,"textures/gui/container.png");
    String content = "Hello";
    SliderPercentageOption sliderPercentageOption;
    Widget sliderBar;

    protected NpcGui(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    protected void init() {
//        keyboardListener.enableRepeatEvents => keyboardHandler.setSendRepeatsToGui
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 100, 66, 200, 20, ITextComponent.nullToEmpty("title"));
        this.children.add(this.textFieldWidget);
        this.button = new Button(this.width / 2 - 40, 96, 80, 20, ITextComponent.nullToEmpty("save"),(button) -> {
        });
        this.addButton(button);

        this.sliderPercentageOption = new SliderPercentageOption("npctestmod.sliderbar", 5, 100, 5, (setting) -> {
            return Double.valueOf(0);
        }, (setting, value) -> {
        }, (gameSettings, sliderPercentageOption1) -> ITextComponent.nullToEmpty("test"));
        this.sliderBar = this.sliderPercentageOption.createButton(Minecraft.getInstance().options, this.width / 2 - 100, 120, 200);
        this.children.add(this.sliderBar);

        super.init();
    }

    @Override
    public void render(MatrixStack pose, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(pose);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(NPC_TEXTURE);
        int textureWidth = 208;
        int textureHeight = 156;
        this.blit(pose,this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);
        this.drawString(pose,this.font, content, this.width / 2 - 10, 30, 0xeb0505);

        this.textFieldWidget.render(pose,mouseX, mouseY, partialTick);
        this.button.render(pose,mouseX, mouseY, partialTick);
        this.sliderBar.render(pose,mouseX, mouseY, partialTick);
        super.render(pose,mouseX, mouseY, partialTick);
    }
}