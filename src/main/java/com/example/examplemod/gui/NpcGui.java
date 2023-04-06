package com.example.examplemod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
//    ResourceLocation NPC_FIRST_GUI_TEXTURE = new ResourceLocation("examplemod", "textures/gui/gui.png");
    String content = "Hello";
    SliderPercentageOption sliderPercentageOption;
    Widget sliderBar;

    private ResourceLocation NPC_GUI_TEXTURE = new ResourceLocation("examplemod", "textures/gui/container.png");
    private int textureWidth = 176;
    private int textureHeight = 166;
    protected NpcGui(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    protected void init() {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 100, 66, 200, 20, ITextComponent.getTextComponentOrEmpty("Title"));
        this.children.add(this.textFieldWidget);

        this.button = new Button(this.width / 2 - 40, 96, 80, 20, ITextComponent.getTextComponentOrEmpty("save"), (button) -> {
        });
        this.addButton(button);

        this.sliderPercentageOption = new SliderPercentageOption("examplemod.sliderbar", 5, 100, 5, (setting) -> {
            return Double.valueOf(0);
        }, (setting, value) -> {
        }, (gameSettings, sliderPercentageOption1) -> ITextComponent.getTextComponentOrEmpty("test"));
        this.sliderBar = this.sliderPercentageOption.createWidget(Minecraft.getInstance().gameSettings, this.width / 2 - 100, 120, 200);
        this.children.add(this.sliderBar);

        super.init();
    }

    @Override
    public void render(MatrixStack pose, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(pose);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(NPC_GUI_TEXTURE);
        int textureWidth = 208;
        int textureHeight = 156;
        this.blit(pose,this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);
        this.drawString(pose, this.font, content, this.width / 2 - 10, 30, 0xeb0505);

        this.textFieldWidget.render(pose, mouseX, mouseY, partialTick);
        this.button.render(pose, mouseX, mouseY, partialTick);
        this.sliderBar.render(pose, mouseX, mouseY, partialTick);
        super.render(pose, mouseX, mouseY, partialTick);
    }
}