package com.codepath.simpletodo.listener;

import com.codepath.simpletodo.model.Todo;

public interface EditTodoDialogListener {
    void onFinishEditTodoDialog(int itemPosition, Todo itemTodo);
}
