package com.codepath.simpletodo.model;

import java.io.Serializable;

public class Todo implements Serializable {

    private long id;
    private String note;
    private Priority priority;
    private long dueDate;


    public Todo(String note) {
        this.note = note;
    }

    public Todo(long id, String note, int priorityOrder, long dueDate) {
        this.id = id;
        this.note = note;
        this.priority = Priority.getFromOrder(priorityOrder);
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public boolean hasDueDate() {
        return this.dueDate > 0;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(int priorityOrder) {
        this.priority = Priority.getFromOrder(priorityOrder);
    }

    public void setPriority(String priorityName) {
        try {
            this.priority = Priority.valueOf(priorityName.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.priority = null;
        }
    }

    public boolean hasPriority() {
        return this.priority != null;
    }

    public int getPriorityOrder() {
        return hasPriority() ? this.priority.getOrder() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        return id == todo.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
