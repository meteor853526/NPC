package com.npc.test.container;
import net.minecraft.util.IIntArray;

public class NpcContainerItemNumber implements IIntArray {
    int i = 0;

    @Override
    public int get(int index) {
        return i;
    }

    @Override
    public void set(int index, int value) {
        i = value;
    }

    @Override
    public int getCount() {
        return 0;
    }

//    @Override
    public int size() {
        return 1;
    }
}