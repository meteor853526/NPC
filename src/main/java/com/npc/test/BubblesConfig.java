/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.ForgeConfigSpec
 *  net.minecraftforge.common.ForgeConfigSpec$BooleanValue
 *  net.minecraftforge.common.ForgeConfigSpec$Builder
 *  net.minecraftforge.common.ForgeConfigSpec$ConfigValue
 *  net.minecraftforge.common.ForgeConfigSpec$DoubleValue
 *  net.minecraftforge.common.ForgeConfigSpec$IntValue
 *  org.apache.commons.lang3.tuple.Pair
 */
package com.npc.test;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BubblesConfig {
    public static final String CATEGORY_GENERAL = "general";
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        Pair clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        Pair serverSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        CLIENT_SPEC = (ForgeConfigSpec)clientSpecPair.getRight();
        CLIENT = (Client)clientSpecPair.getLeft();
        SERVER_SPEC = (ForgeConfigSpec)serverSpecPair.getRight();
        SERVER = (Server)serverSpecPair.getLeft();
    }

    public static class Server {
        public ForgeConfigSpec.BooleanValue chatListener;
        public ForgeConfigSpec.ConfigValue<String> commandName;
        public ForgeConfigSpec.DoubleValue bubbleRange;
        public ForgeConfigSpec.BooleanValue canThroughBlocks;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push(BubblesConfig.CATEGORY_GENERAL);
            this.chatListener = builder.comment("Enable Chat Listener").define("chatListener", true);
            this.commandName = builder.comment("Create a custom command (empty = no command)").define("commandName", ((Object)"").toString());
            this.bubbleRange = builder.comment("Radius of the bubble sending to the players (packet)").defineInRange("bubbleRange", 50.0, 0.0, Double.MAX_VALUE);
            this.canThroughBlocks = builder.comment("Can players see the bubbles through blocks or not ( useful for server rp as example )").define("throughBlock", true);
        }
    }

    public static class Client {
        public ForgeConfigSpec.ConfigValue<List<Integer>> colorOutline;
        public ForgeConfigSpec.ConfigValue<List<Integer>> colorInside;
        public ForgeConfigSpec.ConfigValue<List<Integer>> colorText;
        public ForgeConfigSpec.IntValue durationBubbles;
        public ForgeConfigSpec.IntValue maxBubblesStack;
        public ForgeConfigSpec.IntValue lineWidth;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push(BubblesConfig.CATEGORY_GENERAL);
            this.colorOutline = builder.comment("Adjust the color of the bubbles outline | args = r,g,b,a").define("outlineColor", new ArrayList());
            this.colorInside = builder.comment("Adjust the color of the bubbles inside | args = r,g,b,a").define("insideColor", new ArrayList());
            this.colorText = builder.comment("Adjust the color of the bubbles text | args = r,g,b,a").define("textColor", new ArrayList());
            this.durationBubbles = builder.comment("Adjust duration time of bubbles").defineInRange("durationBubbles", 10, 0, 60);
            this.maxBubblesStack = builder.comment("Max Bubbles Stackable on a player's head").defineInRange("maxBubblesStack", 0, 0, Integer.MAX_VALUE);
            this.lineWidth = builder.comment("Max line width").defineInRange("lineWidth", 150, 50, Integer.MAX_VALUE);
        }
    }
}

