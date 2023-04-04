/*
 * Decompiled with CFR 0.150.
 */
package com.npc.test.util;



import com.npc.test.BubblesConfig;

import java.util.ArrayDeque;
import java.util.List;

public class Bubble {
    private final ArrayDeque<Message> messages = new ArrayDeque();

    public Bubble(long startTime, String text) {
        this.messages.add(new Message(startTime, text, new SpecColor((List) BubblesConfig.CLIENT.colorOutline.get()), new SpecColor((List)BubblesConfig.CLIENT.colorInside.get()), new SpecColor((List)BubblesConfig.CLIENT.colorText.get())));
    }

    public ArrayDeque<Message> getMessages() {
        return this.messages;
    }
}

