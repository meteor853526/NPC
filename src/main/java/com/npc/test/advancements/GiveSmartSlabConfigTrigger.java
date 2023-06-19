package com.npc.test.advancements;


import com.google.gson.JsonObject;
import com.npc.test.NpcTestMod;
import com.npc.test.config.subconfig.MiscConfig;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class GiveSmartSlabConfigTrigger extends AbstractCriterionTrigger<GiveSmartSlabConfigTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(NpcTestMod.MOD_ID, "give_smart_slab_config");

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(entityPredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }


    public void trigger(ServerPlayerEntity serverPlayer) {
        super.trigger(serverPlayer, instance -> MiscConfig.GIVE_SMART_SLAB.get());
    }

    public static class Instance extends CriterionInstance {
        public Instance(EntityPredicate.AndPredicate player) {
            super(ID, player);
        }
    }
}
