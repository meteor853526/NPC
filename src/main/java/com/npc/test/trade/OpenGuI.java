package com.npc.test.trade;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class OpenGuI {
    public static void openGUI() {
        Minecraft.getInstance().setScreen(new NpcGui(new StringTextComponent("test")));
    }
}
