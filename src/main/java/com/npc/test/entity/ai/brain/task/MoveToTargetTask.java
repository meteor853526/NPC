package com.npc.test.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

public class MoveToTargetTask extends Task<MobEntity> {
   private final float speedModifier;

   public MoveToTargetTask(float speed) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.REGISTERED));
      this.speedModifier = speed;
   }

   protected void start(ServerWorld worldIn, MobEntity entityIn, long gameTimeIn) {
      LivingEntity livingentity = entityIn.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
      if (BrainUtil.canSee(entityIn, livingentity) && BrainUtil.isWithinAttackRange(entityIn, livingentity, 1)) {
         this.clearWalkTarget(entityIn);
      } else {
         this.setWalkAndLookTarget(entityIn, livingentity);
      }

   }

   private void setWalkAndLookTarget(LivingEntity p_233968_1_, LivingEntity target) {
      Brain brain = p_233968_1_.getBrain();
      brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(target, true));
      WalkTarget walktarget = new WalkTarget(new EntityPosWrapper(target, false), this.speedModifier, 0);
      brain.setMemory(MemoryModuleType.WALK_TARGET, walktarget);
   }

   private void clearWalkTarget(LivingEntity p_233967_1_) {
      p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
   }
}