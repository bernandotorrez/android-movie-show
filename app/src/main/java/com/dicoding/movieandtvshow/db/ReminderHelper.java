package com.dicoding.movieandtvshow.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dicoding.movieandtvshow.reminder.ReminderSettingItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.movieandtvshow.db.DatabaseContract.ReminderColumns.*;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TABLE_REMINDER;

public class ReminderHelper {

    private static final String DATABASE_TABLE = TABLE_REMINDER;
    private static DatabaseHelper dataBaseHelper;
    private static ReminderHelper INSTANCE;

    private static SQLiteDatabase database;

    public ReminderHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public ReminderHelper() {

    }

    public static ReminderHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReminderHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<ReminderSettingItems> getAllReminder() {
        ArrayList<ReminderSettingItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        ReminderSettingItems reminder;

        if (cursor.getCount() > 0) {
            do {
                reminder = new ReminderSettingItems();
                reminder.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                reminder.setTypeRemidner(cursor.getString(cursor.getColumnIndexOrThrow(TYPE_REMINDER)));
                reminder.setTimeReminder(cursor.getString(cursor.getColumnIndexOrThrow(TIME_REMINDER)));

                arrayList.add(reminder);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }


        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return arrayList;
    }

    public static String getDataReminder(Integer id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_REMINDER + " WHERE "  +_ID + " = " +id,null);
        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndexOrThrow(IS_REMINDER));
    }

    public static String getTimeReminder(Integer id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_REMINDER + " WHERE "  +_ID + " = " +id,null);
        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndexOrThrow(TIME_REMINDER));
    }

    public static long updateReminder(ReminderSettingItems reminder) {
        ContentValues args = new ContentValues();
        args.put(IS_REMINDER, reminder.getIsReminder());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + reminder.getId() + "'", null);
    }

}
