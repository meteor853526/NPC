package com.example.examplemod.container;

import com.example.examplemod.item.ModItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "examplemod");
    public static RegistryObject<Item> NpcContainerItem = ITEMS.register("npc_container", () -> {
        return new BlockItem(BlockRegistry.npcContainerBlock.get(), new Item.Properties().group(ModItemGroup.EXAMPLE_GROUP));
    });
}
