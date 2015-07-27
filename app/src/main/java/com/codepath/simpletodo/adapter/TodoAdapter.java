package com.codepath.simpletodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.Todo;
import com.codepath.simpletodo.util.DateFormatUtils;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    public TodoAdapter(Context context, List<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            viewHolder.itemNoteTextView = (TextView) convertView.findViewById(R.id.itemNoteTextView);
            viewHolder.itemDueDateTextView = (TextView) convertView.findViewById(R.id.itemDueDateTextView);
            viewHolder.itemPriorityTextView = (TextView) convertView.findViewById(R.id.itemPriorityTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemNoteTextView.setText(todo.getNote());

        if (todo.hasDueDate()) {
            viewHolder.itemDueDateTextView.setVisibility(View.VISIBLE);
            viewHolder.itemDueDateTextView.setText(DateFormatUtils.getFormattedDate(todo.getDueDate()));
        } else {
            viewHolder.itemDueDateTextView.setVisibility(View.GONE);
        }

        if (todo.hasPriority()) {
            viewHolder.itemPriorityTextView.setVisibility(View.VISIBLE);
            viewHolder.itemPriorityTextView.setText(todo.getPriority().getLabel());
            int color = getContext().getResources().getColor(todo.getPriority().getColor());
            viewHolder.itemPriorityTextView.setTextColor(color);
        } else {
            viewHolder.itemPriorityTextView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView itemNoteTextView;
        TextView itemDueDateTextView;
        TextView itemPriorityTextView;
    }
}
