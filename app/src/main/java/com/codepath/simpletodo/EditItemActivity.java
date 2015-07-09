package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditItemActivity extends AppCompatActivity {

    private EditText editTextItem;
    private Integer itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        itemId = intent.getIntExtra(ToDoActivity.ITEM_ID, -1);

        final String itemText = intent.getStringExtra(ToDoActivity.ITEM_TEXT);
        editTextItem = (EditText) findViewById(R.id.editTextItem);
        editTextItem.append(itemText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveItem(View view) {
        final String itemText = editTextItem.getText().toString();

        if (itemText.isEmpty() || itemText.trim().isEmpty()) {
            Toast.makeText(this, R.string.you_cannot_save_empty_text, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(ToDoActivity.ITEM_ID, itemId);
        intent.putExtra(ToDoActivity.ITEM_TEXT, itemText);
        setResult(RESULT_OK, intent);
        finish();
    }
}
