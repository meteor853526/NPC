package com.npc.test.passive;

import com.npc.test.NpcTestMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.Path;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class NpcEntity<T> extends VillagerEntity {

    public static final String MODEL_ID_TAG = "ModelId";

    public NpcEntity(EntityType<? extends VillagerEntity> p_i50182_1_, World p_i50182_2_) {
        super(p_i50182_1_, p_i50182_2_);
    }


    public NpcEntity(EntityType<? extends VillagerEntity> p_i50183_1_, World p_i50183_2_, VillagerType p_i50183_3_) {
        super(p_i50183_1_, p_i50183_2_, p_i50183_3_);
    }

    private void setUnhappy() {
        this.setUnhappyCounter(40);
        if (!this.level.isClientSide()) {
            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
        }

    }
    @Override
    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);

//        BlockPos playerPos = player.blockPosition();
//        Vector3d pl = player.position();

        if (!this.level.isClientSide()) {

            System.out.println("NPC " + p_230254_1_.getName() + " click");

            this.moveTo(p_230254_1_.getPosition(10));
            this.playSound(SoundEvents.VEX_DEATH, 100, 100);

            p_230254_1_.sendMessage(new StringTextComponent("No Home Position has been set!"),null);
        }

        if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.isTrading() && !this.isSleeping() && !p_230254_1_.isSecondaryUseActive()) {
            if (this.isBaby()) {
                this.setUnhappy();
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            } else {
                boolean flag = this.getOffers().isEmpty();
                if (p_230254_2_ == Hand.MAIN_HAND) {
                    if (flag && !this.level.isClientSide) {
                        this.setUnhappy();
                    }

                    p_230254_1_.awardStat(Stats.TALKED_TO_VILLAGER);
                }

                if (flag) {
                    return ActionResultType.sidedSuccess(this.level.isClientSide);
                } else {
                    if (!this.level.isClientSide && !this.offers.isEmpty()) {
                        //this.startTrading(p_230254_1_);
                    }

                    return ActionResultType.sidedSuccess(this.level.isClientSide);
                }
            }
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }
    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    public boolean canPathReach(BlockPos pos) {
        Path path = this.getNavigation().createPath(pos, 0);
        return path != null && path.canReach();
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

}
