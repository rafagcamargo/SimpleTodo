package com.codepath.simpletodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.simpletodo.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ToDoActivity extends AppCompatActivity {

    public static final String ITEM_ID = "itemId";
    public static final String ITEM_TEXT = "itemText";

    private static final int EDIT_ITEM_REQUEST = 0;

    private List<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listViewItems;
    private EditText editTextNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        listViewItems = (ListView) findViewById(R.id.listViewItems);
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);

        items = new ArrayList<>();
        readItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listViewItems.setAdapter(itemsAdapter);

        setupListViewListener();
        setupEditTextViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                final int itemId = data.getIntExtra(ITEM_ID, -1);
                final String itemText = data.getStringExtra(ITEM_TEXT);
                items.set(itemId, itemText);
                writeItems();
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onAddItem(View view) {
        addItem();
    }

    private void addItem() {
        final String itemText = editTextNewItem.getText().toString();

        if (itemText.isEmpty() || itemText.trim().isEmpty()) {
            Toast.makeText(this, R.string.you_cannot_save_empty_text, Toast.LENGTH_SHORT).show();
            return;
        }

        itemsAdapter.add(itemText);
        editTextNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ToDoActivity.this, EditItemActivity.class);
                intent.putExtra(ITEM_ID, position);
                intent.putExtra(ITEM_TEXT, items.get(position));
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
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

    private void readItems() {
        File todoFile = getTodoFile();
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File todoFile = getTodoFile();
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getTodoFile() {
        File fileDirectory = getFilesDir();
        File todoFile = new File(fileDirectory, "todo.txt");
        return todoFile;
    }
}
