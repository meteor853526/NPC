package com.npc.test.api.task;


import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IMaidTask {

    ResourceLocation getUid();


    ItemStack getIcon();

    /**
     *
     *
     * @param maid
     * @return
     */
    @Nullable
    SoundEvent getAmbientSound(NpcEntity maid);

    /**
     *
     *
     * @param maid
     * @return
     */
    List<Pair<Integer, Task<? super NpcEntity>>> createBrainTasks(NpcEntity maid);

    /**
     *
     *
     * @param maid
     * @return
     */
    default boolean isEnable(NpcEntity maid) {
        return true;
    }

    /**
     *
     *
     * @return
     */
    default TranslationTextComponent getName() {
        return new TranslationTextComponent(String.format("task.%s.%s", getUid().getNamespace(), getUid().getPath()));
    }

    /**
     *
     *
     * @param maid
     * @return
     */
    default List<Pair<String, Predicate<NpcEntity>>> getConditionDescription(NpcEntity maid) {
        return Collections.emptyList();
    }

    /**
     *
     *
     * @param maid
     * @return
     */
    default List<String> getDescription(NpcEntity maid) {
        String key = String.format("task.%s.%s.desc", getUid().getNamespace(), getUid().getPath());
        return Lists.newArrayList(key);
    }
}
