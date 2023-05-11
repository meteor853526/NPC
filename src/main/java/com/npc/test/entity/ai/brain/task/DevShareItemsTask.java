package com.npc.test.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.DeliveryEntity;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DevShareItemsTask extends Task<DeliveryEntity> {
   private Set<Item> trades = ImmutableSet.of();


   public DevShareItemsTask() {
      super(ImmutableMap.of( MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.VALUE_PRESENT, InitEntities.PICKUP.get(),MemoryModuleStatus.VALUE_PRESENT));

   }

   protected boolean checkExtraStartConditions(ServerWorld worldIn, DeliveryEntity owner) {
      //System.out.println("4");

      return true;
   }

   protected boolean canStillUse(ServerWorld worldIn, DeliveryEntity entityIn, long gameTimeIn) {
      return this.checkExtraStartConditions(worldIn, entityIn);
   }

   protected void start(ServerWorld worldIn, DeliveryEntity owner, long gameTimeIn) {
      //NpcEntity NpcEntity = (NpcEntity)entityIn.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
      //BrainUtil.lockGazeAndWalkToEachOther(entityIn, NpcEntity, 0.5F);
      this.getStatus();

         throwHalfStack(owner, owner.getInventory().removeAllItems(), NpcEntity.me);

//      this.trades = figureOutWhatIAmWillingToTrade(entityIn, NpcEntity);
   }

   protected void tick(ServerWorld worldIn, DeliveryEntity owner, long gameTime) {
      //System.out.println(this.getStatus());

      //NpcEntity NpcEntity = (NpcEntity)owner.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
//      if (!(owner.distanceToSqr(NpcEntity) > 5.0D)) {
//         BrainUtil.lockGazeAndWalkToEachOther(owner, NpcEntity, 0.5F);
//         owner.gossip(worldIn, NpcEntity, gameTime);
//         if (owner.hasExcessFood() && (owner.getVillagerData().getProfession() == VillagerProfession.FARMER || NpcEntity.wantsMoreFood())) {
//            throwHalfStack(owner, NpcEntity.FOOD_POINTS.keySet(), NpcEntity);
//         }
//

      //throwHalfStack(owner, owner.getInventory().removeAllItems(), com.npc.test.passive.NpcEntity.me);

//
//         if (!this.trades.isEmpty() && owner.getInventory().hasAnyOf(this.trades)) {
//            throwHalfStack(owner, this.trades, NpcEntity);
//         }
//
//      }
   }
   private static void throwItems(PiglinEntity p_234475_0_, List<ItemStack> p_234475_1_) {
      Optional<PlayerEntity> optional = p_234475_0_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
      if (optional.isPresent()) {
         throwItemsTowardPlayer(p_234475_0_, optional.get(), p_234475_1_);
      } else {
         //throwItemsTowardRandomPos(p_234475_0_, p_234475_1_);
      }

   }
//   private static void throwItemsTowardRandomPos(PiglinEntity p_234490_0_, List<ItemStack> p_234490_1_) {
//      throwItemsTowardPos(p_234490_0_, p_234490_1_, getRandomNearbyPos(p_234490_0_));
//   }

   private static void throwItemsTowardPlayer(PiglinEntity p_234472_0_, PlayerEntity p_234472_1_, List<ItemStack> p_234472_2_) {
      throwItemsTowardPos(p_234472_0_, p_234472_2_, p_234472_1_.position());
   }

   private static void throwItemsTowardPos(PiglinEntity p_234476_0_, List<ItemStack> p_234476_1_, Vector3d p_234476_2_) {
      if (!p_234476_1_.isEmpty()) {
         p_234476_0_.swing(Hand.OFF_HAND);

         for(ItemStack itemstack : p_234476_1_) {
            BrainUtil.throwItem(p_234476_0_, itemstack, p_234476_2_.add(0.0D, 1.0D, 0.0D));
         }
      }

   }
   protected void stop(ServerWorld worldIn, NpcEntity entityIn, long gameTimeIn) {
      NpcEntity.taskID = 0;
      System.out.println(this.getStatus());
      entityIn.getBrain().eraseMemory(InitEntities.PICKUP.get());
   }



   private static void throwHalfStack(DeliveryEntity p_220586_0_, List<ItemStack> p_220586_1_, LivingEntity p_220586_2_) {
      Inventory inventory = p_220586_0_.getInventory();
      //ItemStack itemstack = ItemStack.EMPTY;
      if (!p_220586_1_.isEmpty()) {
         p_220586_0_.swing(Hand.OFF_HAND);

         for(ItemStack itemstack : p_220586_1_) {
            //BrainUtil.throwItem(p_220586_0_, itemstack, p_234476_2_.add(0.0D, 1.0D, 0.0D));
            Vector3d vector3d = new Vector3d(236,4,127);
            BrainUtil.throwItem(p_220586_0_, itemstack, vector3d);
         }
      }
//      int i = 0;
//
//      while(i < inventory.getContainerSize()) {
//         ItemStack itemstack1;
//         Item item;
//         int j;
//         label28: {
//            itemstack1 = inventory.getItem(i);
//            if (!itemstack1.isEmpty()) {
//               item = itemstack1.getItem();
//               if (p_220586_1_.contains(item)) {
//                  if (itemstack1.getCount() > itemstack1.getMaxStackSize() / 2) {
//                     j = itemstack1.getCount() / 2;
//                     break label28;
//                  }
//
//                  if (itemstack1.getCount() > 24) {
//                     j = itemstack1.getCount() - 24;
//                     break label28;
//                  }
//               }
//            }
//
//            ++i;
//            continue;
//         }
//
//         itemstack1.shrink(j);
//         itemstack = new ItemStack(item, j);
//         break;
//      }
//
//      if (!itemstack.isEmpty()) {
//         Vector3d vector3d = new Vector3d(236,4,127);
//         BrainUtil.throwItem(p_220586_0_, itemstack, vector3d);
//
//         //236,4,127
//      }

   }
}