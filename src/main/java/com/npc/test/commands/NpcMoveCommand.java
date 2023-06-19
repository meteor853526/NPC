package com.npc.test.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.npc.test.NpcTestMod;
//import com.npc.test.api.task.IFarmTask;

import com.npc.test.entity.ai.brain.task.MaidMoveToBlockTask;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.NpcEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class NpcMoveCommand {
    public NpcEntity npc;
    public NpcMoveCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("npc").then(Commands.literal("move").executes((command) -> {
            return NpcMove(command.getSource());
        })));
    }

    private int NpcMove(CommandSource source ) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        boolean hasHomepos = player.getPersistentData().getIntArray(NpcTestMod.MOD_ID + "homepos").length != 0;

        BlockPos pos = player.blockPosition();


//        npc.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(pos));
//        new MaidFarmMoveTask(null,100,100);
        if(hasHomepos) {

            int[] playerPos = player.getPersistentData().getIntArray(NpcTestMod.MOD_ID + "homepos");


            player.teleportTo(playerPos[0], playerPos[1], playerPos[2]);

            source.sendSuccess(new StringTextComponent("Player returned Home!"), true);
            return 1;
        } else {
            source.sendSuccess(new StringTextComponent("No Home Position has been set!"),true );
            System.out.println("Faile");
            return -1;
        }
    }
}
