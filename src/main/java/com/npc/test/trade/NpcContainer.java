package com.npc.test.trade;

import com.npc.test.container.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class NpcContainer extends Container {
    private NpcInventory inventory;
    ItemStack target, source;
    
    public NpcContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new NpcInventory(buffer.readItem(), buffer.readItem()));
        
    }
    
    public NpcContainer(int id, PlayerInventory playerInventory, ItemStack target, ItemStack source) {
        this(id, playerInventory, new NpcInventory(target, source));
    }

    public NpcContainer(int id, PlayerInventory playerInventory, NpcInventory inventory) {
    	super(ModContainerTypes.NPC.get(), id);
        this.inventory = inventory;
        this.source = inventory.source;
        this.target= inventory.target;
        
        this.addSlot(new Slot(this.inventory, 0, 39, 49));
        this.addSlot(new Slot(this.inventory, 1, 66, 49));
        this.addSlot(new Slot(this.inventory, 2, 126, 49) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
            
            @Override
            public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
                this.checkTakeAchievements(p_190901_2_);
                ItemStack itemstack = NpcContainer.this.inventory.getItem(0);
                ItemStack itemstack1 = NpcContainer.this.inventory.getItem(1);
                // 交易完成後扣除付款邏輯
                NpcContainer.this.setItem(0, ItemStack.EMPTY);
                NpcContainer.this.setItem(1, ItemStack.EMPTY);
                return p_190901_2_;
             }
        });
        // 玩家背包
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = x + y * 9 + 9;
                int posX = 7 + x * 18;
                int posY = 85 + y * 18;
                this.addSlot(new Slot(playerInventory, index, posX, posY));
            }
        }

        // 玩家手持物品欄
        for (int x = 0; x < 9; ++x) {
            int index = x;
            int posX = 7 + x * 18;
            int posY = 143;
            this.addSlot(new Slot(playerInventory, index, posX, posY));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.inventory.stillValid(player);
    }
    
    @Override
    public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_) {
       return false;
    }
    

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
    	ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
           ItemStack itemstack1 = slot.getItem();
           itemstack = itemstack1.copy();
           if (index == 2) {
              if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                 return ItemStack.EMPTY;
              }
              slot.onQuickCraft(itemstack1, itemstack);
           } else if (index != 0 && index != 1) {
              if (index >= 3 && index < 30) {
                 if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                    return ItemStack.EMPTY;
                 }
              } else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                 return ItemStack.EMPTY;
              }
           } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
              return ItemStack.EMPTY;
           }

           if (itemstack1.isEmpty()) {
              slot.set(ItemStack.EMPTY);
           } else {
              slot.setChanged();
           }

           if (itemstack1.getCount() == itemstack.getCount()) {
              return ItemStack.EMPTY;
           }

           slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
    
    @Override
    public void slotsChanged(IInventory p_75130_1_) {
    	this.inventory.updateSellItem();
        super.slotsChanged(p_75130_1_);
     }
    
    @Override
    public void removed(PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        if (!p_75134_1_.level.isClientSide) {
           if (!p_75134_1_.isAlive() || p_75134_1_ instanceof ServerPlayerEntity && ((ServerPlayerEntity)p_75134_1_).hasDisconnected()) {
              ItemStack itemstack = this.inventory.removeItemNoUpdate(0);
              if (!itemstack.isEmpty()) {
                 p_75134_1_.drop(itemstack, false);
              }

              itemstack = this.inventory.removeItemNoUpdate(1);
              if (!itemstack.isEmpty()) {
                 p_75134_1_.drop(itemstack, false);
              }
           } else {
              p_75134_1_.inventory.placeItemBackInInventory(p_75134_1_.level, this.inventory.removeItemNoUpdate(0));
              p_75134_1_.inventory.placeItemBackInInventory(p_75134_1_.level, this.inventory.removeItemNoUpdate(1));
           }

        }
     }
}