package com.npc.test.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.WalkToPOITask;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.village.PointOfInterestType;

import java.util.Optional;

public class VillagerTasks {
   public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> getCorePackage(VillagerProfession profession, float p_220638_1_) {
      return ImmutableList.of(Pair.of(0, new SwimTask(0.8F)), Pair.of(0, new InteractWithDoorTask()), Pair.of(0, new LookTask(45, 90)), Pair.of(0, new PanicTask()), Pair.of(0, new WakeUpTask()), Pair.of(0, new HideFromRaidOnBellRingTask()), Pair.of(0, new BeginRaidTask()), Pair.of(0, new ExpirePOITask(profession.getJobPoiType(), MemoryModuleType.JOB_SITE)), Pair.of(0, new ExpirePOITask(profession.getJobPoiType(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new net.minecraft.entity.ai.brain.task.WalkToTargetTask()), Pair.of(2, new SwitchVillagerJobTask(profession)), Pair.of(3, new TradeTask(p_220638_1_)), Pair.of(5, new PickupWantedItemTask(p_220638_1_, false, 4)), Pair.of(6, new GatherPOITask(profession.getJobPoiType(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new FindPotentialJobTask(p_220638_1_)), Pair.of(8, new FindJobTask(p_220638_1_)), Pair.of(10, new GatherPOITask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))), Pair.of(10, new GatherPOITask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))), Pair.of(10, new AssignProfessionTask()), Pair.of(10, new ChangeJobTask()));
   }

   public  WalkToTargetTask getMove(VillagerProfession profession, float p_220638_1_) {
      return new WalkToTargetTask(120, 140);
   }
//
//   public  WalkToTargetTask set(VillagerProfession profession, float p_220638_1_) {
//      return new WalkToTargetTask().set(120,100);
//   }



}