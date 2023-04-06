package com.example.examplemod.util;

import com.example.examplemod.ExampleMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ExampleTags {

    public static class Blocks  {

        public static final Tags.IOptionalNamedTag<Block> FIRESTONE_CLICKABLE_BLOCKS = createTag("firestone_clickable_blocks");
//        public static final Tags.IOptionalNamedTag<Block> NPC_CONTAINER_BLOCK = createTag("npc_container_block");
        private static Tags.IOptionalNamedTag<Block> createTag(String name) {
            return BlockTags.createOptional(new ResourceLocation(ExampleMod.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Block> createForgeTag(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }

    }

    public static class Items  {

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(ExampleMod.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }

    }

}
