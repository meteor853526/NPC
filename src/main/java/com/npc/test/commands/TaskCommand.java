package com.npc.test.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class TaskCommand{
    public TaskCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("task").then(Commands.literal("diedie").executes((command) -> {
            return getTask(command.getSource());
        })));
    }

    private int getTask(CommandSource source ) throws CommandSyntaxException {
        source.sendSuccess(new StringTextComponent("diedie: give me corn" ), true);
//        context.getSource().sendFeedback(new StringTextComponent("build horse farm"), false);
        return 1;
    }

}