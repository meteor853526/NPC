package com.npc.test.container;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
//        registerFactory => register
        ScreenManager.register(ContainerTypeRegistry.npcContainer.get(), (NpcContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) -> {
            return new NpcContainerScreen(screenContainer, inv, titleIn);
        });
    }
}