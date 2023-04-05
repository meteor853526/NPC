package com.npc.test.passive;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.npc.test.MySQLExample;
import com.npc.test.NpcTestMod;
import com.npc.test.PlayerChatEvent;
import com.npc.test.RequestHandler;
import com.npc.test.api.event.InteractMaidEvent;
import com.npc.test.api.event.MaidTickEvent;
import com.npc.test.api.task.IMaidTask;
import com.npc.test.entity.ai.brain.NpcBrain;
import com.npc.test.entity.task.TaskManager;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.MoveToTargetTask;
import net.minecraft.entity.ai.brain.task.VillagerTasks;
import net.minecraft.entity.ai.brain.task.WalkToTargetTask;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NpcEntity<T> extends TameableEntity{
    public static final EntityType<NpcEntity> TYPE = EntityType.Builder.<NpcEntity>of(NpcEntity::new, EntityClassification.CREATURE)
            .sized(0.6f, 1.0f).clientTrackingRange(10).build("npc");
    public static final String MODEL_ID_TAG = "ModelId";
    public static long lastRequest = 0;
    public static String msg = "";
    public static BlockPos pos = null;
    public static String replay = "";


    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY);
    private static final ImmutableList<SensorType<? extends Sensor<? super VillagerEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);
    private static final DataParameter<String> DATA_TASK = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);


    public NpcEntity(EntityType<? extends NpcEntity> type, World world) {
        super(type, world);
        ((GroundPathNavigator) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
    }
//    public NpcEntity(World worldIn) {
//        this(TYPE, worldIn);
//    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MoveTowardsTargetGoal(this,100,100));
        this.goalSelector.addGoal(1,new LookAtGoal(this,PlayerEntity.class,1.0F));

    }



    @Override
    @SuppressWarnings("all")
    public Brain<NpcEntity> getBrain() {
        return (Brain<NpcEntity>) super.getBrain();
    }

    @Override
    protected Brain.BrainCodec<NpcEntity> brainProvider() {
        return Brain.provider(NpcBrain.getMemoryTypes(),NpcBrain.getSensorTypes());
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamicIn) {
        Brain<NpcEntity> brain = this.brainProvider().makeBrain(dynamicIn);
        NpcBrain.registerBrainGoals(brain, this);
        return brain;
    }

    public void refreshBrain(ServerWorld serverWorldIn) {
        Brain<NpcEntity> brain = this.getBrain();
        brain.stopAll(serverWorldIn, this);
        this.brain = brain.copyWithoutBehaviors();
        NpcBrain.registerBrainGoals(this.getBrain(), this);
    }

    @Override
    protected void customServerAiStep() {

        super.customServerAiStep();
        this.level.getProfiler().push("NpcBrain");
        this.getBrain().tick((ServerWorld) this.level, this);
        this.level.getProfiler().pop();
    }
    @Override
    public void tick() {
        super.tick();
        if(pos != null){

            BrainUtil.setWalkAndLookTargetMemories((LivingEntity) this.getEntity(),pos,1,1);
            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(pos,1,1));
            //pos = null;
            //System.out.println(replay);

        }
    }
    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    public ActionResultType testTask(Brain<VillagerEntity> villagerBrain,PlayerEntity p_230254_1_, Hand p_230254_2_){
        //npc.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(pos));
        //this.moveTo(pos,100,100);
        villagerBrain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 120, 120));
        new WalkToTargetTask(120,120);
        //villagerBrain.addActivity(WalkToTargetTask());
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }
    @Override
    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        Thread t = new Thread(() -> {
            try {
                MySQLExample sqlExample = new MySQLExample();
                String response = RequestHandler.getAIResponse("你現在是一個minecraft裡的npc，根據以下的value請敘述出設定"+sqlExample.getData()+"並敘述出你的人物");
                p_230254_1_.sendMessage(new StringTextComponent(response),null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        //testTask(this.getBrain(),p_230254_1_,p_230254_2_);
//        BlockPos playerPos = player.blockPosition();
//        Vector3d pl = player.position();
        //this.moveTo(pos,100,100);
//        if(pos != null){
//            BrainUtil.setWalkAndLookTargetMemories((LivingEntity) this.getEntity(),pos,1,1);
//            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(pos,1,1));
//        }
        if (!this.level.isClientSide()) {


            if(msg != ""){
                //p_230254_1_.sendMessage(new StringTextComponent(msg),null);
                //p_230254_1_.displayClientMessage(new StringTextComponent("[AIMobs] Error getting response"),true);
                //System.out.println("NPC " + p_230254_1_.getName() + " click");
                //p_230254_1_.getShoulderEntityRight();
                //getResponse(p_230254_1_,p_230254_2_);
//                lastRequest = System.currentTimeMillis();
//                Thread t = new Thread(() -> {
//                    try {
//
//                        String response = RequestHandler.getAIResponse(msg);
//                        replay = response;
//                        p_230254_1_.sendMessage(new StringTextComponent("ChatGPT:"+ response),null);
//                        this.playSound(SoundEvents.VILLAGER_TRADE, 100, 100);
//                        // player.sendMessage(Text.of("<" + entityName + "> " + response));
//                        //prompts += response + "\"\n";
//                    } catch (Exception e) {
//                        p_230254_1_.sendMessage(new StringTextComponent("[AIMobs] Error getting response"),null);
//                        e.printStackTrace();
//                    } finally {
//                        //hideWaitMessage();
//                    }
//                });
//                t.start();
            }

        }
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    @Override
    protected void pushEntities() {
        super.pushEntities();
        //
//        if (this.isPickup() && this.isTame()) {
//            List<Entity> entityList = this.level.getEntities(this,
//                    this.getBoundingBox().inflate(0.5, 0, 0.5), this::canPickup);
//            if (!entityList.isEmpty() && this.isAlive()) {
//                for (Entity entityPickup : entityList) {
//                    //
//                    if (entityPickup instanceof ItemEntity) {
//                        pickupItem((ItemEntity) entityPickup, false);
//                    }
//                    //
//                    if (entityPickup instanceof ExperienceOrbEntity) {
//                        pickupXPOrb((ExperienceOrbEntity) entityPickup);
//                    }
//                    //
//                    if (entityPickup instanceof EntityPowerPoint) {
//                        pickupPowerPoint((EntityPowerPoint) entityPickup);
//                    }
//                    //
//                    if (entityPickup instanceof AbstractArrowEntity) {
//                        pickupArrow((AbstractArrowEntity) entityPickup, false);
//                    }
//                }
//            }
//        }
    }
    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean result = super.doHurtTarget(entityIn);
        this.getMainHandItem().hurtAndBreak(1, this, (maid) -> maid.broadcastBreakEvent(Hand.MAIN_HAND));
        return result;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
//        if (MinecraftForge.EVENT_BUS.post(new MaidAttackEvent(this, source, amount))) {
//            return false;
//        }
//        if (source.getEntity() instanceof PlayerEntity && this.isOwnedBy((PlayerEntity) source.getEntity())) {
//            //
//            amount = MathHelper.clamp(amount / 5, 0, 2);
//        }
        return super.hurt(source, amount);
    }

//    @Override
//    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
//        if (!this.isInvulnerableTo(damageSrc)) {
//            MaidHurtEvent maidHurtEvent = new MaidHurtEvent(this, damageSrc, damageAmount);
//            damageAmount = MinecraftForge.EVENT_BUS.post(maidHurtEvent) ? 0 : maidHurtEvent.getAmount();
//            damageAmount = ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
//            if (damageAmount > 0) {
//                damageAmount = this.getDamageAfterArmorAbsorb(damageSrc, damageAmount);
//                damageAmount = this.getDamageAfterMagicAbsorb(damageSrc, damageAmount);
//                float damageAfterAbsorption = Math.max(damageAmount - this.getAbsorptionAmount(), 0);
//                this.setAbsorptionAmount(this.getAbsorptionAmount() - (damageAmount - damageAfterAbsorption));
//                float damageDealtAbsorbed = damageAmount - damageAfterAbsorption;
//                if (0 < damageDealtAbsorbed && damageDealtAbsorbed < (Float.MAX_VALUE / 10) && damageSrc.getEntity() instanceof ServerPlayerEntity) {
//                    ((ServerPlayerEntity) damageSrc.getEntity()).awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(damageDealtAbsorbed * 10));
//                }
//                MaidDamageEvent maidDamageEvent = new MaidDamageEvent(this, damageSrc, damageAfterAbsorption);
//                damageAfterAbsorption = MinecraftForge.EVENT_BUS.post(maidDamageEvent) ? 0 : maidDamageEvent.getAmount();
//                damageAfterAbsorption = ForgeHooks.onLivingDamage(this, damageSrc, damageAfterAbsorption);
//                if (damageAfterAbsorption != 0) {
//                    float health = this.getHealth();
//                    this.getCombatTracker().recordDamage(damageSrc, health, damageAfterAbsorption);
//                    this.setHealth(health - damageAfterAbsorption);
//                    this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAfterAbsorption);
//                }
//            }
//        }
//    }

    @Override
    public void die(DamageSource cause) {
//        if (!MinecraftForge.EVENT_BUS.post(new MaidDeathEvent(this, cause))) {
//            super.die(cause);
//        }
        super.die(cause);
    }

//    public void sendItemBreakMessage(ItemStack stack) {
//        if (!this.level.isClientSide) {
//            NetworkHandler.sendToNearby(this, new ItemBreakMessage(this.getId(), stack));
//        }
//    }

//    @Override
//    public void addAdditionalSaveData(CompoundNBT compound) {
//        super.addAdditionalSaveData(compound);
//        compound.putString(MODEL_ID_TAG, getModelId());
//        compound.putString(TASK_TAG, getTask().getUid().toString());
//        compound.putBoolean(PICKUP_TAG, isPickup());
//        compound.putBoolean(HOME_TAG, isHomeModeEnable());
//        compound.putBoolean(RIDEABLE_TAG, isRideable());
//        compound.putInt(BACKPACK_LEVEL_TAG, getBackpackLevel());
//        compound.put(MAID_INVENTORY_TAG, maidInv.serializeNBT());
//        compound.put(MAID_BAUBLE_INVENTORY_TAG, maidBauble.serializeNBT());
//        compound.putBoolean(STRUCK_BY_LIGHTNING_TAG, isStruckByLightning());
//        compound.putBoolean(INVULNERABLE_TAG, getIsInvulnerable());
//        compound.putInt(HUNGER_TAG, getHunger());
//        compound.putInt(FAVORABILITY_TAG, getFavorability());
//        compound.putInt(EXPERIENCE_TAG, getExperience());
//        compound.putString(SCHEDULE_MODE_TAG, getSchedule().name());
//        compound.put(RESTRICT_CENTER_TAG, NBTUtil.writeBlockPos(getRestrictCenter()));
//    }

//    public boolean isBegging() {
//        return this.entityData.get(DATA_BEGGING);
//    }
    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    public boolean canPathReach(BlockPos pos) {
        Path path = this.getNavigation().createPath(pos, 0);
        return path != null && path.canReach();
    }

    public boolean canPathReach(Entity entity) {
        Path path = this.getNavigation().createPath(entity, 0);
        return path != null && path.canReach();
    }


    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
//        this.entityData.define(DATA_MODEL_ID, DEFAULT_MODEL_ID);
//        this.entityData.define(DATA_TASK, TaskIdle.UID.toString());
//        this.entityData.define(DATA_BEGGING, false);
//        this.entityData.define(DATA_PICKUP, true);
//        this.entityData.define(DATA_HOME_MODE, false);
//        this.entityData.define(DATA_RIDEABLE, true);
//        this.entityData.define(DATA_INVULNERABLE, false);
//        this.entityData.define(DATA_BACKPACK_LEVEL, 0);
//        this.entityData.define(DATA_HUNGER, 0);
//        this.entityData.define(DATA_FAVORABILITY, 0);
//        this.entityData.define(DATA_EXPERIENCE, 0);
//        this.entityData.define(DATA_STRUCK_BY_LIGHTNING, false);
//        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
//        this.entityData.define(DATA_ARM_RISE, false);
//        this.entityData.define(SCHEDULE_MODE, MaidSchedule.DAY);
//        this.entityData.define(RESTRICT_CENTER, BlockPos.ZERO);
//        this.entityData.define(RESTRICT_RADIUS, MaidConfig.MAID_HOME_RANGE.get().floatValue());
//        this.entityData.define(CHAT_BUBBLE, MaidChatBubbles.DEFAULT);
    }

    @Override
    public Entity getEntity() {
        return super.getEntity();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public boolean shouldRiderSit() {
        return super.shouldRiderSit();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return super.getPickedResult(target);
    }

    @Override
    public boolean canRiderInteract() {
        return super.canRiderInteract();
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return super.canBeRiddenInWater(rider);
    }

    @Override
    public EntityClassification getClassification(boolean forSpawnCount) {
        return super.getClassification(forSpawnCount);
    }

    @Override
    public boolean isMultipartEntity() {
        return super.isMultipartEntity();
    }

    @Nullable
    @Override
    public PartEntity<?>[] getParts() {
        return super.getParts();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return super.getCapability(cap);
    }

}
