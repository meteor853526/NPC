package com.npc.test.item;

import com.npc.test.item.custom.ModSpawnEggItem;
import com.npc.test.NpcTestMod;
import com.npc.test.entity.ModEntityTypes;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NpcTestMod.MOD_ID);


    public static final RegistryObject<Item> AMETHYST = ITEMS.register("amethyst",
            () -> new Item(new Item.Properties().tab(ModItemGroup.TUTORIAL_GROUP)));
    public static final RegistryObject<ModSpawnEggItem> BUFF_ZOMBIE_SPAWN_EGG = ITEMS.register("npc_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.NPC, 0x464F56, 0x1D6336,
                    new Item.Properties().tab(ModItemGroup.TUTORIAL_GROUP)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
