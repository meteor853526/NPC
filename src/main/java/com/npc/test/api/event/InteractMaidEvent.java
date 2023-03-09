package com.npc.test.api.event;

import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;


@Cancelable
public class InteractMaidEvent extends Event {
    private final World world;
    private final PlayerEntity player;
    private final NpcEntity maid;
    private final ItemStack stack;

    public InteractMaidEvent(PlayerEntity player, NpcEntity maid, ItemStack stack) {
        this.player = player;
        this.world = player.level;
        this.maid = maid;
        this.stack = stack;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public NpcEntity getMaid() {
        return maid;
    }

    public ItemStack getStack() {
        return stack;
    }

    public World getWorld() {
        return world;
    }
}
