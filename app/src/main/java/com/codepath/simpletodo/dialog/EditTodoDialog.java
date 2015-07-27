package com.codepath.simpletodo.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.activity.TodoActivity;
import com.codepath.simpletodo.listener.EditTodoDialogListener;
import com.codepath.simpletodo.model.Todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTodoDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Button buttonDueDate;
    private Button buttonEditItem;
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

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//// the content
//        final RelativeLayout root = new RelativeLayout(getActivity());
//        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        // creating the fullscreen dialog
//        final Dialog dialog = new Dialog(getActivity());
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(root);
//        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        return dialog;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        itemPosition = args.getInt(TodoActivity.ITEM_POSITION);
        itemTodo = (Todo) args.getSerializable(TodoActivity.ITEM_TODO);

        View view = inflater.inflate(R.layout.fragment_edit_todo, container, false);
        //getDialog().setTitle("Edit item");

        buttonDueDate = (Button) view.findViewById(R.id.buttonDueDate);
        buttonDueDate.setText(getFormattedDate(new Date()));
        buttonDueDate.setOnClickListener(buttonDueDateOnClickListener());

        buttonEditItem = (Button) view.findViewById(R.id.buttonEditItem);
        buttonEditItem.setOnClickListener(buttonEditItemOnClickListener());

        editTextItem = (EditText) view.findViewById(R.id.editTextItem);
        editTextItem.append(itemTodo.getNote());

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        buttonDueDate.setText(getFormattedDate(c.getTime()));
    }

    public String getFormattedDate(Date date) {
        String format = "EEE, LLL dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(date);
    }

    private View.OnClickListener buttonDueDateOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                //bundle.putSerializable("listener", EditTodoDialog.this);
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
}
