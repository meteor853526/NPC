package com.npc.test.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.npc.test.passive.NpcEntity;
import com.npc.test.trade.OpenGuI;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.lwjgl.system.CallbackI;

public class OpenGuiCommand {

  public NpcEntity npc;

  public OpenGuiCommand(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("ok").then(Commands.literal("gpt").executes((command) -> {
      return  Opengui(command.getSource());
    })));
  }

  private  int Opengui(CommandSource source) throws CommandSyntaxException {
    World worldIn = Minecraft.getInstance().level;
    if(worldIn.isClientSide) {
      DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        OpenGuI.openGUI();
      });
    }
    return 1;
  }
}
