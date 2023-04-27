package com.npc.test.trade;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "npctestmod");
    public static RegistryObject<Item> npcIngot = ITEMS.register("npc_gui", () -> {
        return new NpcGuiItem();
    });
}