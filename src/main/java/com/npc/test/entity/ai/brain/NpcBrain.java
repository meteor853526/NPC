package com.npc.test.entity.ai.brain;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
//import com.google.common.collect.Lists;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
//import com.npc.test.entity.ai.brain.task.MaidFollowOwnerTask;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder;
import com.npc.test.entity.ai.brain.task.*;
import com.npc.test.entity.ai.brain.task.PickupWantedItemTask;
import com.npc.test.entity.ai.brain.task.ShareItemsTask;
import com.npc.test.entity.ai.brain.task.WalkToTargetTask;
import com.npc.test.init.InitEntities;

import com.npc.test.passive.NpcEntity;
//import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.Memory;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class NpcBrain extends BrainUtil {


    public static ImmutableList<MemoryModuleType<?>> getMemoryTypes() {
        return ImmutableList.of(
                MemoryModuleType.PATH,
                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.NEAREST_HOSTILE,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                InitEntities.SERVICE_CHECK.get(),
                InitEntities.PICKUP.get(),
                InitEntities.TASK_ID.get(),
                InitEntities.Farmer_Drop.get()
                //InitEntities.TARGET_POS.get()
        );
    }

    public static ImmutableList<SensorType<? extends Sensor<? super NpcEntity>>> getSensorTypes() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.HURT_BY,
                InitEntities.MAID_PICKUP_ENTITIES_SENSOR.get()
//                InitEntities.MAID_HOSTILES_SENSOR.get(),
//                InitEntities.MAID_PICKUP_ENTITIES_SENSOR.get()
        );
    }

    public static void registerBrainGoals(Brain<NpcEntity> brain, NpcEntity maid) {
        //registerSchedule(brain, maid);
        registerCoreGoals(brain);
//        registerPanicGoals(brain);
//        registerAwaitGoals(brain);
//        registerIdleGoals(brain);
//        registerWorkGoals(brain, maid);
//        registerRestGoals(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(maid.level.getDayTime(), maid.level.getGameTime());
    }

//    private static void registerSchedule(Brain<NpcEntity> brain, NpcEntity maid) {
//        switch (maid.getSchedule()) {
//            case ALL:
//                brain.setSchedule(InitEntities.MAID_ALL_DAY_SCHEDULES.get());
//                break;
//            case NIGHT:
//                brain.setSchedule(InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get());
//                break;
//            case DAY:
//            default:
//                brain.setSchedule(InitEntities.MAID_DAY_SHIFT_SCHEDULES.get());
//                break;
//        }
//    }

    private static void registerCoreGoals(Brain<NpcEntity> brain) {
        Pair<Integer, Task<? super NpcEntity>> swim = Pair.of(0, new SwimTask(0.8f));
        Pair<Integer, Task<? super NpcEntity>> look = Pair.of(0, new LookTask(45, 90));
//        Pair<Integer, Task<? super EntityMaid>> maidPanic = Pair.of(1, new MaidPanicTask());
//        Pair<Integer, Task<? super EntityMaid>> maidAwait = Pair.of(1, new MaidAwaitTask());
        Pair<Integer, Task<? super NpcEntity>> interactWithDoor = Pair.of(2, new InteractWithDoorTask());
        Pair<Integer, Task<? super NpcEntity>> walkToTarget = Pair.of(2, new WalkToTargetTask(NpcEntity.TYPE));
        Pair<Integer, Task<? super NpcEntity>> ShareItemTask = Pair.of(3, new ShareItemsTask());
        Pair<Integer, Task<? super NpcEntity>> test = Pair.of(3, new devlairTask());
        Pair<Integer, Task<? super NpcEntity>> ItemTask = Pair.of(3, new PickupWantedItemTask<>(1,false,1));
        Pair<Integer, Task<? super NpcEntity>> pickupItem = Pair.of(10, new MaidPickupEntitiesTask(NpcEntity::isPickup, 8, 0.6f));
//        Pair<Integer, Task<? super EntityMaid>> followOwner = Pair.of(3, new MaidFollowOwnerTask(0.5f, 8, 2));
//        Pair<Integer, Task<? super EntityMaid>> pickupItem = Pair.of(10, new MaidPickupEntitiesTask(EntityMaid::isPickup, 8, 0.6f));
//        Pair<Integer, Task<? super EntityMaid>> maidReturnHome = Pair.of(20, new MaidReturnHomeTask(0.5f));
//        Pair<Integer, Task<? super EntityMaid>> clearSleep = Pair.of(99, new MaidClearSleepTask());

        //brain.addActivity(Activity.CORE, ImmutableList.of(swim, look, maidPanic, maidAwait, interactWithDoor, walkToTarget, followOwner, pickupItem, maidReturnHome, clearSleep));
        brain.addActivity(Activity.CORE, ImmutableList.of(swim, look, interactWithDoor, walkToTarget,pickupItem,ItemTask,test,ShareItemTask));
    }

    private static void registerIdleGoals(Brain<NpcEntity> brain) {
        Pair<Task<? super NpcEntity>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(NpcEntity.TYPE, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super NpcEntity>, Integer> walkRandomly = Pair.of(new WalkRandomlyTask(0.3f, 5, 3), 1);
        Pair<Task<? super NpcEntity>, Integer> noLook = Pair.of(new DummyTask(40, 80), 2);
        FirstShuffledTask<NpcEntity> firstShuffledTask = new FirstShuffledTask<>(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, walkRandomly, noLook));
        //SupplementedTask<NpcEntity> supplementedTask = new SupplementedTask<>(e -> !e.isBegging(), firstShuffledTask);

        //Pair<Integer, Task<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        //Pair<Integer, Task<? super NpcEntity>> supplemented = Pair.of(6, supplementedTask);
        Pair<Integer, Task<? super NpcEntity>> updateActivity = Pair.of(99, new UpdateActivityTask());

        //brain.addActivity(Activity.IDLE, ImmutableList.of(beg, supplemented, updateActivity));
        brain.addActivity(Activity.IDLE, ImmutableList.of(  updateActivity));
    }
//
//    private static void registerWorkGoals(Brain<NpcEntity> brain, NpcEntity maid) {
//        Pair<Integer, Task<? super NpcEntity>> updateActivity = Pair.of(99, new UpdateActivityTask());
//        List<Pair<Integer, Task<? super NpcEntity>>> pairMaidList = maid.getTask().createBrainTasks(maid);
//        if (pairMaidList.isEmpty()) {
//            pairMaidList = Lists.newArrayList(updateActivity);
//        } else {
//            pairMaidList.add(updateActivity);
//        }
//        //pairMaidList.add(Pair.of(6, new MaidBegTask()));
//        brain.addActivity(Activity.WORK, ImmutableList.copyOf(pairMaidList));
//    }
//
//    private static void registerRestGoals(Brain<EntityMaid> brain) {
//        Pair<Integer, Task<? super EntityMaid>> findBed = Pair.of(5, new MaidFindBedTask(0.6f, 12));
//        Pair<Integer, Task<? super EntityMaid>> sleep = Pair.of(6, new MaidSleepTask());
//        Pair<Integer, Task<? super EntityMaid>> updateActivity = Pair.of(99, new UpdateActivityTask());
//
//        brain.addActivity(Activity.REST, ImmutableList.of(findBed, sleep, updateActivity));
//    }
//
//    private static void registerPanicGoals(Brain<EntityMaid> brain) {
//        Pair<Integer, Task<? super EntityMaid>> clearHurt = Pair.of(5, new MaidClearHurtTask());
//        Pair<Integer, Task<? super EntityMaid>> runAway = Pair.of(5, MaidRunAwayTask.entity(MemoryModuleType.NEAREST_HOSTILE, 0.7f, 6, false));
//        Pair<Integer, Task<? super EntityMaid>> runAwayHurt = Pair.of(5, MaidRunAwayTask.entity(MemoryModuleType.HURT_BY_ENTITY, 0.7f, 6, false));
//
//        brain.addActivity(Activity.PANIC, ImmutableList.of(clearHurt, runAway, runAwayHurt));
//    }
//
//    private static void registerAwaitGoals(Brain<EntityMaid> brain) {
//        Pair<Task<? super EntityMaid>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
//        Pair<Task<? super EntityMaid>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
//        Pair<Task<? super EntityMaid>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
//        Pair<Task<? super EntityMaid>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
//        Pair<Task<? super EntityMaid>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
//        Pair<Task<? super EntityMaid>, Integer> noLook = Pair.of(new DummyTask(30, 60), 2);
//
//        Pair<Integer, Task<? super EntityMaid>> shuffled = Pair.of(5, new FirstShuffledTask<>(
//                ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, noLook)));
//        Pair<Integer, Task<? super EntityMaid>> updateActivity = Pair.of(99, new UpdateActivityTask());
//
//        brain.addActivity(Activity.RIDE, ImmutableList.of(shuffled, updateActivity));
//    }


}
