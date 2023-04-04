/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.PacketBuffer
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.fml.network.NetworkEvent$Context
 */
package com.npc.test.packet;


import com.npc.test.BubblesConfig;
import com.npc.test.client.ClientBubblesUtil;
import com.npc.test.util.Bubble;
import com.npc.test.util.Message;
import com.npc.test.util.SpecColor;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.codec.binary.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SCSyncBubbleMessage {
    private long startTime;
    private String text;
    private UUID uuid;

    public SCSyncBubbleMessage(long startTime, String text, UUID uuid) {
        this.startTime = startTime;
        this.text = text;
        this.uuid = uuid;
    }

    public SCSyncBubbleMessage() {
    }

    public static void encode(SCSyncBubbleMessage message, PacketBuffer buf) {
        buf.writeLong(message.startTime);
        buf.writeUtf(message.text);////////////
        buf.writeUUID(message.uuid);
    }

    public static SCSyncBubbleMessage decode(PacketBuffer buf) {
        SCSyncBubbleMessage message = new SCSyncBubbleMessage();
        message.startTime = buf.readLong();
        message.text = buf.readUtf(32767);
        message.uuid = buf.readUUID();
        return message;
    }

    public static void handle(SCSyncBubbleMessage packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> SCSyncBubbleMessage.handleClient(packet));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(value=Dist.CLIENT)
    private static void handleClient(SCSyncBubbleMessage packet) {
        Bubble bubble = ClientBubblesUtil.BUBBLES_SYNC.get(packet.uuid);
        if (bubble == null) {
            bubble = new Bubble(packet.startTime, packet.text);
            ClientBubblesUtil.BUBBLES_SYNC.put(packet.uuid, bubble);
        } else {
            if (bubble.getMessages().size() == ((Integer) BubblesConfig.CLIENT.maxBubblesStack.get()).intValue()) {
                bubble.getMessages().removeLast();
            }
            bubble.getMessages().addFirst(new Message(packet.startTime, packet.text, new SpecColor((List)BubblesConfig.CLIENT.colorOutline.get()), new SpecColor((List)BubblesConfig.CLIENT.colorInside.get()), new SpecColor((List)BubblesConfig.CLIENT.colorText.get())));
        }
    }
}

