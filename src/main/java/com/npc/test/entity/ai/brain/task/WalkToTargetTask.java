package com.npc.test.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.util.math.IPosWrapper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.WALK_TARGET;

public class WalkToTargetTask extends Task<NpcEntity> {
   private int remainingCooldown;
   @Nullable
   private Path path;
   @Nullable
   private BlockPos lastTargetPos;
   private float speedModifier;

   public WalkToTargetTask() {
      this(150, 250);
   }

   public WalkToTargetTask(int p_i241908_1_, int p_i241908_2_) {
      super(ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleStatus.REGISTERED, MemoryModuleType.PATH, MemoryModuleStatus.VALUE_ABSENT, WALK_TARGET, MemoryModuleStatus.VALUE_PRESENT), p_i241908_1_, p_i241908_2_);
   }

   protected boolean checkExtraStartConditions(ServerWorld worldIn, NpcEntity owner) {
      if (this.remainingCooldown > 0) {
         --this.remainingCooldown;
         return false;
      } else {
         Brain<?> brain = owner.getBrain();
         WalkTarget walktarget = brain.getMemory(WALK_TARGET).get();
         boolean flag = this.reachedTarget(owner, walktarget);
         if (!flag && this.tryComputePath(owner, walktarget, worldIn.getGameTime())) {
            this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
            return true;
         } else {
            brain.eraseMemory(WALK_TARGET);
            if (flag) {
               brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            }

            return false;
         }
      }
   }

   protected boolean canStillUse(ServerWorld worldIn, NpcEntity entityIn, long gameTimeIn) {
      if (this.path != null && this.lastTargetPos != null) {
         Optional<WalkTarget> optional = entityIn.getBrain().getMemory(WALK_TARGET);
         PathNavigator pathnavigator = entityIn.getNavigation();
         return !pathnavigator.isDone() && optional.isPresent() && !this.reachedTarget(entityIn, optional.get());
      } else {
         return false;
      }
   }

   protected void stop(ServerWorld worldIn, NpcEntity entityIn, long gameTimeIn) {
      if (entityIn.getBrain().hasMemoryValue(WALK_TARGET) && !this.reachedTarget(entityIn, entityIn.getBrain().getMemory(WALK_TARGET).get()) && entityIn.getNavigation().isStuck()) {
         this.remainingCooldown = worldIn.getRandom().nextInt(40);
      }

      entityIn.getNavigation().stop();
      entityIn.getBrain().eraseMemory(WALK_TARGET);
      entityIn.getBrain().eraseMemory(MemoryModuleType.PATH);
      this.path = null;
   }

   protected void start(ServerWorld worldIn, NpcEntity entityIn, long gameTimeIn) {
      entityIn.getBrain().setMemory(MemoryModuleType.PATH, this.path);
      entityIn.getNavigation().moveTo(this.path, (double)this.speedModifier);
   }

   public void set(NpcEntity entityIn, Vector3d pos){
      entityIn.getBrain().setMemory(WALK_TARGET, new WalkTarget(pos, 120, 120));
   }

   protected void tick(ServerWorld worldIn, NpcEntity owner, long gameTime) {
      Path path = owner.getNavigation().getPath();
      Brain<?> brain = owner.getBrain();
      if (this.path != path) {
         this.path = path;
         brain.setMemory(MemoryModuleType.PATH, path);
      }

      if (path != null && this.lastTargetPos != null) {
         WalkTarget walktarget = brain.getMemory(WALK_TARGET).get();
         if (walktarget.getTarget().currentBlockPosition().distSqr(this.lastTargetPos) > 4.0D && this.tryComputePath(owner, walktarget, worldIn.getGameTime())) {
            this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
            this.start(worldIn, owner, gameTime);
         }

      }
   }

   private boolean tryComputePath(MobEntity p_220487_1_, WalkTarget p_220487_2_, long p_220487_3_) {
      BlockPos blockpos = p_220487_2_.getTarget().currentBlockPosition();
      this.path = p_220487_1_.getNavigation().createPath(blockpos, 0);
      this.speedModifier = p_220487_2_.getSpeedModifier();
      Brain<?> brain = p_220487_1_.getBrain();
      if (this.reachedTarget(p_220487_1_, p_220487_2_)) {
         brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
      } else {
         boolean flag = this.path != null && this.path.canReach();
         if (flag) {
            brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
         } else if (!brain.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
            brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, p_220487_3_);
         }

         if (this.path != null) {
            return true;
         }

         Vector3d vector3d = RandomPositionGenerator.getPosTowards((CreatureEntity)p_220487_1_, 10, 7, Vector3d.atBottomCenterOf(blockpos));
         if (vector3d != null) {
            this.path = p_220487_1_.getNavigation().createPath(vector3d.x, vector3d.y, vector3d.z, 0);
            return this.path != null;
         }
      }

      return false;
   }

   private boolean reachedTarget(MobEntity p_220486_1_, WalkTarget p_220486_2_) {
      return p_220486_2_.getTarget().currentBlockPosition().distManhattan(p_220486_1_.blockPosition()) <= p_220486_2_.getCloseEnoughDist();
   }
}