/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.ServerPlayerEntity
 *  net.minecraft.util.RegistryKey
 *  net.minecraftforge.event.ServerChatEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.fml.network.PacketDistributor
 *  net.minecraftforge.fml.network.PacketDistributor$TargetPoint
 */
package com.npc.test.server;

import com.npc.test.config.BubblesConfig;
import com.npc.test.NpcTestMod;
import com.npc.test.packet.SCSendModPresent;
import com.npc.test.packet.SCSyncBubbleMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class BubblesServerEvent {
    @SubscribeEvent
    public static void onSeverChatEvent(ServerChatEvent event) {
        if (!((Boolean) BubblesConfig.SERVER.chatListener.get()).booleanValue()) {
            return;
        }
        ServerPlayerEntity player = event.getPlayer();
        long startTime = System.currentTimeMillis();
        String message = event.getMessage();
        double distance = (Double)BubblesConfig.SERVER.bubbleRange.get();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        NpcTestMod.channel.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p((double)x, (double)y, (double)z, (double)distance, player.getLevel().dimension())), (Object)new SCSyncBubbleMessage(startTime, message, player.getUUID()));
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        NpcTestMod.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getPlayer()), (Object)new SCSendModPresent((Boolean)BubblesConfig.SERVER.canThroughBlocks.get()));
    }
}

