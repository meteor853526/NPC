package com.npc.test.entity.ai.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.npc.test.entity.ai.brain.task.DevShareItemsTask;
import com.npc.test.entity.ai.brain.task.ShareItemsTask;
import com.npc.test.entity.ai.brain.task.WalkToTargetTask;
import com.npc.test.entity.ai.brain.task.devlairTask;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.DeliveryEntity;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;

public final class DeliveryBrain extends BrainUtil {
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
                InitEntities.LOCK.get(),
                InitEntities.Farmer_Drop.get()
                //InitEntities.TARGET_POS.get()
        );
    }

    public static ImmutableList<SensorType<? extends Sensor<? super DeliveryEntity>>> getSensorTypes() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.HURT_BY
//                InitEntities.MAID_HOSTILES_SENSOR.get(),
//                InitEntities.MAID_PICKUP_ENTITIES_SENSOR.get()
        );
    }

    public static void registerBrainGoals(Brain<DeliveryEntity> brain, DeliveryEntity maid) {
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

//    private static void registerSchedule(Brain<DeliveryEntity> brain, DeliveryEntity maid) {
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

    private static void registerCoreGoals(Brain<DeliveryEntity> brain) {
        Pair<Integer, Task<? super DeliveryEntity>> swim = Pair.of(0, new SwimTask(0.8f));
        Pair<Integer, Task<? super DeliveryEntity>> look = Pair.of(0, new LookTask(45, 90));
//        Pair<Integer, Task<? super EntityMaid>> maidPanic = Pair.of(1, new MaidPanicTask());
//        Pair<Integer, Task<? super EntityMaid>> maidAwait = Pair.of(1, new MaidAwaitTask());
        Pair<Integer, Task<? super DeliveryEntity>> interactWithDoor = Pair.of(2, new InteractWithDoorTask());
        Pair<Integer, Task<? super DeliveryEntity>> walkToTarget = Pair.of(2, new WalkToTargetTask(DeliveryEntity.TYPE));
        Pair<Integer, Task<? super DeliveryEntity>> ShareItemTask = Pair.of(3, new DevShareItemsTask());
        Pair<Integer, Task<? super DeliveryEntity>> test = Pair.of(3, new devlairTask());
//        Pair<Integer, Task<? super EntityMaid>> followOwner = Pair.of(3, new MaidFollowOwnerTask(0.5f, 8, 2));
//        Pair<Integer, Task<? super EntityMaid>> pickupItem = Pair.of(10, new MaidPickupEntitiesTask(EntityMaid::isPickup, 8, 0.6f));
//        Pair<Integer, Task<? super EntityMaid>> maidReturnHome = Pair.of(20, new MaidReturnHomeTask(0.5f));
//        Pair<Integer, Task<? super EntityMaid>> clearSleep = Pair.of(99, new MaidClearSleepTask());

        //brain.addActivity(Activity.CORE, ImmutableList.of(swim, look, maidPanic, maidAwait, interactWithDoor, walkToTarget, followOwner, pickupItem, maidReturnHome, clearSleep));
        brain.addActivity(Activity.CORE, ImmutableList.of(swim, look, interactWithDoor, walkToTarget,ShareItemTask,test));
    }

    private static void registerIdleGoals(Brain<DeliveryEntity> brain) {
        Pair<Task<? super DeliveryEntity>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super DeliveryEntity>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(DeliveryEntity.TYPE, 5), 1);
        Pair<Task<? super DeliveryEntity>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super DeliveryEntity>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super DeliveryEntity>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super DeliveryEntity>, Integer> walkRandomly = Pair.of(new WalkRandomlyTask(0.3f, 5, 3), 1);
        Pair<Task<? super DeliveryEntity>, Integer> noLook = Pair.of(new DummyTask(40, 80), 2);
        FirstShuffledTask<DeliveryEntity> firstShuffledTask = new FirstShuffledTask<>(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, walkRandomly, noLook));
        //SupplementedTask<DeliveryEntity> supplementedTask = new SupplementedTask<>(e -> !e.isBegging(), firstShuffledTask);

        //Pair<Integer, Task<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        //Pair<Integer, Task<? super DeliveryEntity>> supplemented = Pair.of(6, supplementedTask);
        Pair<Integer, Task<? super DeliveryEntity>> updateActivity = Pair.of(99, new UpdateActivityTask());

        //brain.addActivity(Activity.IDLE, ImmutableList.of(beg, supplemented, updateActivity));
        brain.addActivity(Activity.IDLE, ImmutableList.of(  updateActivity));
    }

}
