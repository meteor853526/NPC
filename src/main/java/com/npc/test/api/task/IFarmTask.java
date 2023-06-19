package com.npc.test.api.task;

import com.npc.test.api.task.IMaidTask;

//package com.npc.test.api.task;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmMoveTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmPlantTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
//import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.ai.brain.task.Task;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.BlockPos;
//
//import java.util.List;
//
public interface IFarmTask extends IMaidTask {
//    /**
//     *
//     *
//     * @param stack ItemStack
//     * @return boolean
//     */
//    boolean isSeed(ItemStack stack);
//
//    /**
//     *
//     *
//     * @param maid
//     * @param cropPos
//     * @param cropState
//     * @return
//     */
//    boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);
//
//    /**
//     *
//     *
//     * @param maid
//     * @param cropPos
//     * @param cropState
//     */
//    void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);
//
//    /**
//     *
//     * @param maid
//     * @param basePos
//     * @param baseState
//     * @param seed
//     * @return
//     */
//    boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed);
//
//    /**
//     *
//     *
//     * @param maid
//     * @param basePos
//     * @param baseState
//     * @param seed
//     * @return
//     */
//    ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed);
//
//    /**
//     *
//     *
//     * @return
//     */
//    default double getCloseEnoughDist() {
//        return 1.0;
//    }
//
//    /**
//     *
//     *
//     * @return
//     */
//    default boolean checkCropPosAbove() {
//        return true;
//    }
//
//    /**
//     *
//     *
//     * @param maid
//     * @return
//     */
//    @Override
//    default SoundEvent getAmbientSound(EntityMaid maid) {
//        return SoundUtil.environmentSound(maid, InitSounds.MAID_FARM.get(), 0.5f);
//    }
//
//    /**
//     *
//     *
//     * @param maid
//     * @return
//     */
//    @Override
//    default List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
//        MaidFarmMoveTask maidFarmMoveTask = new MaidFarmMoveTask(this, 0.6f, 16);
//        MaidFarmPlantTask maidFarmPlantTask = new MaidFarmPlantTask(this);
//        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
//    }
}
