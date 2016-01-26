package com.example.aparna.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aparna on 1/20/16.
 */
public class TodoListSqlLiteDB extends SQLiteOpenHelper {
    public static final String TAG = "TodoListSqlLiteDB";
    // Database Info
    private static final String DATABASE_NAME = "todoListDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // ITems Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";

    private static TodoListSqlLiteDB sInstance;

    public static synchronized TodoListSqlLiteDB getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoListSqlLiteDB(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoListSqlLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
//        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TEXT + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addorUpdateItem(Item item) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long itemId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.getText());

            if ((Long)item.getId()!= null && (Long)item.getId()> 0) { //TODO check this > 0 again
                // Get the primary key of the user we just updated
                int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_ID + "=?", new String[]{String.valueOf(item.getId())});
                if (rows == 1){
                    db.setTransactionSuccessful();
                    return item.getId();
                }
            } else {
                // user with this userName did not already exist, so insert new user
                itemId = db.insertOrThrow(TABLE_ITEMS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update item");
        } finally {
            db.endTransaction();
        }
        return itemId;
    }

    // Get all posts in the database
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_ITEMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.setText(cursor.getString(cursor.getColumnIndex(KEY_ITEM_TEXT)));
                    newItem.setId(cursor.getLong(cursor.getColumnIndex(KEY_ITEM_ID)));
                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get Items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }
    public boolean deleteItem(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String table = TABLE_ITEMS;
        String whereClause = "id=" + String.valueOf(id);
//        if(db.delete(TABLE_ITEMS, KEY_ITEM_ID+ "=?", new String[]{String.valueOf(id)}) > 0);
        try {
            if(db.delete(table, whereClause, null) > 0){
                db.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all items");
            return false;
        } finally {
            db.endTransaction();
        }
    }
    // Delete all items in the database
    public void deleteAllItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all items");
        } finally {
            db.endTransaction();
        }
    }
}
