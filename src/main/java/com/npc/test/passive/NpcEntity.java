package com.npc.test.passive;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.npc.test.entity.ai.brain.NpcBrain;
import com.npc.test.entity.ai.brain.task.ShareItemsTask;
import com.npc.test.entity.chatbubble.ChatBubbleManger;
import com.npc.test.entity.chatbubble.ChatText;
import com.npc.test.entity.chatbubble.MaidChatBubbles;

import com.npc.test.init.InitEntities;
import com.npc.test.init.InitItems;
import com.npc.test.inventory.container.MaidConfigContainer;
import com.npc.test.inventory.container.MaidMainContainer;
import com.npc.test.inventory.handler.MaidBackpackHandler;
import com.npc.test.item.ItemMaidBackpack;
import com.npc.test.util.ItemsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.WalkToTargetTask;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class NpcEntity<T> extends TameableEntity{
    public static final EntityType<NpcEntity> TYPE = EntityType.Builder.<NpcEntity>of(NpcEntity::new, EntityClassification.CREATURE)
            .sized(0.6f, 1.0f).clientTrackingRange(10).build("npc");
    public static final String MODEL_ID_TAG = "ModelId";
    public static long lastRequest = 0;

    public static BlockPos pos = null;
    private int backpackDelay = 0;
    private final ItemStackHandler maidInv = new MaidBackpackHandler(36);
    public boolean guiOpening = false;

    public static PlayerEntity me;
    public static final String BACKPACK_LEVEL_TAG = "MaidBackpackLevel";
    private static final String PICKUP_TAG = "MaidIsPickup";
    private static final DataParameter<Integer> DATA_BACKPACK_LEVEL = EntityDataManager.defineId(NpcEntity.class, DataSerializers.INT);

    private static final DataParameter<Boolean> DATA_PICKUP = EntityDataManager.defineId(NpcEntity.class, DataSerializers.BOOLEAN);
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY);
    private static final ImmutableList<SensorType<? extends Sensor<? super VillagerEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);

    private static final DataParameter<String> DATA_TASK = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    private static final DataParameter<MaidChatBubbles> CHAT_BUBBLE = EntityDataManager.defineId(NpcEntity.class, MaidChatBubbles.DATA);
    private static final DataParameter<String> DATA_MODEL_ID = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";

    private final Inventory inventory = new Inventory(8);

    public static int taskID=0;

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
        if(taskID == 1){
            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(pos,1,1));
        }else if (taskID == 2){
            this.getBrain().setMemory(InitEntities.PICKUP.get(),true);
        }
        //this.getBrain().setMemory(InitEntities.PICKUP.get(),false);
//        this.getBrain().setMemory(InitEntities.SERVICE_CHECK.get(),true);
//        this.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER,me);
//        if(taskID == 1 ){
// //           BrainUtil.setWalkAndLookTargetMemories((LivingEntity) this.getEntity(),pos,1,1);
//            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(pos,1,1));
//            if(this.getBrain().getMemory(InitEntities.LOCK.get()).orElse(false)){
//                this.getBrain().setMemory(InitEntities.TASK_ID.get(),1);
//            }
//            taskID = this.getBrain().getMemory(InitEntities.TASK_ID.get()).orElse(0);
//            System.out.println(taskID);
//        }else if (taskID == 2){
//            this.getBrain().setMemory(InitEntities.PICKUP.get(),true);
//            this.getBrain().setMemory(InitEntities.TASK_ID.get(),2);
//        }
        //testTask();
    }
    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide) {
            ChatBubbleManger.tick(this);
        }
        ChatBubbleManger.tick(this);
    }

    public ActionResultType testTask(){

        if(taskID == 1){
            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(pos,1,1));
            this.getBrain().setMemory(InitEntities.TASK_ID.get(),1);
        }


//        //npc.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(pos));
//        //this.moveTo(pos,100,100);
//        villagerBrain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 120, 120));
//        new WalkToTargetTask(120,120);
//        //villagerBrain.addActivity(WalkToTargetTask());
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }






    @Override
    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {

        //testTask(this.getBrain(),p_230254_1_,p_230254_2_);
//        BlockPos playerPos = player.blockPosition();
//        Vector3d pl = player.position();
        //this.moveTo(pos,100,100);
//        if(pos != null){
//            BrainUtil.setWalkAndLookTargetMemories((LivingEntity) this.getEntity(),pos,1,1);
//            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(pos,1,1));
//        }
        openMaidGui(p_230254_1_);
        me = p_230254_1_;
        if (!this.level.isClientSide()) {


            //if(msg != ""){
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
           // }

        }
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    @Override
    protected void pushEntities() {
        super.pushEntities();

        List<Entity> entityList = this.level.getEntities(this,
                this.getBoundingBox().inflate(0.5, 0, 0.5), this::canPickup);
        if (!entityList.isEmpty() && this.isAlive()) {
            for (Entity entityPickup : entityList) {
                // 如果是物品
                if (entityPickup instanceof ItemEntity) {
                    pickupItem((ItemEntity) entityPickup, false);

                }
//                // 如果是经验
//                if (entityPickup instanceof ExperienceOrbEntity) {
//                    pickupXPOrb((ExperienceOrbEntity) entityPickup);
//                }
//                // 如果是 P 点
//                if (entityPickup instanceof EntityPowerPoint) {
//                    pickupPowerPoint((EntityPowerPoint) entityPickup);
//                }
//                // 如果是箭
//                if (entityPickup instanceof AbstractArrowEntity) {
//                    pickupArrow((AbstractArrowEntity) entityPickup, false);
//                }
            }
        }
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
    public boolean isPickup() {
        return this.entityData.get(DATA_PICKUP);
    }

    public void setPickup(boolean isPickup) {
        this.entityData.set(DATA_PICKUP, isPickup);
    }
    public boolean canPickup(Entity pickupEntity, boolean checkInWater) {

        return taskID != 2;
    }
    public boolean canPickup(Entity pickupEntity) {

        return canPickup(pickupEntity, false);
    }
    public boolean pickupItem(ItemEntity entityItem, boolean simulate) {
        ItemStack itemstack = entityItem.getItem();
        boolean flag = inventory.canAddItem(itemstack);
        if (!flag) {
            return false;
        }
        //this.setItemSlot(equipmentslottype, itemstack);
        this.onItemPickup(entityItem);
        this.take(entityItem, itemstack.getCount());
        System.out.println(itemstack.getCount());
        ItemStack itemstack1 = inventory.addItem(itemstack);
        if (itemstack1.isEmpty()) {
            entityItem.remove();
        } else {
            itemstack.setCount(itemstack1.getCount());
        }
        return true;

//        if (!level.isClientSide && entityItem.isAlive() && !entityItem.hasPickUpDelay()) {
//            // 获取实体的物品堆
//            ItemStack itemstack = entityItem.getItem();
//            this.take(entityItem, itemstack.getCount());
            // 检查物品是否合法
//            if (!canInsertItem(itemstack)) {
//                return false;
//            }
            // 获取数量，为后面方面用
//            int count = itemstack.getCount();
//            itemstack = ItemHandlerHelper.insertItemStacked(getAvailableInv(false), itemstack, simulate);
//            if (count == itemstack.getCount()) {
//                return false;
//            }

//            if (!simulate) {
//                // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
//                this.take(entityItem, count - itemstack.getCount());
//                if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
//                    pickupSoundCount--;
//                    if (pickupSoundCount == 0) {
//                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
//                        pickupSoundCount = 5;
//                    }
//                }
//                // 如果遍历塞完后发现为空了
//                if (itemstack.isEmpty()) {
//                    // 清除这个实体
//                    entityItem.remove();
//                } else {
//                    // 将物品数量同步到客户端
//                    entityItem.setItem(itemstack);
//                }
//            }
//            return true;
//        }
//        return false;
    }
    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean openMaidGui(PlayerEntity player) {
        return openMaidGui(player, TabIndex.MAIN);
    }

    public boolean openMaidGui(PlayerEntity player, int tabIndex) {
        if (player instanceof ServerPlayerEntity && !this.isSleeping()) {
            this.navigation.stop();
            NetworkHooks.openGui((ServerPlayerEntity) player, getGuiProvider(tabIndex), (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    private INamedContainerProvider getGuiProvider(int tabIndex) {
        switch (tabIndex) {
            case TabIndex.CONFIG:
                return MaidConfigContainer.create(getId());
            case TabIndex.MAIN:
            default:
                return MaidMainContainer.create(getId());
        }
    }

    public boolean backpackHasDelay() {
        return backpackDelay > 0;
    }

    public void setBackpackDelay() {
        backpackDelay = 20;
    }
    public static boolean canInsertItem(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            return !(block instanceof ShulkerBoxBlock);
        }
        //return stack.getItem() != InitItems.PHOTO.get();
        return true;
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


    @Override
    public void die(DamageSource cause) {
//        if (!MinecraftForge.EVENT_BUS.post(new MaidDeathEvent(this, cause))) {
//            super.die(cause);
//        }
        super.die(cause);
    }

    public MaidChatBubbles getChatBubble() {
        return this.entityData.get(CHAT_BUBBLE);
    }



    public void addChatBubble(long endTime, ChatText text) {
        ChatBubbleManger.addChatBubble(endTime, text, this);
    }

    public void setChatBubble(MaidChatBubbles bubbles) {
        this.entityData.set(CHAT_BUBBLE, bubbles);
    }

    public String getModelId() {
        return this.entityData.get(DATA_MODEL_ID);
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
            this.entityData.define(DATA_MODEL_ID, DEFAULT_MODEL_ID);
//        this.entityData.define(DATA_TASK, TaskIdle.UID.toString());
//        this.entityData.define(DATA_BEGGING, false);
          this.entityData.define(DATA_PICKUP, true);
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
    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
//        if (compound.contains(MODEL_ID_TAG, Constants.NBT.TAG_STRING)) {
//            setModelId(compound.getString(MODEL_ID_TAG));
//        }
//        if (compound.contains(TASK_TAG, Constants.NBT.TAG_STRING)) {
//            ResourceLocation uid = new ResourceLocation(compound.getString(TASK_TAG));
//            IMaidTask task = TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
//            setTask(task);
//        }
        if (compound.contains(PICKUP_TAG, Constants.NBT.TAG_BYTE)) {
            setPickup(compound.getBoolean(PICKUP_TAG));
        }
//        if (compound.contains(HOME_TAG, Constants.NBT.TAG_BYTE)) {
//            setHomeModeEnable(compound.getBoolean(HOME_TAG));
//        }
//        if (compound.contains(RIDEABLE_TAG, Constants.NBT.TAG_BYTE)) {
//            setRideable(compound.getBoolean(RIDEABLE_TAG));
//        }
//        if (compound.contains(BACKPACK_LEVEL_TAG, Constants.NBT.TAG_INT)) {
//            setBackpackLevel(compound.getInt(BACKPACK_LEVEL_TAG));
//        }
//        if (compound.contains(MAID_INVENTORY_TAG, Constants.NBT.TAG_COMPOUND)) {
//            maidInv.deserializeNBT(compound.getCompound(MAID_INVENTORY_TAG));
//        }
//        if (compound.contains(MAID_BAUBLE_INVENTORY_TAG, Constants.NBT.TAG_COMPOUND)) {
//            maidBauble.deserializeNBT(compound.getCompound(MAID_BAUBLE_INVENTORY_TAG));
//        }
//        if (compound.contains(STRUCK_BY_LIGHTNING_TAG, Constants.NBT.TAG_BYTE)) {
//            setStruckByLightning(compound.getBoolean(STRUCK_BY_LIGHTNING_TAG));
//        }
//        if (compound.contains(INVULNERABLE_TAG, Constants.NBT.TAG_BYTE)) {
//            setEntityInvulnerable(compound.getBoolean(INVULNERABLE_TAG));
//        }
//        if (compound.contains(HUNGER_TAG, Constants.NBT.TAG_INT)) {
//            setHunger(compound.getInt(HUNGER_TAG));
//        }
//        if (compound.contains(FAVORABILITY_TAG, Constants.NBT.TAG_INT)) {
//            setFavorability(compound.getInt(FAVORABILITY_TAG));
//        }
//        if (compound.contains(EXPERIENCE_TAG, Constants.NBT.TAG_INT)) {
//            setExperience(compound.getInt(EXPERIENCE_TAG));
//        }
//        if (compound.contains(SCHEDULE_MODE_TAG, Constants.NBT.TAG_STRING)) {
//            setSchedule(MaidSchedule.valueOf(compound.getString(SCHEDULE_MODE_TAG)));
//        }
//        if (compound.contains(RESTRICT_CENTER_TAG, Constants.NBT.TAG_COMPOUND)) {
//            setRestrictCenter(NBTUtil.readBlockPos(compound.getCompound(RESTRICT_CENTER_TAG)));
//        }
    }
    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
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
    }


    public void setModelId(String modelId) {
        this.entityData.set(DATA_MODEL_ID, modelId);
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
