package com.example.examplemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class OpenGuI {
    public static void openGUI() {
        Minecraft.getInstance().displayGuiScreen(new NpcGui(new StringTextComponent("test")));
    }
}
