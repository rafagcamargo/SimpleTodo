package com.codepath.simpletodo.model;

import android.support.annotation.Nullable;

import com.codepath.simpletodo.R;

public enum Priority {
    HIGH(1, R.string.high, R.color.red, R.id.radioButtonHigh),
    MEDIUM(3, R.string.medium, R.color.yellow, R.id.radioButtonMedium),
    LOW(5, R.string.low, R.color.green, R.id.radioButtonLow);

    private int order;
    private int label;
    private int color;
    private int id;

    Priority(int order, int label, int color, int id) {
        this.order = order;
        this.label = label;
        this.color = color;
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public int getLabel() {
        return label;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public static Priority getFromOrder(int order) {
        switch (order) {
            case 1:
                return HIGH;
            case 3:
                return MEDIUM;
            case 5:
                return LOW;
        }

        return null;
    }
}
