package com.shobhit.pooltool.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class NotificationDatabase extends SQLiteOpenHelper {

    public NotificationDatabase(Context applicationcontext) {
        super(applicationcontext, "notification_database.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE notification ( groupName TEXT, groupId TEXT, addedMobileNo TEXT, date TEXT, time TEXT ,  itemId TEXT , item TEXT, amountGiverType TEXT,amount TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS notification";
        database.execSQL(query);
        onCreate(database);
    }

    public void delete(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM notification";
        database.execSQL(query);
        database.close();
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertNotification(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("groupName", queryValues.get("groupName"));
        values.put("groupId", queryValues.get("groupId"));
        values.put("addedMobileNo", queryValues.get("addedMobileNo"));
        values.put("date", queryValues.get("date"));
        values.put("time", queryValues.get("time"));
        values.put("itemId", queryValues.get("itemId"));
        values.put("item", queryValues.get("item"));
        values.put("amountGiverType", queryValues.get("amountGiverType"));
        values.put("amount", queryValues.get("amount"));
        database.insert("notification", null, values);
        database.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllNotification() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM notification ORDER BY time DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("groupName", cursor.getString(0));
                map.put("groupId", cursor.getString(1));
                map.put("addedMobileNo", cursor.getString(2));
                map.put("date", cursor.getString(3));
                map.put("time", cursor.getString(4));
                map.put("itemId", cursor.getString(5));
                map.put("item", cursor.getString(6));
                map.put("amountGiverType", cursor.getString(7));
                map.put("amount", cursor.getString(8));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> getNotificationDetails( String groupid) {
        ArrayList<HashMap<String, String>> groupDetail;
        groupDetail = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM notification WHERE groupId="+groupid+"";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("groupName", cursor.getString(0));
                map.put("groupId", cursor.getString(1));
                map.put("addedMobileNo", cursor.getString(2));
                map.put("date", cursor.getString(3));
                map.put("time", cursor.getString(4));
                map.put("itemId", cursor.getString(5));
                map.put("item", cursor.getString(6));
                map.put("amountGiverType", cursor.getString(7));
                map.put("amount", cursor.getString(8));
                groupDetail.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return groupDetail;
    }
}