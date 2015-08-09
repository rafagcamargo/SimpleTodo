package com.codepath.simpletodo.sort;

import com.codepath.simpletodo.model.Todo;

import java.util.Comparator;

public class SortByPriority implements Comparator<Todo> {

    public static final Integer ASCENDING = 1;
    public static final Integer DESCENDING = -1;

    private int order;

    public SortByPriority() {
        this.order = ASCENDING;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compare(Todo lhs, Todo rhs) {
        return order * (rhs.getPriorityOrder() - lhs.getPriorityOrder());
    }
}
