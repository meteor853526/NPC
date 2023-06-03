package com.npc.test.trade;

import com.npc.test.chat.ConnectGUI;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenGuI {
	public ServerChatEvent event;
    public static void openGUI(ServerPlayerEntity player) throws IOException {

			FileReader fr = new FileReader("C:\\Users\\User\\IdeaProjects\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\GUI_Value.txt");
			BufferedReader br = new BufferedReader(fr);

			String line;
			String lastLine = "";
			while (br.ready()) {
				line = br.readLine();
				if (line != null) {
					lastLine = line;
				}
			}
			fr.close();


			String[] parts = lastLine.split(",");

			String item = parts[0].trim();
			int Amount = Integer.parseInt(parts[1].trim());


			// 交易目標
			ItemStack target = null, source;
			if (item.contains("Wheat")) {
				target = new ItemStack(Items.WHEAT, Amount);
			}
			if (item.contains("Carrot")) {
				target = new ItemStack(Blocks.CARROTS, Amount);
			}
			if (item.contains("Beetroot")) {
				target = new ItemStack(Items.BEETROOT, Amount);
			}
    	source = new ItemStack(Items.EMERALD, 1);


			ItemStack finalTarget = target;
			NetworkHooks.openGui(player, new SimpleNamedContainerProvider((i, playerInventory, playerEntity) -> {
    		return new NpcContainer(i, playerInventory, finalTarget, source);
        }, new TranslationTextComponent("Farmer trade screen")), packetBuffer -> {
        	packetBuffer.writeItemStack(finalTarget, false);
        	packetBuffer.writeItemStack(source, false);
        });
    }
}
