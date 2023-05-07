package com.npc.test.network.message;


import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidConfigMessage {
    private final int id;
    private final boolean home;
    private final boolean pick;
    private final boolean ride;
    //private final MaidSchedule schedule;

//    public MaidConfigMessage(int id, boolean home, boolean pick, boolean ride, MaidSchedule schedule) {
//        this.id = id;
//        this.home = home;
//        this.pick = pick;
//        this.ride = ride;
//        this.schedule = schedule;
//    }
    public MaidConfigMessage(int id, boolean home, boolean pick, boolean ride) {
        this.id = id;
        this.home = home;
        this.pick = pick;
        this.ride = ride;
    }
    public static void encode(MaidConfigMessage message, PacketBuffer buf) {
        buf.writeInt(message.id);
        buf.writeBoolean(message.home);
        buf.writeBoolean(message.pick);
        buf.writeBoolean(message.ride);
        //buf.writeEnum(message.schedule);
    }

//    public static MaidConfigMessage decode(PacketBuffer buf) {
//        return new MaidConfigMessage(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readEnum(MaidSchedule.class));
//    }
    public static MaidConfigMessage decode(PacketBuffer buf) {
        return new MaidConfigMessage(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(MaidConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof NpcEntity && ((NpcEntity) entity).isOwnedBy(sender)) {
                    NpcEntity maid = (NpcEntity) entity;
                    //maid.setHomeModeEnable(message.home);
                    maid.setPickup(message.pick);
                    //maid.setRideable(message.ride);
                    if (maid.getVehicle() != null) {
                        maid.stopRiding();
                    }
//                    if (maid.getSchedule() != message.schedule) {
//                        maid.setSchedule(message.schedule);
//                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
