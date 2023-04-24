/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.command.CommandSource
 *  net.minecraft.command.Commands
 *  net.minecraft.command.arguments.MessageArgument
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.RegistryKey
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.KeybindTextComponent
 *  net.minecraft.util.text.TranslationTextComponent
 *  net.minecraftforge.fml.network.PacketDistributor
 *  net.minecraftforge.fml.network.PacketDistributor$TargetPoint
 */
package com.npc.test.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import com.npc.test.config.BubblesConfig;
import com.npc.test.NpcTestMod;
import com.npc.test.packet.SCSyncBubbleMessage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class BubblesCustomCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal((String)((String) BubblesConfig.SERVER.commandName.get())).then(Commands.argument((String)"message", (ArgumentType)MessageArgument.message()).executes(p_198626_0_ -> {
            ITextComponent itextcomponent = MessageArgument.getMessage((CommandContext)p_198626_0_, (String)"message");
            BubblesCustomCommand.createBubble((CommandSource)p_198626_0_.getSource(), itextcomponent.getString());
            return 1;
        })));
    }

    public static int createBubble(CommandSource source, String message) {
        if (source.getEntity() == null || !(source.getEntity() instanceof PlayerEntity)) {
            source.sendFailure((ITextComponent)new TranslationTextComponent("text.mustbeplayer"));
            return 0;
        }
        PlayerEntity player = (PlayerEntity)source.getEntity();
        long startTime = System.currentTimeMillis();
        double distance = (Double)BubblesConfig.SERVER.bubbleRange.get();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        NpcTestMod.channel.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p((double)x, (double)y, (double)z, (double)distance, (RegistryKey)player.getCommandSenderWorld().dimension())), (Object)new SCSyncBubbleMessage(startTime, message, player.getUUID()));
        player.sendMessage((ITextComponent)new KeybindTextComponent("\u00a7cBubble created !"), UUID.randomUUID());
        return 0;
    }
}

