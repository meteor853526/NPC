package com.npc.test.trade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;

public class NpcInventory implements IInventory {
	 private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
	ItemStack target, source;
	 public NpcInventory(ItemStack target, ItemStack source) {
		 this.target = target;
		 this.source= source;
	 }
	@Override
	public void clearContent() {
		this.itemStacks.clear();
	}

	@Override
	public int getContainerSize() {
		return this.itemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.itemStacks) {
	         if (!itemstack.isEmpty()) {
	            return false;
	         }
	      }
	     return true;
	}

	@Override
	public ItemStack getItem(int p_70301_1_) {
		return this.itemStacks.get(p_70301_1_);
	}

	@Override
	public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
		ItemStack itemstack = this.itemStacks.get(p_70298_1_);
	      if (p_70298_1_ == 2 && !itemstack.isEmpty()) {
	         return ItemStackHelper.removeItem(this.itemStacks, p_70298_1_, itemstack.getCount());
	      } else {
	         ItemStack itemstack1 = ItemStackHelper.removeItem(this.itemStacks, p_70298_1_, p_70298_2_);
	         if (!itemstack1.isEmpty() && this.isPaymentSlot(p_70298_1_)) {
	            this.updateSellItem();
	         }

	         return itemstack1;
	      }
	}
	


    private boolean isPaymentSlot(int p_70469_1_) {
      return p_70469_1_ == 0 || p_70469_1_ == 1;
    }

	@Override
	public ItemStack removeItemNoUpdate(int p_70304_1_) {
		return ItemStackHelper.takeItem(this.itemStacks, p_70304_1_);
	}

	@Override
	public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
		this.itemStacks.set(p_70299_1_, p_70299_2_);
	      if (!p_70299_2_.isEmpty() && p_70299_2_.getCount() > this.getMaxStackSize()) {
	         p_70299_2_.setCount(this.getMaxStackSize());
	      }

	      if (this.isPaymentSlot(p_70299_1_)) {
	         this.updateSellItem();
	      }
	}

	@Override
	public void setChanged() {
		 this.updateSellItem();
	}

	@Override
	public boolean stillValid(PlayerEntity p_70300_1_) {
		return true;
	}
	
	public void updateSellItem() {
        ItemStack itemstack;
        ItemStack itemstack1;
        if (this.itemStacks.get(0).isEmpty()) {
           itemstack = this.itemStacks.get(1);
           itemstack1 = ItemStack.EMPTY;
        } else {
           itemstack = this.itemStacks.get(0);
           itemstack1 = this.itemStacks.get(1);
        }
		// 檢查交易更新條件
        int count = 0;
        if (itemstack.isEmpty() || source == null || target == null) {
           this.setItem(2, ItemStack.EMPTY);
        } else {
        	if(isRequiredItem(itemstack, source) && itemstack.getCount() == source.getCount())
        		count = 1;
        	if(!itemstack1.isEmpty() && isRequiredItem(itemstack1, source) && itemstack1.getCount() == source.getCount())
        		count = 1;
        }
        
        if(count > 0) {
        	this.setItem(2, target.copy());
        } else
        	this.setItem(2, ItemStack.EMPTY);
     }
	
	private boolean isRequiredItem(ItemStack p_222201_1_, ItemStack p_222201_2_) {
	      if (p_222201_2_.isEmpty() && p_222201_1_.isEmpty()) {
	         return true;
	      } else {
	         ItemStack itemstack = p_222201_1_.copy();
	         if (itemstack.getItem().isDamageable(itemstack)) {
	            itemstack.setDamageValue(itemstack.getDamageValue());
	         }

	         return ItemStack.isSame(itemstack, p_222201_2_) && (!p_222201_2_.hasTag() || itemstack.hasTag() && NBTUtil.compareNbt(p_222201_2_.getTag(), itemstack.getTag(), false));
	      }
	   }

}
