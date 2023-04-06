package com.example.examplemod.container;

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

public class NpcContainerTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private Inventory inventory = new Inventory(1);
    private NpcContainerItemNumber itemNumber = new NpcContainerItemNumber();

    public NpcContainerTileEntity() {
        super(TileEntityTypeRegistry.npcContainerTileEntity.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("NPC Container");
    }

    @Nullable
    @Override
    public Container createMenu(int sycID, PlayerInventory inventory, PlayerEntity player) {
        return new NpcContainer(sycID, inventory, this.pos, this.world, itemNumber);
    }

//    @Override
    public void read(CompoundNBT compound) {
        this.inventory.addItem(ItemStack.read(compound.getCompound("item")));
        super.read(getBlockState(), compound);
////        read->load
//        this.inventory.addItem(ItemStack.read(compound.getCompound("item"))); // read => of
//        super.read(compound);  // read => save
    }

//    CompoundNBT => CompoundTag
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStack itemStack = this.inventory.getStackInSlot(0).copy();
        compound.put("item", itemStack.serializeNBT());
        return super.write(compound); // write => save
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void tick() { // add World world
//        isRemote => isClientSide
        if (!world.isRemote) {
            // getStackInSlot => getItem
            this.itemNumber.set(0, this.inventory.getStackInSlot(0).getCount());
        }
    }
}
