/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.blaze3d.matrix.MatrixStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.SimpleSound
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.SliderPercentageOption
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.SoundEvents
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.KeybindTextComponent
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.util.text.TranslationTextComponent
 *  org.lwjgl.opengl.GL11
 */
package com.npc.test.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.npc.test.BubblesConfig;
import com.npc.test.client.ClientBubblesUtil;
import com.npc.test.util.SpecColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class ScreenConfig
extends Screen {
    private static final ResourceLocation ARROW_ICON = new ResourceLocation("comicsbubbleschat", "textures/right_arrow.png");
    private static final ResourceLocation ARROW_HOVER_ICON = new ResourceLocation("comicsbubbleschat", "textures/right_arrow_hover.png");
    private SpecColor outlineColorSpec;
    private SpecColor insideColorSpec;
    private SpecColor textColorSpec;
    private final String[] stateValues = new String[]{I18n.get("text.red", (Object[])new Object[0]), I18n.get((String)"text.green", (Object[])new Object[0]), I18n.get((String)"text.blue", (Object[])new Object[0]), I18n.get((String)"text.alpha", (Object[])new Object[0])};

    private int guiLeft;
    private int guiTop;
    private boolean buttonNextHover;
    private Integer[] outlineValues;
    private Integer[] insideValues;
    private Integer[] textValues;
    private int durationBubbles;

    public ScreenConfig() {
        super((ITextComponent)new TranslationTextComponent("text.config.title"));
    }

    public void init() {
        this.guiLeft = this.guiLeft / 2;
        this.guiTop = this.guiTop / 2;
        this.outlineColorSpec = new SpecColor((List) BubblesConfig.CLIENT.colorOutline.get());
        this.insideColorSpec = new SpecColor((List)BubblesConfig.CLIENT.colorInside.get());
        this.textColorSpec = new SpecColor((List)BubblesConfig.CLIENT.colorText.get());
        this.durationBubbles = (Integer)BubblesConfig.CLIENT.durationBubbles.get();
        this.initSliders();
        super.init();
    }

    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        this.renderBackground(stack);
        int xButton = this.width - 30;
        int yButton = this.guiTop * 2 - 30;
        boolean bl = this.buttonNextHover = mouseX >= xButton && mouseY >= yButton && mouseX < xButton + 30 && mouseY < yButton + 30;
        if (this.buttonNextHover) {
            this.minecraft.getTextureManager().bind(ARROW_HOVER_ICON);
        } else {
            this.minecraft.getTextureManager().bind(ARROW_ICON);
        }
        ScreenConfig.blit((MatrixStack)stack, (int)xButton, (int)yButton, (int)10, (float)0.0f, (float)0.0f, (int)32, (int)32, (int)32, (int)32);
        Color outlineColor = new SpecColor(this.outlineValues).getColor();
        Color insideColor = new SpecColor(this.insideValues).getColor();
        Color textColor = new SpecColor(this.textValues).getColor();
        ScreenConfig.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)((Object)TextFormatting.UNDERLINE + this.title.getString() + " 1/2"), (int)(this.guiLeft - this.font.width(this.title.getString()) / 2 - 10), (int)(this.guiTop / 2 - 50), (int)Color.WHITE.getRGB());
        GL11.glPushMatrix();
        GL11.glScalef((float)0.95f, (float)0.95f, (float)0.95f);
        String outlineTitle = I18n.get((String)"text.config.outline", (Object[])new Object[0]);
        ScreenConfig.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)((Object)TextFormatting.UNDERLINE + outlineTitle), (int)(this.guiLeft - this.font.width(outlineTitle) / 2 - 140), (int)(this.guiTop / 2 + 20), (int)Color.WHITE.getRGB());
        String insideTitle = I18n.get((String)"text.config.inside", (Object[])new Object[0]);
        ScreenConfig.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)((Object)TextFormatting.UNDERLINE + insideTitle), (int)(this.guiLeft - this.font.width(insideTitle) / 2 + 10), (int)(this.guiTop / 2 + 20), (int)Color.WHITE.getRGB());
        String textTitle = I18n.get((String)"text.config.text", (Object[])new Object[0]);
        ScreenConfig.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)((Object)TextFormatting.UNDERLINE + textTitle), (int)(this.guiLeft - this.font.width(textTitle) / 2 + 160), (int)(this.guiTop / 2 + 20), (int)Color.WHITE.getRGB());
        GL11.glPopMatrix();
        ScreenConfig.fill((MatrixStack)stack, (int)(this.guiLeft - 210), (int)(this.guiTop / 2 + 130), (int)(this.guiLeft - 100 + 25), (int)(this.guiTop / 2 + 140), (int)outlineColor.getRGB());
        ScreenConfig.fill((MatrixStack)stack, (int)(this.guiLeft - 70), (int)(this.guiTop / 2 + 130), (int)(this.guiLeft + 20 + 45), (int)(this.guiTop / 2 + 140), (int)insideColor.getRGB());
        ScreenConfig.fill((MatrixStack)stack, (int)(this.guiLeft + 40 + 30), (int)(this.guiTop / 2 + 130), (int)(this.guiLeft + 140 + 65), (int)(this.guiTop / 2 + 140), (int)textColor.getRGB());
        GL11.glPushMatrix();
        GL11.glTranslatef((float)this.guiLeft, (float)((float)this.guiTop / 2.0f - 30.0f), (float)0.0f);
        GL11.glScalef((float)0.9f, (float)0.9f, (float)0.9f);
        ClientBubblesUtil.drawBubble(50, -10, outlineColor.getRGB(), insideColor.getRGB(), stack);
        ClientBubblesUtil.drawMediumBubble(50, -10, outlineColor.getRGB(), insideColor.getRGB(), stack);
        ClientBubblesUtil.drawLittleBubble(50, -10, outlineColor.getRGB(), insideColor.getRGB(), stack);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        this.font.draw(stack, I18n.get((String)"text.config.test", (Object[])new Object[0]), (float)((int)((float)(-this.font.width(I18n.get((String)"text.config.test", (Object[])new Object[0]))) / 2.0f)), 0.0f, textColor.getRGB());
        GL11.glDisable((int)3042);
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glPopMatrix();
        if (!ClientBubblesUtil.serverSupport && !Minecraft.getInstance().hasSingleplayerServer()) {
            String noLinked = I18n.get((String)"text.config.nolinked", (Object[])new Object[0]);
            this.font.draw(stack, noLinked, (float)((int)((float)this.guiLeft - (float)this.font.width(noLinked) / 2.0f)), (float)(this.guiTop * 2 - 10), Color.RED.getRGB());
        }
        super.render(stack, mouseX, mouseY, p_230430_4_);
    }

    private void initSliders() {
        this.outlineValues = this.outlineColorSpec.getValues();
        this.insideValues = this.insideColorSpec.getValues();
        this.textValues = this.textColorSpec.getValues();
        for (int i = 0; i < 4; ++i) {
            int finalI = i;
            SliderPercentageOption outlineSlider = new SliderPercentageOption("outlineslider" + i, 0.0, 255.0, 1.0f, gameSettings -> (double)this.outlineValues[finalI], (gameSettings, aDouble) -> {
                this.outlineValues[finalI] = (int)Math.round(aDouble);
            }, (gameSettings, sliderPercentageOption) -> {
                int value = (int)Math.round(sliderPercentageOption.get(gameSettings));
                this.outlineValues[finalI] = value;
                return new KeybindTextComponent(this.stateValues[finalI] + " : " + Math.round(sliderPercentageOption.get(gameSettings)));
            });
            this.addButton(outlineSlider.createButton(Minecraft.getInstance().options, this.guiLeft - 170 - 40, this.guiTop / 2 + 40 + finalI * 20, 135));
            SliderPercentageOption insideSlider = new SliderPercentageOption("insideslider" + i, 0.0, 255.0, 1.0f, gameSettings -> (double)this.insideValues[finalI], (gameSettings, aDouble) -> {
                this.insideValues[finalI] = (int)Math.round(aDouble);
            }, (gameSettings, sliderPercentageOption) -> {
                int value = (int)Math.round(sliderPercentageOption.get(gameSettings));
                this.insideValues[finalI] = value;
                return new KeybindTextComponent(this.stateValues[finalI] + " : " + Math.round(sliderPercentageOption.get(gameSettings)));
            });
            this.addButton(insideSlider.createButton(Minecraft.getInstance().options, this.guiLeft - 170 + 100, this.guiTop / 2 + 40 + finalI * 20, 135));
            SliderPercentageOption textSlider = new SliderPercentageOption("textslider" + i, 0.0, 255.0, 1.0f, gameSettings -> (double)this.textValues[finalI], (gameSettings, aDouble) -> {
                this.textValues[finalI] = (int)Math.round(aDouble);
            }, (gameSettings, sliderPercentageOption) -> {
                int value = (int)Math.round(sliderPercentageOption.get(gameSettings));
                this.textValues[finalI] = value;
                return new KeybindTextComponent(this.stateValues[finalI] + " : " + Math.round(sliderPercentageOption.get(gameSettings)));
            });
            this.addButton(textSlider.createButton(Minecraft.getInstance().options, this.guiLeft - 170 + 240, this.guiTop / 2 + 40 + finalI * 20, 135));
        }
        SliderPercentageOption durationSlider = new SliderPercentageOption("durationslider", 0.0, 60.0, 1.0f, gameSettings -> (double) this.durationBubbles, (gameSettings, aDouble) -> {
            this.durationBubbles = (int)Math.round(aDouble);
        }, (gameSettings, sliderPercentageOption) -> {
            int value;
            this.durationBubbles = value = (int)Math.round(sliderPercentageOption.get(gameSettings));
            return new KeybindTextComponent(I18n.get((String)"text.config.duration.prefix", (Object[])new Object[0]) + " : " + value + " " + I18n.get((String)"text.config.duration.suffix", (Object[])new Object[0]));
        });
        this.addButton(durationSlider.createButton(Minecraft.getInstance().options, this.guiLeft - 170 - 40 + 100, this.guiTop / 2 + 145, 200));
    }

    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (this.buttonNextHover) {
            this.configSave();
            Minecraft.getInstance().setScreen((Screen)new ScreenConfigNext());
            Minecraft.getInstance().getSoundManager().play((ISound)SimpleSound.forUI((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    public void onClose() {
        this.configSave();
        super.onClose();
    }

    public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
        this.configSave();
        super.resize(p_231152_1_, p_231152_2_, p_231152_3_);
    }

    public boolean func_231177_au__() {
        return false;
    }

    private void configSave() {
        BubblesConfig.CLIENT.colorOutline.set(Lists.newArrayList(this.outlineValues));
        BubblesConfig.CLIENT.colorInside.set(Lists.newArrayList(this.insideValues));
        BubblesConfig.CLIENT.colorText.set(Lists.newArrayList(this.textValues));
        BubblesConfig.CLIENT.durationBubbles.set(this.durationBubbles);
    }
}

