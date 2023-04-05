package com.npc.test.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

import javax.annotation.Nullable;

import static net.minecraft.command.arguments.ResourceLocationArgument.getRecipe;

//public class NpcContainerTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
public class NpcContainerTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    static final int WORK_TIME = 2 * 20;

    private int progress = 0;
    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return progress;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };



    public NpcContainerTileEntity() {
        super();
    }
    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new NpcContainer(id, playerInventory,getBlockPos(), new WorldBorder(), this.fields);
    }

//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int p_70301_1_) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int p_70304_1_) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
//
//    }
//
//    @Override
//    public boolean stillValid(PlayerEntity p_70300_1_) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction p_180463_1_) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return null;
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return null;
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {

    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        return false;
    }

    @Override
    public void clearContent() {

    }

//    private Inventory inventory = new Inventory(1);
//    private NpcContainerItemNumber itemNumber = new NpcContainerItemNumber();
//
//    public NpcContainerTileEntity() {
//        super(TileEntityTypeRegistry.NpcContainerTileEntity.get());
//    }
//
//    @Override
//    public ITextComponent getDisplayName() {
//        return new StringTextComponent("Fist Container");
//    }
//
//    @Nullable
//    @Override
//    public Container createMenu(int sycID, PlayerInventory inventory, PlayerEntity player) {
//        return new NpcContainerBlock(sycID, inventory, this.pos, this.level, itemNumber);
//    }
//
//    @Override
//    public void read(CompoundNBT compound) {
////        read->load
//        this.inventory.addItem(ItemStack.of(compound.getCompound("item"))); // read => of
//        super.save(compound);  // read => save
//    }
//
////    CompoundNBT => CompoundTag
//    @Override
//    public CompoundNBT write(CompoundNBT compound) {
//        ItemStack itemStack = this.inventory.getItem(0).copy();
//        compound.put("item", itemStack.serializeNBT());
//        return super.save(compound); // write => save
//    }
//
//    public Inventory getInventory() {
//        return inventory;
//    }
//
//    @Override
//    public void tick() { // add World world
////        isRemote => isClientSide
//        if (!level.isClientSide) {
//            // getStackInSlot => getItem
//            this.itemNumber.set(0, this.inventory.getItem(0).getCount());
//        }
//    }
}
