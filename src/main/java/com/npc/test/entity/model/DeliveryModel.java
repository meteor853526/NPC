package com.npc.test.entity.model;

import com.google.common.collect.Maps;
import com.npc.test.client.resource.pojo.MaidModelInfo;
import com.npc.test.passive.DeliveryEntity;
import net.minecraft.client.renderer.entity.model.VillagerModel;

import java.util.HashMap;
import java.util.Optional;


public class DeliveryModel<T extends DeliveryEntity> extends VillagerModel<T> {
    private static final String JSON_FILE_NAME = "maid_model.json";
    private final HashMap<String, MaidModelInfo> idInfoMap;
    private static DeliveryModel INSTANCE;
    public DeliveryModel(float p_i1163_1_) {
        super(p_i1163_1_);
        this.idInfoMap = Maps.newHashMap();
    }

    public float getModelRenderItemScale(String modelId) {
        if (idInfoMap.containsKey(modelId)) {
            return idInfoMap.get(modelId).getRenderItemScale();
        }
        return 1.0f;
    }
    public static DeliveryModel getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new DeliveryModel(32);
        }
        return INSTANCE;
    }

    public Optional<MaidModelInfo> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }
    public String getJsonFileName() {
        return JSON_FILE_NAME;
    }


}
