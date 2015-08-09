package com.codepath.simpletodo.model;

import android.support.annotation.Nullable;

import com.codepath.simpletodo.R;

public enum Priority {
    HIGH(1, R.string.high, R.color.red, R.id.radioButtonHigh),
    MEDIUM(2, R.string.medium, R.color.yellow, R.id.radioButtonMedium),
    LOW(3, R.string.low, R.color.green, R.id.radioButtonLow);

    private final int order;
    private final int label;
    private final int color;
    private final int id;

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
            case 2:
                return MEDIUM;
            case 3:
                return LOW;
        }

        return null;
    }
}
