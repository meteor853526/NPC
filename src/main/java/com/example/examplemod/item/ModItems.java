package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.custom.FireStone;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MOD_ID);

    public static final RegistryObject<Item> AMETHYST = ITEMS.register("amethyst",
            () -> new Item(new Item.Properties().group(ModItemGroup.EXAMPLE_GROUP)));

    public static final RegistryObject<Item> FIRESTONE = ITEMS.register( "firestone",
            () -> new FireStone(new Item.Properties().group(ModItemGroup.EXAMPLE_GROUP).maxDamage(8)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
