/*
 * Decompiled with CFR 0.150.
 */
package com.npc.test.util;

import java.awt.*;
import java.util.List;

public class SpecColor {
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public SpecColor(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public SpecColor(List<Integer> rgba) {
        this.red = rgba.get(0);
        this.green = rgba.get(1);
        this.blue = rgba.get(2);
        this.alpha = rgba.get(3);
    }

    public SpecColor(Integer[] values) {
        this.red = Math.round(values[0].intValue());
        this.green = Math.round(values[1].intValue());
        this.blue = Math.round(values[2].intValue());
        this.alpha = Math.round(values[3].intValue());
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public Color getColor() {
        return new Color(this.red, this.green, this.blue, this.alpha);
    }

    public int getRGB() {
        return this.getColor().getRGB();
    }

    public void setValues(Integer[] values) {
        this.red = values[0];
        this.green = values[1];
        this.blue = values[2];
        this.alpha = values[3];
    }

    public Integer[] getValues() {
        return new Integer[]{this.red, this.green, this.blue, this.alpha};
    }
}

