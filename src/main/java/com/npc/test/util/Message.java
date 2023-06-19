/*
 * Decompiled with CFR 0.150.
 */
package com.npc.test.util;

public class Message {
    private long startTime;
    private String message;
    private SpecColor colorOutline;
    private SpecColor colorInside;
    private SpecColor colorText;

    public Message(long startTime, String message, SpecColor color, SpecColor colorInside, SpecColor colorText) {
        this.startTime = startTime;
        this.message = message;
        this.colorOutline = color;
        this.colorInside = colorInside;
        this.colorText = colorText;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public String getMessage() {
        return this.message;
    }

    public SpecColor getColorInside() {
        return this.colorInside;
    }

    public SpecColor getColorOutline() {
        return this.colorOutline;
    }

    public SpecColor getColorText() {
        return this.colorText;
    }
}

