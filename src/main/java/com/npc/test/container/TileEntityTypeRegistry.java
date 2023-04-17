package com.npc.test.container;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraftforge.forgespi.Environment.build;

public class TileEntityTypeRegistry {
//    remove new and <>
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "npctestmod");
    public static RegistryObject<TileEntityType<NpcContainerTileEntity>> npcContainerTileEntity = TILE_ENTITY_TYPE_DEFERRED_REGISTER.register("npc_container_tileentity", () -> {
        return TileEntityType.Builder.create(() -> {
            return new NpcContainerTileEntity();
        }, BlockRegistry.npcContainerBlock.get()).build(null);
    });
}