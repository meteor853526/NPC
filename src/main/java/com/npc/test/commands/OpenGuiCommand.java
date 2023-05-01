package com.npc.test.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.npc.test.trade.OpenGuI;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class OpenGuiCommand {

    public OpenGuiCommand(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("ok").then(Commands.literal("gpt").executes((command) -> {
      return  Opengui(command.getSource());
    })));
  }

  private  int Opengui(CommandSource source) throws CommandSyntaxException {
    World worldIn = Minecraft.getInstance().level;
    if(worldIn.isClientSide) {
      DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        try {
			OpenGuI.openGUI(source.getPlayerOrException());
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
      });
    }
    return 1;
  }
}
