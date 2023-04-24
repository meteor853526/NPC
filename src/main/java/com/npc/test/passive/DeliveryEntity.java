package com.npc.test.passive;

import com.mojang.serialization.Dynamic;
import com.npc.test.entity.ai.brain.DeliveryBrain;
import com.npc.test.entity.ai.brain.NpcBrain;
import com.npc.test.entity.chatbubble.ChatBubbleManger;
import com.npc.test.entity.chatbubble.ChatText;
import com.npc.test.entity.chatbubble.DeliveryChatBubbleManger;
import com.npc.test.entity.chatbubble.MaidChatBubbles;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.WalkToTargetTask;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DeliveryEntity<T> extends TameableEntity {


    public static final EntityType<NpcEntity> TYPE = EntityType.Builder.<NpcEntity>of(NpcEntity::new, EntityClassification.CREATURE)
            .sized(0.6f, 1.0f).clientTrackingRange(10).build("npc");
    public static final String MODEL_ID_TAG = "ModelId";
    public static long lastRequest = 0;
    public static String msg = "";

    public static BlockPos pos = null;
    public static String replay = "";

    private static final DataParameter<MaidChatBubbles> CHAT_BUBBLE = EntityDataManager.defineId(DeliveryEntity.class, MaidChatBubbles.DATA);
    private static final DataParameter<String> DATA_MODEL_ID = EntityDataManager.defineId(DeliveryEntity.class, DataSerializers.STRING);
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    public DeliveryEntity(EntityType<? extends DeliveryEntity> type, World world) {
        super(type, world);
        ((GroundPathNavigator) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
    }
//    public DeliveryEntity(World worldIn) {
//        this(TYPE, worldIn);
//    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MoveTowardsTargetGoal(this,100,100));
        this.goalSelector.addGoal(1,new LookAtGoal(this, PlayerEntity.class,1.0F));

    }

    @Override
    @SuppressWarnings("all")
    public Brain<DeliveryEntity> getBrain() {
        return (Brain<DeliveryEntity>) super.getBrain();
    }

    @Override
    protected Brain.BrainCodec<DeliveryEntity> brainProvider() {
        return Brain.provider(NpcBrain.getMemoryTypes(), DeliveryBrain.getSensorTypes());
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamicIn) {
        Brain<DeliveryEntity> brain = this.brainProvider().makeBrain(dynamicIn);
        DeliveryBrain.registerBrainGoals(brain, this);
        return brain;
    }

    public void refreshBrain(ServerWorld serverWorldIn) {
        Brain<DeliveryEntity> brain = this.getBrain();
        brain.stopAll(serverWorldIn, this);
        this.brain = brain.copyWithoutBehaviors();
        DeliveryBrain.registerBrainGoals(this.getBrain(), this);
    }
    public String getModelId() {
        return this.entityData.get(DATA_MODEL_ID);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
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
        if (!level.isClientSide) {
            DeliveryChatBubbleManger.tick(this);
        }
        DeliveryChatBubbleManger.tick(this);
    }

    public ActionResultType testTask(Brain<VillagerEntity> villagerBrain, PlayerEntity p_230254_1_, Hand p_230254_2_){
        //npc.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(pos));
        //this.moveTo(pos,100,100);
        villagerBrain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 120, 120));
        new WalkToTargetTask(120,120);
        //villagerBrain.addActivity(WalkToTargetTask());
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }


    public MaidChatBubbles getChatBubble() {
        return this.entityData.get(CHAT_BUBBLE);
    }



    public void addChatBubble(long endTime, ChatText text) {
        DeliveryChatBubbleManger.addChatBubble(endTime, text, this);
    }

    public void setChatBubble(MaidChatBubbles bubbles) {
        this.entityData.set(CHAT_BUBBLE, bubbles);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MODEL_ID, DEFAULT_MODEL_ID);
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
        this.entityData.define(CHAT_BUBBLE, MaidChatBubbles.DEFAULT);
    }


    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    public Entity getEntity() {
        return super.getEntity();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return super.getPickedResult(target);
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
