package com.npc.test.trade;


import com.npc.test.item.ModItemGroup;
import net.minecraft.item.Item;

public class NpcGuiItem extends Item {
    public NpcGuiItem() {
        super(new Properties().tab(ModItemGroup.TUTORIAL_GROUP));
    }

}