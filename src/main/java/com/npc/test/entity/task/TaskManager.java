package com.npc.test.entity.task;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.npc.test.api.task.IMaidTask;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TaskManager {
    private static Map<ResourceLocation, IMaidTask> TASK_MAP;
    private static List<IMaidTask> TASK_INDEX;
    private static IMaidTask IDLE_TASK;

    private TaskManager() {
        IDLE_TASK = new TaskIdle();
        TASK_MAP = Maps.newHashMap();
        TASK_INDEX = Lists.newArrayList();
    }

    public static void init() {
        TaskManager manager = new TaskManager();
//        manager.add(IDLE_TASK);
//        manager.add(new TaskAttack());
//        manager.add(new TaskBowAttack());
//        manager.add(new TaskDanmakuAttack());
//        manager.add(new TaskNormalFarm());
//        manager.add(new TaskSugarCane());
//        manager.add(new TaskMelon());
//        manager.add(new TaskCocoa());
//        manager.add(new TaskGrass());
//        manager.add(new TaskSnow());
//        manager.add(new TaskFeedOwner());
//        manager.add(new TaskShears());
//        manager.add(new TaskMilk());
//        manager.add(new TaskTorch());
//        manager.add(new TaskFeedAnimal());
//        manager.add(new TaskExtinguishing());
//        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
//            littleMaid.addMaidTask(manager);
//        }
        TASK_MAP = ImmutableMap.copyOf(TASK_MAP);
        TASK_INDEX = ImmutableList.copyOf(TASK_INDEX);
    }

    /**
     * get Task
     */
    public static Optional<IMaidTask> findTask(ResourceLocation uid) {
        return Optional.ofNullable(TASK_MAP.get(uid));
    }

    /**
     * init Task
     */
    public static IMaidTask getIdleTask() {
        return IDLE_TASK;
    }

    public static Map<ResourceLocation, IMaidTask> getTaskMap() {
        return TASK_MAP;
    }

    public static List<IMaidTask> getTaskIndex() {
        return TASK_INDEX;
    }

    /**
     * register Task
     */
    public void add(IMaidTask task) {
        TASK_MAP.put(task.getUid(), task);
        TASK_INDEX.add(task);
    }
}
