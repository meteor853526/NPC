/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.renderer.entity.model.PlayerModel
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.ClientPlayerNetworkEvent$LoggedOutEvent
 *  net.minecraftforge.client.event.InputEvent$KeyInputEvent
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.npc.test.client;

import com.npc.test.config.BubblesConfig;
import com.npc.test.client.screen.ScreenConfig;
import com.npc.test.util.Bubble;

import com.npc.test.util.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
public class BubblesClientEvent {
    @SubscribeEvent
    public static void bubblesRenderEvent(RenderLivingEvent.Pre<PlayerEntity, PlayerModel<PlayerEntity>> event) {
        UUID uuid = event.getEntity().getUUID();
        if (!ClientBubblesUtil.BUBBLES_SYNC.containsKey(uuid)) {
            return;
        }
        Bubble bubble = ClientBubblesUtil.BUBBLES_SYNC.get(uuid);
        long endTime = bubble.getMessages().getFirst().getStartTime() + TimeUnit.SECONDS.toMillis(((Integer) BubblesConfig.CLIENT.durationBubbles.get()).intValue());
        if (endTime < System.currentTimeMillis()) {
            ClientBubblesUtil.BUBBLES_SYNC.remove(event.getEntity().getUUID());
            return;
        }
        LivingEntity entity = event.getEntity();
        float offsetX = (float)entity.getX();
        float offsetY = (float)entity.getY() + 2.5f;
        float offsetZ = (float)entity.getZ();
        int k = 0;
        for (Message message : new ArrayDeque<Message>(bubble.getMessages())) {
            long endTime2 = message.getStartTime() + TimeUnit.SECONDS.toMillis(((Integer)BubblesConfig.CLIENT.durationBubbles.get()).intValue());
            if (endTime2 < System.currentTimeMillis()) {
                bubble.getMessages().remove(message);
                continue;
            }
            ClientBubblesUtil.draw(message, bubble.getMessages(), event, endTime2, offsetX, offsetY + 0.2f, offsetZ, event.getEntity().isDiscrete(), true, true, k == 0, k);
            ++k;
        }
    }

    @SubscribeEvent
    public static void onPress(InputEvent.KeyInputEvent event) {
        KeyBinding[] keyBindings = ClientBubblesUtil.keyBindings;
        if (keyBindings[0].isDown()) {
            Minecraft.getInstance().setScreen((Screen)new ScreenConfig());
        }
    }

    @SubscribeEvent
    public static void onServerQuit(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        ClientBubblesUtil.serverSupport = false;
    }
}

