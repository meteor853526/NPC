package com.npc.test.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.npc.test.NpcTestMod;
import com.npc.test.passive.NpcEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;

public class ChatCommand {



    public NpcEntity npc;
    public ChatCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("chat").then(Commands.literal("gpt").executes((command) -> {
            return setHome(command.getSource());
        })));
    }

    private int setHome(CommandSource source ) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        BlockPos playerPos = player.blockPosition();
        Vector3d pl = player.position();
        String pos = "(" + playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ() + ")";


        player.getPersistentData().putIntArray(NpcTestMod.MOD_ID + "homepos", new int[]{ playerPos.getX(), playerPos.getY(), playerPos.getZ() });
        //new WalkToTargetTask().set(,pl);
        //new WalkToTargetTask();


        //player.getPersistentData().putIntArray(MemoryModuleType.WALK_TARGET + "walk_target", new int[]{ playerPos.getX(), playerPos.getY(), playerPos.getZ() });


        //new VillagerTasks().getMove(null,150);

        source.sendSuccess(new StringTextComponent("Set Home at " + pos), true);
        //source.sendSuccess(new StringTextComponent("Set Home at " + pl), true);
        return 1;
    }
}
