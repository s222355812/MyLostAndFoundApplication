package com.example.mylostandfoundapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LostFoundItemDAO {

    private static final String TABLE_NAME = "lost_found_items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    private DatabaseHelper databaseHelper;

    public LostFoundItemDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertLostFoundItem(LostFoundItem item) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_PHONE, item.getPhone());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_LOCATION, item.getLocation());
        values.put(COLUMN_LATITUDE, item.getLatitude());
        values.put(COLUMN_LONGITUDE, item.getLongitude());

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public List<LostFoundItem> getAllLostFoundItems() {
        List<LostFoundItem> itemList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_PHONE,
                COLUMN_DESCRIPTION,
                COLUMN_DATE,
                COLUMN_LOCATION,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE
        };
        @SuppressLint("Range")
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));

                LostFoundItem item = new LostFoundItem(id, name, phone, description, date, location, latitude, longitude);
                itemList.add(item);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return itemList;
    }

    public int updateLostFoundItem(LostFoundItem item) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_PHONE, item.getPhone());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_LOCATION, item.getLocation());
        values.put(COLUMN_LATITUDE, item.getLatitude());
        values.put(COLUMN_LONGITUDE, item.getLongitude());

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return rowsAffected;
    }

    public int deleteLostFoundItem(int itemId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};

        int rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        return rowsDeleted;
    }
}
