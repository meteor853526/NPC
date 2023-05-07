package com.npc.test.init;


import com.npc.test.NpcTestMod;
import com.npc.test.entity.ai.brain.sensor.MaidPickupEntitiesSensor;
import com.npc.test.entity.chatbubble.MaidChatBubbles;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.schedule.ScheduleBuilder;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.IPosWrapper;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntities {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, NpcTestMod.MOD_ID);

    //public static RegistryObject<MemoryModuleType<IPosWrapper>> TARGET_POS = MEMORY_MODULE_TYPES.register("target_pos", () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, NpcTestMod.MOD_ID);
    public static RegistryObject<DataSerializerEntry> MAID_CHAT_BUBBLE_DATA_SERIALIZERS = DATA_SERIALIZERS.register("maid_chat_bubble", () -> new DataSerializerEntry(MaidChatBubbles.DATA));
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, NpcTestMod.MOD_ID);
    public static RegistryObject<MemoryModuleType<List<Entity>>> VISIBLE_PICKUP_ENTITIES = MEMORY_MODULE_TYPES.register("visible_pickup_entities", () -> new MemoryModuleType<>(Optional.empty()));

    public static RegistryObject<SensorType<MaidPickupEntitiesSensor>> MAID_PICKUP_ENTITIES_SENSOR = SENSOR_TYPES.register("maid_pickup_entities", () -> new SensorType<>(MaidPickupEntitiesSensor::new));
    @SubscribeEvent
    public static void addEntityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(NpcEntity.TYPE,LivingEntity.createLivingAttributes().build());

    }
}
