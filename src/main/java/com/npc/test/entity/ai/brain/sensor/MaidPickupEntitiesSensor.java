package com.npc.test.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaidPickupEntitiesSensor extends Sensor<NpcEntity> {
    private static final int PICKABLE_DISTANCE = 9;
    private static final int HORIZONTAL_SEARCH_RANGE = 8;
    private static final int VERTICAL_SEARCH_RANGE = 4;

    public MaidPickupEntitiesSensor() {
        super(30);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(InitEntities.VISIBLE_PICKUP_ENTITIES.get());
    }

    @Override
    protected void doTick(ServerWorld worldIn, NpcEntity maid) {
        if (!maid.isTame()) {
            return;
        }
        List<Entity> allEntities = worldIn.getEntitiesOfClass(Entity.class,
                maid.getBoundingBox().inflate(HORIZONTAL_SEARCH_RANGE, VERTICAL_SEARCH_RANGE, HORIZONTAL_SEARCH_RANGE),
                Entity::isAlive);
        allEntities.sort(Comparator.comparingDouble(maid::distanceToSqr));
        List<Entity> optional = allEntities.stream()
                .filter(e -> maid.canPickup(e, true))
                .filter(e -> e.closerThan(maid, PICKABLE_DISTANCE))
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(maid::canSee).collect(Collectors.toList());
        maid.getBrain().setMemory(InitEntities.VISIBLE_PICKUP_ENTITIES.get(), optional);
    }
}
