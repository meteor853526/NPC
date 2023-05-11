package com.npc.test.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
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

public class WalkToTargetTask extends Task<MobEntity> {
   private int remainingCooldown;
   @Nullable
   private Path path;
   @Nullable
   private BlockPos lastTargetPos;
   private float speedModifier;

   private EntityType<?> entityType;


   public WalkToTargetTask(EntityType<?> entity) {
      this(150, 250);
      this.entityType = entity;
   }

   public WalkToTargetTask(int p_i241908_1_, int p_i241908_2_) {
      super(ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleStatus.REGISTERED, MemoryModuleType.PATH, MemoryModuleStatus.VALUE_ABSENT, WALK_TARGET, MemoryModuleStatus.VALUE_PRESENT), p_i241908_1_, p_i241908_2_);
   }

   protected boolean checkExtraStartConditions(ServerWorld worldIn, MobEntity owner) {
      if (this.remainingCooldown > 0) {
         --this.remainingCooldown;
         return false;
      } else {
         Brain<?> brain = owner.getBrain();
         WalkTarget walktarget = brain.getMemory(WALK_TARGET).get();

         boolean flag = this.reachedTarget(owner, walktarget);
         if (!flag && this.tryComputePath(owner, walktarget, worldIn.getGameTime())) {
            this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
            //System.out.println("check");
            return true;
         } else {
            brain.eraseMemory(WALK_TARGET);
            if (flag) {
               brain.eraseMemory(InitEntities.TASK_ID.get());
               System.out.println(brain.getMemory(InitEntities.TASK_ID.get()) + "????");
               brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
               brain.setMemory(InitEntities.LOCK.get(),true);
               System.out.println(this.getStatus());

            }

            return false;
         }
      }
   }

   protected boolean canStillUse(ServerWorld worldIn, MobEntity entityIn, long gameTimeIn) {

      if (this.path != null && this.lastTargetPos != null) {
         Optional<WalkTarget> optional = entityIn.getBrain().getMemory(WALK_TARGET);
         PathNavigator pathnavigator = entityIn.getNavigation();
         return !pathnavigator.isDone() && optional.isPresent() && !this.reachedTarget(entityIn, optional.get());
      } else {
         return false;
      }
   }

   protected void stop(ServerWorld worldIn, MobEntity entityIn, long gameTimeIn) {
      if (entityIn.getBrain().hasMemoryValue(WALK_TARGET) && !this.reachedTarget(entityIn, entityIn.getBrain().getMemory(WALK_TARGET).get()) && entityIn.getNavigation().isStuck()) {
         this.remainingCooldown = worldIn.getRandom().nextInt(40);
      }

      entityIn.getNavigation().stop();
      entityIn.getBrain().eraseMemory(WALK_TARGET);
      entityIn.getBrain().eraseMemory(MemoryModuleType.PATH);
      //NpcEntity.pos = null;
      //System.out.println("??????????????????????????????");
      this.path = null;
   }

   protected void start(ServerWorld worldIn, MobEntity entityIn, long gameTimeIn) {

      entityIn.getBrain().setMemory(MemoryModuleType.PATH, this.path);
      entityIn.getNavigation().moveTo(this.path, (double)this.speedModifier);

   }

//   public void set(MobEntity entityIn, Vector3d pos){
//      entityIn.getBrain().setMemory(WALK_TARGET, new WalkTarget(pos, 120, 120));
//   }

   protected void tick(ServerWorld worldIn, MobEntity owner, long gameTime) {
      Path path = owner.getNavigation().getPath();
      Brain<?> brain = owner.getBrain();

      if (NpcEntity.pos != null) {

         this.lastTargetPos = NpcEntity.pos;
      }
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