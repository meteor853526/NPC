package com.npc.test.inventory.container;


import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

import javax.annotation.Nullable;

public abstract class AbstractMaidContainer extends Container {
    protected final NpcEntity maid;

    public AbstractMaidContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, int entityId) {
        super(type, id);
        this.maid = (NpcEntity) inventory.player.level.getEntity(entityId);
        if (maid != null) {
            this.addPlayerInv(inventory);
            maid.guiOpening = true;
        }
    }

    private void addPlayerInv(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 88 + col * 18, 174 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 88 + col * 18, 232));
        }
    }

    public NpcEntity getMaid() {
        return maid;
    }

    @Override
    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        maid.guiOpening = false;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }
}
