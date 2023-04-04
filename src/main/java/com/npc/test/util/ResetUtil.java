/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package com.npc.test.util;

import com.google.common.collect.Lists;
import com.npc.test.BubblesConfig;
import com.npc.test.client.ClientBubblesUtil;


public class ResetUtil {
    public static void resetColors() {
        BubblesConfig.CLIENT.colorOutline.set(Lists.newArrayList(0, 0, 0, 220));
        BubblesConfig.CLIENT.colorInside.set(Lists.newArrayList(203, 203, 203, 220));
        BubblesConfig.CLIENT.colorText.set(Lists.newArrayList(0, 0, 0, 220));
    }

    public static void resetDuration() {
        BubblesConfig.CLIENT.durationBubbles.set((Integer) 10);
    }

    public static void clearBubbles() {
        ClientBubblesUtil.BUBBLES_SYNC.clear();
    }
}

