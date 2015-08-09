package com.codepath.simpletodo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.adapter.TodoAdapter;
import com.codepath.simpletodo.database.DatabaseHelper;
import com.codepath.simpletodo.dialog.EditTodoDialog;
import com.codepath.simpletodo.listener.EditTodoDialogListener;
import com.codepath.simpletodo.model.Todo;
import com.codepath.simpletodo.sort.SortByPriority;

import java.util.List;


public class TodoActivity extends AppCompatActivity implements EditTodoDialogListener {

    public static final String ITEM_POSITION = "itemPosition";
    public static final String ITEM_TODO = "itemTodo";

    private static final int EDIT_ITEM_REQUEST = 0;

    private ArrayAdapter<Todo> todoAdapter;
    private ListView listViewItems;
    private EditText editTextNewItem;
    private DatabaseHelper databaseHelper;
    private Integer screenType;
    private SortByPriority sortByPriority;

    private MenuItem menuItemAscending;
    private MenuItem menuItemDescending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        databaseHelper = DatabaseHelper.getInstance(this);

        listViewItems = (ListView) findViewById(R.id.listViewItems);
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);

        List<Todo> todos = databaseHelper.getAllTodos();

        todoAdapter = new TodoAdapter(this, todos);
        sortByPriority = new SortByPriority();
        todoAdapter.sort(sortByPriority);
        listViewItems.setAdapter(todoAdapter);

        setupListViewListener();
        setupEditTextViewListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_NAME, MODE_PRIVATE);
        screenType = Integer.valueOf(sharedPref.getString(SettingsActivity.PREFERENCE_SCREEN_TYPE, "0"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        menuItemAscending = menu.findItem(R.id.sorting_priority_ascending);
        menuItemDescending = menu.findItem(R.id.sorting_priority_descending);
        return true;
    }

    public void onClickSettings(MenuItem menu) {
        Intent intent = new Intent(TodoActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClickSorting(MenuItem menu) {
        boolean ascending = menu.getItemId() == R.id.sorting_priority_ascending;
        int order = ascending ? SortByPriority.ASCENDING : SortByPriority.DESCENDING;

        sortByPriority.setOrder(order);
        todoAdapter.sort(sortByPriority);

        menuItemAscending.setEnabled(!ascending);
        menuItemDescending.setEnabled(ascending);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                final int itemPosition = data.getIntExtra(ITEM_POSITION, -1);
                final Todo itemTodo = (Todo) data.getSerializableExtra(ITEM_TODO);
                databaseHelper.updateTodo(itemTodo);
                todoAdapter.remove(itemTodo);
                todoAdapter.insert(itemTodo, itemPosition);
                todoAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onAddItem(View view) {
        addItem();
    }

    @Override
    public void onFinishEditTodoDialog(int itemPosition, Todo itemTodo) {
        databaseHelper.updateTodo(itemTodo);
        todoAdapter.remove(itemTodo);
        todoAdapter.insert(itemTodo, itemPosition);
        todoAdapter.notifyDataSetChanged();
    }

    private void addItem() {
        final String itemText = editTextNewItem.getText().toString();

        if (itemText.isEmpty() || itemText.trim().isEmpty()) {
            Toast.makeText(this, R.string.you_cannot_save_empty_text, Toast.LENGTH_SHORT).show();
            return;
        }

        final Todo todo = new Todo(itemText);
        databaseHelper.addTodo(todo);
        todoAdapter.add(todo);
        editTextNewItem.setText("");
    }

    private void setupListViewListener() {
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo = todoAdapter.getItem(position);
                databaseHelper.deleteTodo(todo);
                todoAdapter.remove(todo);
                todoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo = todoAdapter.getItem(position);
                if (screenType == 0) {
                    showEditTodoActivity(position, todo);
                } else {
                    showEditTodoDialog(position, todo);
                }
            }
        });
    }

    private void setupEditTextViewListener() {
        editTextNewItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    addItem();
                    return true;
                }
                return false;
            }
        });
    }

    private void showEditTodoActivity(int position, Todo todo) {
        Intent intent = new Intent(TodoActivity.this, EditTodoActivity.class);
        intent.putExtra(ITEM_POSITION, position);
        intent.putExtra(ITEM_TODO, todo);
        startActivityForResult(intent, EDIT_ITEM_REQUEST);
    }

    private void showEditTodoDialog(int position, Todo todo) {
        EditTodoDialog alertDialog = EditTodoDialog.newInstance(position, todo);
        FragmentManager fragmentManager = getSupportFragmentManager();
        alertDialog.show(fragmentManager, "edit_todo_dialog");
    }

}
