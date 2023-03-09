package com.npc.test.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.npc.test.passive.NpcEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;


public class NpcModel <T extends NpcEntity> extends VillagerModel<T> {


    public NpcModel(float p_i1163_1_) {
        super(p_i1163_1_);
    }

    public NpcModel(float p_i51059_1_, int p_i51059_2_, int p_i51059_3_) {
        super(p_i51059_1_, p_i51059_2_, p_i51059_3_);
    }


}
