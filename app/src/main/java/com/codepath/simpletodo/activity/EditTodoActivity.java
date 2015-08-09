package com.codepath.simpletodo.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.dialog.DatePickerFragment;
import com.codepath.simpletodo.model.Todo;
import com.codepath.simpletodo.util.DateFormatUtils;

import java.io.Serializable;
import java.util.Calendar;


public class EditTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, Serializable {

    private EditText editTextItem;
    private Button buttonDueDate;

    private Integer itemPosition;
    private Todo itemTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        Intent intent = getIntent();
        itemPosition = intent.getIntExtra(TodoActivity.ITEM_POSITION, -1);
        itemTodo = (Todo) intent.getSerializableExtra(TodoActivity.ITEM_TODO);

        editTextItem = (EditText) findViewById(R.id.editTextItem);
        editTextItem.append(itemTodo.getNote());

        buttonDueDate = (Button) findViewById(R.id.buttonDueDate);

        if (itemTodo.hasDueDate()) {
            buttonDueDate.setText(DateFormatUtils.getFormattedDate(itemTodo.getDueDate()));
        }

        if (itemTodo.hasPriority()) {
            RadioGroup radioGroupPriority = (RadioGroup) findViewById(R.id.radioGroupPriority);
            radioGroupPriority.check(itemTodo.getPriority().getId());
        }
    }

    public void onSaveItem(View view) {
        final String itemText = editTextItem.getText().toString();

        if (itemText.isEmpty() || itemText.trim().isEmpty()) {
            Toast.makeText(this, R.string.you_cannot_save_empty_text, Toast.LENGTH_SHORT).show();
            return;
        }

        itemTodo.setNote(itemText);

        Intent intent = new Intent();
        intent.putExtra(TodoActivity.ITEM_POSITION, itemPosition);
        intent.putExtra(TodoActivity.ITEM_TODO, itemTodo);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("listener", this);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onRadioButtonClicked(View view) {
        int priorityOrder = Integer.valueOf((String) view.getTag());
        itemTodo.setPriority(priorityOrder);
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

}
