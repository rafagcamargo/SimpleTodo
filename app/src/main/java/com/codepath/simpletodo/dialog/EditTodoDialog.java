package com.codepath.simpletodo.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.activity.TodoActivity;
import com.codepath.simpletodo.listener.EditTodoDialogListener;
import com.codepath.simpletodo.model.Todo;
import com.codepath.simpletodo.util.DateFormatUtils;

import java.io.Serializable;
import java.util.Calendar;

public class EditTodoDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener,
        Serializable, AdapterView.OnItemSelectedListener {

    private Button buttonDueDate;
    private EditText editTextItem;

    private Integer itemPosition;
    private Todo itemTodo;

    public static EditTodoDialog newInstance(int position, Todo todo) {
        EditTodoDialog editTodoDialog = new EditTodoDialog();
        Bundle args = new Bundle();
        args.putInt(TodoActivity.ITEM_POSITION, position);
        args.putSerializable(TodoActivity.ITEM_TODO, todo);
        editTodoDialog.setArguments(args);
        return editTodoDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        itemPosition = args.getInt(TodoActivity.ITEM_POSITION);
        itemTodo = (Todo) args.getSerializable(TodoActivity.ITEM_TODO);

        View view = inflater.inflate(R.layout.fragment_edit_todo, container, false);
        getDialog().setTitle(R.string.edit_item);

        buttonDueDate = (Button) view.findViewById(R.id.buttonDueDate);
        buttonDueDate.setOnClickListener(buttonDueDateOnClickListener());

        Button buttonEditItem = (Button) view.findViewById(R.id.buttonEditItem);
        buttonEditItem.setOnClickListener(buttonEditItemOnClickListener());

        editTextItem = (EditText) view.findViewById(R.id.editTextItem);
        editTextItem.append(itemTodo.getNote());

        if (itemTodo.hasDueDate()) {
            buttonDueDate.setText(DateFormatUtils.getFormattedDate(itemTodo.getDueDate()));
        }

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.todo_priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (itemTodo.hasPriority()) {
            spinner.setSelection(itemTodo.getPriorityOrder());
        }

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        itemTodo.setDueDate(c.getTimeInMillis());
        buttonDueDate.setText(DateFormatUtils.getFormattedDate(c.getTime()));
    }

    private View.OnClickListener buttonDueDateOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("listener", EditTodoDialog.this);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        };
    }

    private View.OnClickListener buttonEditItemOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String itemText = editTextItem.getText().toString();

                if (itemText.isEmpty() || itemText.trim().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.you_cannot_save_empty_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                itemTodo.setNote(itemText);

                EditTodoDialogListener listener = (EditTodoDialogListener) getActivity();
                listener.onFinishEditTodoDialog(itemPosition, itemTodo);
                dismiss();
            }
        };
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String priorityName = (String) parent.getAdapter().getItem(position);
        itemTodo.setPriority(priorityName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //no-op
    }
}
