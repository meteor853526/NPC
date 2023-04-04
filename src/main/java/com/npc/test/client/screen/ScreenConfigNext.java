/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.matrix.MatrixStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.SimpleSound
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.widget.TextFieldWidget
 *  net.minecraft.client.gui.widget.Widget
 *  net.minecraft.client.gui.widget.button.Button
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.SoundEvents
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.KeybindTextComponent
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.util.text.TranslationTextComponent
 */
package com.npc.test.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.npc.test.BubblesConfig;
import com.npc.test.util.ResetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.math.BigInteger;

public class ScreenConfigNext
extends Screen {
    private static final ResourceLocation ARROW_ICON = new ResourceLocation("comicsbubbleschat", "textures/left_arrow.png");
    private static final ResourceLocation ARROW_HOVER_ICON = new ResourceLocation("comicsbubbleschat", "textures/left_arrow_hover.png");
    private Button buttonSaveMaxStack;
    private Button buttonSaveMaxLine;
    private TextFieldWidget bubblesStackField;
    private TextFieldWidget bubblesLineWidthField;
    private int guiLeft;
    private int guiTop;
    private boolean buttonPrevHover;

    protected ScreenConfigNext() {
        super((ITextComponent)new TranslationTextComponent("text.config.title"));
    }

    public void init() {
        this.guiLeft = this.width / 2;
        this.guiTop = this.height / 2;
        TranslationTextComponent resetColor = new TranslationTextComponent("text.config.resetcolor");
        TranslationTextComponent resetDuration = new TranslationTextComponent("text.config.resetduration");
        TranslationTextComponent clearBubbles = new TranslationTextComponent("text.config.clearbubbles");
        TranslationTextComponent resetStack = new TranslationTextComponent("text.config.resetstack");
        TranslationTextComponent resetLineWidth = new TranslationTextComponent("text.config.resetlinewidth");
        Button buttonResetColor = new Button(this.guiLeft - 100, this.guiTop / 2 - 20, 200, 20, (ITextComponent)resetColor, p_onPress_1_ -> ResetUtil.resetColors());
        Button buttonResetDuration = new Button(this.guiLeft - 100, this.guiTop / 2, 200, 20, (ITextComponent)resetDuration, p_onPress_1_ -> ResetUtil.resetDuration());
        Button buttonResetStack = new Button(this.guiLeft - 100, this.guiTop / 2 + 20, 200, 20, (ITextComponent)resetStack, p_onPress_1_ -> {
            BubblesConfig.CLIENT.maxBubblesStack.set(0);
            this.bubblesStackField.insertText("0");
            this.buttonSaveMaxStack.active = false;
        });
        Button buttonResetLineWidth = new Button(this.guiLeft - 100, this.guiTop / 2 + 40, 200, 20, (ITextComponent)resetLineWidth, p_onPress_1_ -> {
            BubblesConfig.CLIENT.lineWidth.set(150);
            this.bubblesLineWidthField.insertText("150");
            this.bubblesLineWidthField.active = false;
        });
        Button buttonClearBubbles = new Button(this.guiLeft - 100, this.guiTop / 2 + 60, 200, 20, (ITextComponent)clearBubbles, p_onPress_1_ -> ResetUtil.clearBubbles());
        this.buttonSaveMaxStack = new Button(this.guiLeft + 104, this.guiTop / 2 + 100, 20, 20, (ITextComponent)new KeybindTextComponent((Object)TextFormatting.GREEN + "\u2714"), p_onPress_1_ -> {
            BubblesConfig.CLIENT.maxBubblesStack.set(Integer.parseInt(this.bubblesStackField.getValue()));
            this.buttonSaveMaxStack.active = false;
        });
        this.buttonSaveMaxLine = new Button(this.guiLeft + 104, this.guiTop / 2 + 140, 20, 20, (ITextComponent)new KeybindTextComponent((Object)TextFormatting.GREEN + "\u2714"), p_onPress_1_ -> {
            BubblesConfig.CLIENT.lineWidth.set(Integer.parseInt(this.bubblesLineWidthField.getValue()));
            this.buttonSaveMaxLine.active = false;
        });
        this.bubblesStackField = new TextFieldWidget(this.font, this.guiLeft - 99, this.guiTop / 2 + 100, 198, 20, ITextComponent.nullToEmpty(null));
        this.bubblesStackField.setFilter(this::isNumeric);
        this.bubblesStackField.setValue(String.valueOf(BubblesConfig.CLIENT.maxBubblesStack.get()));
        this.bubblesLineWidthField = new TextFieldWidget(this.font, this.guiLeft - 99, this.guiTop / 2 + 140, 198, 20, ITextComponent.nullToEmpty(null));
        this.bubblesLineWidthField.setFilter(this::isNumeric);
        this.bubblesLineWidthField.insertText(String.valueOf(BubblesConfig.CLIENT.lineWidth.get()));
        this.addButton((Widget)buttonResetColor);
        this.addButton((Widget)buttonResetDuration);
        this.addButton((Widget)buttonClearBubbles);
        this.addButton((Widget)buttonResetStack);
        this.addButton((Widget)buttonResetLineWidth);
        this.addButton((Widget)this.buttonSaveMaxStack);
        this.addButton((Widget)this.buttonSaveMaxLine);
        this.buttonSaveMaxStack.active = false;
        this.buttonSaveMaxLine.active = false;
        super.init();
    }

    public void tick() {
        this.bubblesLineWidthField.tick();
        this.bubblesStackField.tick();
        super.tick();
    }

    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        this.renderBackground(stack);
        int xButton = 0;
        int yButton = this.height - 30;
        boolean bl = this.buttonPrevHover = mouseX >= xButton && mouseY >= yButton && mouseX < xButton + 30 && mouseY < yButton + 30;
        if (this.buttonPrevHover) {
            this.minecraft.getTextureManager().bind(ARROW_HOVER_ICON);
        } else {
            this.minecraft.getTextureManager().bind(ARROW_ICON);
        }
        ScreenConfigNext.blit((MatrixStack)stack, (int)xButton, (int)yButton, (int)10, (float)0.0f, (float)0.0f, (int)32, (int)32, (int)32, (int)32);
        String title = I18n.get((String)"text.config.title", (Object[])new Object[0]);
        ScreenConfigNext.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)((Object)TextFormatting.UNDERLINE + title + " 2/2"), (int)(this.guiLeft - this.font.width(title) / 2 - 10), (int)(this.guiTop / 2 - 50), (int)Color.WHITE.getRGB());
        String maxBubblesText = I18n.get((String)"text.config.maxstackbubbles", (Object[])new Object[0]);
        ScreenConfigNext.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)maxBubblesText, (int)(this.guiLeft - this.font.width(maxBubblesText) / 2), (int)(this.guiTop / 2 + 85), (int)Color.WHITE.getRGB());
        String lengthLineMax = I18n.get((String)"text.config.maxlinewidth", (Object[])new Object[0]);
        ScreenConfigNext.drawString((MatrixStack)stack, (FontRenderer)this.font, (String)lengthLineMax, (int)(this.guiLeft - this.font.width(lengthLineMax) / 2), (int)(this.guiTop / 2 + 125), (int)Color.WHITE.getRGB());
        this.bubblesStackField.render(stack, mouseX, mouseY, p_230430_4_);
        this.bubblesLineWidthField.render(stack, mouseX, mouseY, p_230430_4_);
        super.render(stack, mouseX, mouseY, p_230430_4_);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.buttonPrevHover) {
            Minecraft.getInstance().setScreen((Screen)new ScreenConfig());
            Minecraft.getInstance().getSoundManager().play((ISound)SimpleSound.forUI((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
        if (mouseX >= (double)this.bubblesStackField.x && mouseY >= (double)this.bubblesStackField.y && mouseX < (double)(this.bubblesStackField.x + 198) && mouseY < (double)(this.bubblesStackField.y + 20)) {
            this.bubblesStackField.setFocus(true);
            this.bubblesLineWidthField.setFocus(false);
        }
        if (mouseX >= (double)this.bubblesLineWidthField.x && mouseY >= (double)this.bubblesLineWidthField.y && mouseX < (double)(this.bubblesLineWidthField.x + 198) && mouseY < (double)(this.bubblesLineWidthField.y + 20)) {
            this.bubblesLineWidthField.setFocus(true);
            this.bubblesStackField.setFocus(false);
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        String text;
        if (this.bubblesStackField.isFocused()) {
            this.bubblesStackField.charTyped(p_charTyped_1_, p_charTyped_2_);
            text = this.bubblesStackField.getValue();
            boolean bl = this.buttonSaveMaxStack.active = !text.isEmpty() && !this.bubblesStackField.getValue().equalsIgnoreCase(String.valueOf(BubblesConfig.CLIENT.maxBubblesStack.get()));
        }
        if (this.bubblesLineWidthField.isFocused()) {
            this.bubblesLineWidthField.charTyped(p_charTyped_1_, p_charTyped_2_);
            text = this.bubblesLineWidthField.getValue();
            this.buttonSaveMaxLine.active = !text.isEmpty() && Integer.parseInt(text) >= 50 && !this.bubblesLineWidthField.getValue().equalsIgnoreCase(String.valueOf(BubblesConfig.CLIENT.lineWidth.get()));
        }
        return super.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        String text;
        if (keyCode == 256) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        if (keyCode != 259) {
            return true;
        }
        if (this.bubblesStackField.isFocused()) {
            this.bubblesStackField.keyPressed(keyCode, scanCode, modifiers);
            text = this.bubblesStackField.getValue();
            boolean bl = this.buttonSaveMaxStack.active = !text.isEmpty() && !this.bubblesStackField.getValue().equalsIgnoreCase(String.valueOf(BubblesConfig.CLIENT.maxBubblesStack.get()));
        }
        if (this.bubblesLineWidthField.isFocused()) {
            text = this.bubblesLineWidthField.getValue();
            this.buttonSaveMaxLine.active = !text.isEmpty() && Integer.parseInt(text) >= 50 && !this.bubblesLineWidthField.getValue().equalsIgnoreCase(String.valueOf(BubblesConfig.CLIENT.lineWidth.get()));
            this.bubblesLineWidthField.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean func_231177_au__() {
        return false;
    }

    private boolean isNumeric(CharSequence cs) {
        if (cs.length() == 0 || cs.toString().equalsIgnoreCase("\b")) {
            return true;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; ++i) {
            if (Character.isDigit(cs.charAt(i))) continue;
            return false;
        }
        return new BigInteger(cs.toString()).compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) < 0;
    }
}

