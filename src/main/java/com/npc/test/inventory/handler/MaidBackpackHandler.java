package com.npc.test.inventory.handler;


import com.npc.test.passive.NpcEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MaidBackpackHandler extends ItemStackHandler {
    public MaidBackpackHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return NpcEntity.canInsertItem(stack);
    }
}
