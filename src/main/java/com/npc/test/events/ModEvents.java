package com.npc.test.events;

import com.npc.test.NpcTestMod;
import com.npc.test.commands.NpcMoveCommand;
//import com.npc.test.commands.ReturnHomeCommand;
//import com.npc.test.commands.SetHomeCommand;
import com.npc.test.commands.ReturnHomeCommand;
import com.npc.test.commands.SetHomeCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;



@Mod.EventBusSubscriber(modid = NpcTestMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SetHomeCommand(event.getDispatcher());
        new ReturnHomeCommand(event.getDispatcher());
        new NpcMoveCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if(!event.getOriginal().getCommandSenderWorld().isClientSide()) {
            event.getPlayer().getPersistentData().putIntArray(NpcTestMod.MOD_ID + "homepos",
                    event.getOriginal().getPersistentData().getIntArray(NpcTestMod.MOD_ID + "homepos"));
        }
    }

}
