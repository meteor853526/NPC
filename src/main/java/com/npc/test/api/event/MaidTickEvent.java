package com.npc.test.api.event;

import com.npc.test.passive.NpcEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class MaidTickEvent extends LivingEvent.LivingUpdateEvent {
    private final NpcEntity maid;

    public MaidTickEvent(NpcEntity maid) {
        super(maid);
        this.maid = maid;
    }

    public NpcEntity getMaid() {
        return maid;
    }
}
