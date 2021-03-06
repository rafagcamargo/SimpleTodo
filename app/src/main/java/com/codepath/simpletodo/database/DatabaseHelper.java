package com.codepath.simpletodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codepath.simpletodo.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "simpleTodoDatabase";

    private static DatabaseHelper databaseHelperInstance;

    private static class Table {
        private static final String TODO = "todo";
    }

    private static class Column {
        private static final String ID = "id";
        private static final String NOTE = "note";
        private static final String PRIORITY_ORDER = "priority_order";
        private static final String DUE_DATE = "due_date";
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelperInstance == null) {
            databaseHelperInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table.TODO + "(" + Column.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Column.NOTE + " TEXT," + Column.PRIORITY_ORDER + " INTEGER," + Column.DUE_DATE + " LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + Table.TODO);
            onCreate(db);
        }
    }

    // Entities database methods

    public void addTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.NOTE, todo.getNote());

        long id = database.insertOrThrow(Table.TODO, null, contentValues);

        todo.setId((int) id);

        database.close();
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + Table.TODO;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getInt(cursor.getColumnIndex(Column.ID));
                String note = cursor.getString(cursor.getColumnIndex(Column.NOTE));
                int priorityOrder = cursor.getInt(cursor.getColumnIndex(Column.PRIORITY_ORDER));
                long dueDate = cursor.getLong(cursor.getColumnIndex(Column.DUE_DATE));

                Todo todo = new Todo(id, note, priorityOrder, dueDate);
                todos.add(todo);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return todos;
    }

    public void updateTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.NOTE, todo.getNote());
        contentValues.put(Column.DUE_DATE, todo.getDueDate());
        contentValues.put(Column.PRIORITY_ORDER, todo.getPriorityOrder());

        database.update(Table.TODO, contentValues, Column.ID + " = ?",
                new String[]{String.valueOf(todo.getId())});

        database.close();
    }

    public void deleteTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(Table.TODO, Column.ID + " = ?", new String[]{String.valueOf(todo.getId())});

        database.close();
    }
}
