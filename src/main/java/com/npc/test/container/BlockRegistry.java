package com.npc.test.container;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "npctestmod");
    public static RegistryObject<Block> obsidianFirstContainerBlock = BLOCKS.register("npc_container", () -> {
        return new NpcContainerBlock();
    });
    public static NpcContainerItemNumber npcContainerBlock;
}