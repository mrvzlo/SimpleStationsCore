package com.ave.simplestationscore.resources;

public abstract class BaseResource {
    int lowValue;
    int highValue;
    public int usage;

    public int getLow() {
        return lowValue;
    }

    public int getHigh() {
        return highValue;
    }

    public void setLow(int amount) {
        lowValue = amount;
        set((lowValue & 0xFFFF) | (highValue << 16));
    }

    public void setHigh(int amount) {
        highValue = amount;
        set((lowValue & 0xFFFF) | (highValue << 16));
    }

    abstract void set(int amount);
}
