package com.npc.test.entity.ai.brain.task;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.function.Predicate;

public class MaidPickupEntitiesTask extends Task<NpcEntity> {
    private final Predicate<NpcEntity> predicate;
    private final int maxDistToWalk;
    private final float speedModifier;

    public MaidPickupEntitiesTask(int maxDistToWalk, float speedModifier) {
        this(Predicates.alwaysTrue(), maxDistToWalk, speedModifier);
    }

    public MaidPickupEntitiesTask(Predicate<NpcEntity> predicate, int maxDistToWalk, float speedModifier) {
        super(ImmutableMap.of(InitEntities.VISIBLE_PICKUP_ENTITIES.get(), MemoryModuleStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        this.predicate = predicate;
        this.maxDistToWalk = maxDistToWalk;
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, NpcEntity owner) {
        return owner.isTame() && predicate.test(owner);
    }

    @Override
    protected void start(ServerWorld worldIn, NpcEntity maid, long gameTimeIn) {
        getItems(maid).stream()
                .filter(e -> e.closerThan(maid, maxDistToWalk) && e.isAlive() && !e.isInWater())
                .filter(maid::canPathReach).findFirst()
                .ifPresent(e -> BrainUtil.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0));
        System.out.println("?????????");
    }

    private List<Entity> getItems(NpcEntity maid) {
        return (List<Entity>) maid.getBrain().getMemory(InitEntities.VISIBLE_PICKUP_ENTITIES.get()).orElse(Lists.newArrayList());
    }
}
