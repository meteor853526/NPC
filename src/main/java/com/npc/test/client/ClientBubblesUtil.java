/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.matrix.MatrixStack
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.AbstractGui
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.IRenderTypeBuffer
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.text.ITextProperties
 *  net.minecraft.util.text.Style
 *  net.minecraftforge.client.event.RenderLivingEvent
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 */
package com.npc.test.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.npc.test.BubblesConfig;
import com.npc.test.util.Bubble;
import com.npc.test.util.Message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientBubblesUtil {
    public static KeyBinding[] keyBindings = new KeyBinding[1];
    public static final HashMap<UUID, Bubble> BUBBLES_SYNC = new HashMap();
    public static boolean serverSupport = false;
    public static boolean bubbleThroughBlocks;

    public static void registerBindings() {
        ClientBubblesUtil.keyBindings[0] = new KeyBinding("key.openoptions.desc", 66, "key.comicsbubbleschat.category");
        for (KeyBinding keyBinding : keyBindings) {
            ClientRegistry.registerKeyBinding((KeyBinding)keyBinding);
        }
    }

    public static void draw(Message message, ArrayDeque<Message> messages, RenderLivingEvent<?, ?> event, long endTime, float x, float y, float z, boolean isSneaking, boolean drawBubble, boolean drawText, boolean drawLittleBubbles, int bubbleQueue) {
        Color outlineColor = message.getColorOutline().getColor();
        Color insideColor = message.getColorInside().getColor();
        Color textColor = message.getColorText().getColor();
        long timeLeft = endTime - System.currentTimeMillis();
        if (timeLeft < 0L) {
            throw new IllegalStateException("timeleft cannot be < 0, contact author if you didn't use api");
        }
        if (timeLeft < 3000L) {
            message.getColorOutline().setAlpha((int)timeLeft * (Integer)((List) BubblesConfig.CLIENT.colorOutline.get()).get(3) / 3000);
            message.getColorInside().setAlpha((int)timeLeft * (Integer)((List)BubblesConfig.CLIENT.colorInside.get()).get(3) / 3000);
            message.getColorText().setAlpha((int)timeLeft * (Integer)((List)BubblesConfig.CLIENT.colorText.get()).get(3) / 3000);
        }
        float size = -0.02f;
        MatrixStack stack = event.getMatrixStack();
        IRenderTypeBuffer buffers = event.getBuffers();
        LivingEntity entityIn = event.getEntity();
        float f = entityIn.getEyeHeight() + 0.5f;
        stack.pushPose();
        stack.translate(0.0, (double)f, 0.0);
        //stack.last(Minecraft.getInstance().gameRenderer.getResourceType().); //?????????????????????????
        stack.scale(size, size, size);
        if (isSneaking || !bubbleThroughBlocks) {
            RenderSystem.enableDepthTest();
        }
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        List<String> lines = fontrenderer.getSplitter().splitLines(message.getMessage(), ((Integer)BubblesConfig.CLIENT.lineWidth.get()).intValue(), Style.EMPTY).stream().map(ITextProperties::getString).collect(Collectors.toList());
        int linesHeight = 10 * lines.size();
        int biggestLineWidth = 0;
        for (String line : lines) {
            int lineWidth = fontrenderer.width(line);
            if (biggestLineWidth >= lineWidth) continue;
            biggestLineWidth = lineWidth;
        }
        float yTranslate = lines.size() + 15;
        int i2 = biggestLineWidth / 2;
        stack.translate((double)(i2 + 20), (double)(-yTranslate), 0.0);
        float scale = 1.0f;
        stack.scale(scale, scale, scale);
        stack.translate(0.0, (double)((float)(-linesHeight) / 2.0f - 15.0f), 0.0);
        if (drawBubble) {
            int distanceBetween = -10;
            int j2 = 0;
            for (Message value : messages) {
                if (j2 == bubbleQueue + 1) break;
                String message2 = value.getMessage();
                List<String> messageLines = fontrenderer.getSplitter().splitLines(message2, (Integer) BubblesConfig.CLIENT.lineWidth.get(), Style.EMPTY).stream().map(ITextProperties::getString).collect(Collectors.toList());
                if (j2 != bubbleQueue) {
                    distanceBetween += messageLines.size() * 10 + 4;
                } else {
                    distanceBetween -= messageLines.size();
                }
                ++j2;
            }
            stack.translate(0.0, (double)(-distanceBetween), 0.0);
            ClientBubblesUtil.drawBubble(i2, -linesHeight / 2, outlineColor.getRGB(), insideColor.getRGB(), stack);
        }
        if (drawLittleBubbles) {
            ClientBubblesUtil.drawLittleBubble(i2, -linesHeight / 2, outlineColor.getRGB(), insideColor.getRGB(), stack);
            ClientBubblesUtil.drawMediumBubble(i2, -linesHeight / 2, outlineColor.getRGB(), insideColor.getRGB(), stack);
        }
        if (drawText) {
            stack.translate(0.0, 0.0, (double)0.1f);
            if (textColor.getAlpha() > 3) {
                boolean flag = !event.getEntity().isDiscrete();
                int lineY = -linesHeight / 2 + 3;
                for (String line : lines) {
                    fontrenderer.drawInBatch(line,(-fontrenderer.width(line)) / 2.0f,(float)lineY,textColor.getRGB(),false,stack.last().pose(),buffers,flag, 0, event.getLight()); /////
                    fontrenderer.getClass();
                    lineY = lineY + 9 + 1;
                }
            }
        }
        stack.popPose();
    }

    public static void fill(MatrixStack matrixStack, int p_fill_0_, int p_fill_1_, int p_fill_2_, int p_fill_3_, int p_fill_4_) {
        AbstractGui.fill((MatrixStack)matrixStack, (int)p_fill_0_, (int)p_fill_1_, (int)p_fill_2_, (int)p_fill_3_, (int)p_fill_4_);
    }

    public static void drawBubble(int i, int size, int outlineColor, int insideColor, MatrixStack matrix) {
        ClientBubblesUtil.fill(matrix, -i - 1, size + 1, i + 1, size + 2, outlineColor);
        ClientBubblesUtil.fill(matrix, -i - 1, -size + 4, i + 1, -size + 5, outlineColor);
        ClientBubblesUtil.fill(matrix, -i - 1, size + 2, -i - 2, size + 3, outlineColor);
        ClientBubblesUtil.fill(matrix, -i - 1, -size + 4, -i - 2, -size + 3, outlineColor);
        ClientBubblesUtil.fill(matrix, -i - 3, size + 3, -i - 2, -size + 3, outlineColor);
        ClientBubblesUtil.fill(matrix, i + 3, size + 3, i + 2, -size + 3, outlineColor);
        ClientBubblesUtil.fill(matrix, i + 1, -size + 4, i + 2, -size + 3, outlineColor);
        ClientBubblesUtil.fill(matrix, i + 1, size + 2, i + 2, size + 3, outlineColor);
        ClientBubblesUtil.fill(matrix, -i - 1, size + 2, i + 1, -size + 4, insideColor);
        ClientBubblesUtil.fill(matrix, -i - 2, size + 3, -i - 1, -size + 3, insideColor);
        ClientBubblesUtil.fill(matrix, i + 2, size + 3, i + 1, -size + 3, insideColor);
    }

    public static void drawMediumBubble(int i, int size, int outlineColor, int insideColor, MatrixStack stack) {
        ClientBubblesUtil.fill(stack, -i + 1, -size + 11, -i - 6, -size + 10, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 2, -size + 12, -i + 1, -size + 11, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 2, -size + 18, -i + 1, -size + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i - 7, -size + 12, -i - 6, -size + 11, outlineColor);
        ClientBubblesUtil.fill(stack, -i - 7, -size + 18, -i - 6, -size + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 1, -size + 19, -i - 6, -size + 18, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 2, -size + 12, -i + 3, -size + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i - 7, -size + 12, -i - 8, -size + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 1, -size + 11, -i - 6, -size + 18, insideColor);
        ClientBubblesUtil.fill(stack, -i - 7, -size + 12, -i - 6, -size + 17, insideColor);
        ClientBubblesUtil.fill(stack, -i + 1, -size + 12, -i + 2, -size + 17, insideColor);
    }

    public static void drawLittleBubble(int i, int size, int outlineColor, int insideColor, MatrixStack stack) {
        ClientBubblesUtil.fill(stack, -i + 1 - 11, -size + 11 + 17, -i - 6 - 9, -size + 10 + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 2 - 11, -size + 12 + 17, -i - 10, -size + 11 + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 2 - 11, -size + 18 + 16, -i - 10, -size + 17 + 16, outlineColor);
        ClientBubblesUtil.fill(stack, -i - 7 - 8, -size + 12 + 17, -i - 6 - 10, -size + 11 + 17, outlineColor);
        ClientBubblesUtil.fill(stack, -i - 7 - 9, -size + 18 + 16, -i - 5 - 10, -size + 17 + 16, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 1 - 11, -size + 19 + 16, -i - 5 - 10, -size + 18 + 16, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 2 - 11, -size + 12 + 17, -i + 2 - 10, -size + 17 + 16, outlineColor);
        ClientBubblesUtil.fill(stack, -i - 6 - 11, -size + 12 + 17, -i - 6 - 10, -size + 17 + 16, outlineColor);
        ClientBubblesUtil.fill(stack, -i + 1 - 11, -size + 11 + 17, -i - 6 - 9, -size + 18 + 16, insideColor);
        ClientBubblesUtil.fill(stack, -i - 7 - 9, -size + 12 + 17, -i - 6 - 9, -size + 17 + 16, insideColor);
        ClientBubblesUtil.fill(stack, -i + 1 - 11, -size + 12 + 17, -i + 2 - 11, -size + 17 + 16, insideColor);
    }
}

