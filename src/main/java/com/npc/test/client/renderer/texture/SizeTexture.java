package com.npc.test.client.renderer.texture;

import net.minecraft.client.renderer.texture.Texture;

public abstract class SizeTexture extends Texture {
    /**
     *
     *
     *
     */
    abstract public int getWidth();

    /**
     *
     *
     * @return
     */
    abstract public int getHeight();

    /**
     *
     *
     * @return
     */
    abstract public boolean isExist();
}
