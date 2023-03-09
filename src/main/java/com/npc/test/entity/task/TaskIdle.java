package com.npc.test.entity.task;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.npc.test.NpcTestMod;
import com.npc.test.api.task.IMaidTask;
import com.npc.test.passive.NpcEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TaskIdle implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(NpcTestMod.MOD_ID, "idle");
    private static final float LOW_TEMPERATURE = 0.15F;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.FEATHER.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(NpcEntity maid) {
        return null;
    }

//    @Nullable
//    @Override
//    public SoundEvent getAmbientSound(NpcEntity maid) {
//        return SoundUtil.environmentSound(maid, InitSounds.MAID_IDLE.get(), 0.5f);
//    }

    @Override
    public List<Pair<Integer, Task<? super NpcEntity>>> createBrainTasks(NpcEntity maid) {
        Pair<Task<? super NpcEntity>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(NpcEntity.TYPE, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> noLook = Pair.of(new DummyTask(20, 40), 2);
        //Pair<Task<? super NpcEntity>, Integer> findSnowballTarget = Pair.of(new ForgetAttackTargetTask<>(this::canSnowballFight, this::findFirstValidSnowballTarget), 2);
        //FirstShuffledTask<NpcEntity> firstShuffledTask = new FirstShuffledTask<>(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, findSnowballTarget, noLook));
        //SupplementedTask<NpcEntity> randomLookTask = new SupplementedTask<>(e -> !e.isBegging(), firstShuffledTask);

        //MaidSnowballTargetTask snowballTargetTask = new MaidSnowballTargetTask(40);

//        Pair<Integer, Task<? super NpcEntity>> beg = Pair.of(5, new MaidBegTask());
//        Pair<Integer, Task<? super NpcEntity>> supplemented = Pair.of(6, randomLookTask);
//        Pair<Integer, Task<? super NpcEntity>> snowballFight = Pair.of(6, snowballTargetTask);

//        return Lists.newArrayList(beg, supplemented, snowballFight);
        return Lists.newArrayList(null,null);
    }

//    private boolean canSnowballFight(NpcEntity maid) {
//        World world = maid.level;
//        BlockPos pos = maid.blockPosition();
//        return !maid.isBegging() && world.getBiome(pos).getTemperature(pos) < LOW_TEMPERATURE && world.getBlockState(pos).is(Blocks.SNOW);
//    }
//
//    private Optional<? extends LivingEntity> findFirstValidSnowballTarget(EntityMaid maid) {
//        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).flatMap(
//                mobs -> mobs.stream().filter(e -> isSnowballTarget(e, maid))
//                        .filter(e -> maid.isWithinRestriction(e.blockPosition()))
//                        .findFirst());
//    }
//
//    private boolean isSnowballTarget(LivingEntity entity, EntityMaid maid) {
//        if (maid.isOwnedBy(entity)) {
//            return true;
//        }
//        if (entity instanceof EntityMaid && maid.getOwner() != null) {
//            EntityMaid maidOther = (EntityMaid) entity;
//            return maid.getOwner().equals(maidOther.getOwner());
//        }
//        return false;
//    }


}
