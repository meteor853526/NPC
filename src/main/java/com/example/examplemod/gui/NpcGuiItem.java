package com.example.examplemod.gui;


import com.example.examplemod.item.ModItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class NpcGuiItem extends Item {
    public NpcGuiItem() {
        super(new Properties().group(ModItemGroup.EXAMPLE_GROUP));
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                OpenGuI.openGUI();
            });
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}