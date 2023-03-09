package com.npc.test.entity.render;

import com.npc.test.NpcTestMod;
import com.npc.test.entity.model.NpcModel;
import com.npc.test.passive.NpcEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.model.VillagerModel;

public class NpcRenderer extends MobRenderer<NpcEntity, NpcModel<NpcEntity>>
{
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(NpcTestMod.MOD_ID, "textures/entity/entity_villager_farmer_png.png");

    public NpcRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new NpcModel<>(1F), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(NpcEntity entity) {
        return TEXTURE;
    }

}
