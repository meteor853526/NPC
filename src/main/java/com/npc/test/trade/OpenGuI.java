package com.npc.test.trade;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

public class OpenGuI {
    public static void openGUI(ServerPlayerEntity player) {
    	// 交易目標
    	ItemStack target, source;
    	source = new ItemStack(Items.EMERALD, 1);
    	target = new ItemStack(Blocks.CARROTS, 3);
    	NetworkHooks.openGui(player, new SimpleNamedContainerProvider((i, playerInventory, playerEntity) -> {
    		return new NpcContainer(i, playerInventory, target, source);
        }, new TranslationTextComponent("Farmer trade screen")), packetBuffer -> {
        	packetBuffer.writeItemStack(target, false);
        	packetBuffer.writeItemStack(source, false);
        });
    }
}
