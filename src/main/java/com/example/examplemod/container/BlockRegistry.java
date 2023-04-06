package com.example.examplemod.container;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "examplemod");
    public static RegistryObject<Block> npcContainerBlock = BLOCKS.register("npc_container", () -> {
        return new NpcContainerBlock();
    });
}