package com.shobhit.pooltool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class NewGroupDatabase extends SQLiteOpenHelper {

    public NewGroupDatabase(Context applicationcontext) {
        super(applicationcontext, "new_group_database.db", null, 1);
    }



    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE newGroup (groupName TEXT, groupId TEXT, adminMobileNo TEXT, date TEXT, time TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS newGroup";
        database.execSQL(query);
        onCreate(database);
    }

    public void delete(String groupid){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM newGroup WHERE groupId = "+groupid+"";
        database.execSQL(query);
        database.close();
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertGroup(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("groupName", queryValues.get("groupName"));
        values.put("groupId", queryValues.get("groupId"));
        values.put("adminMobileNo", queryValues.get("adminMobileNo"));
        values.put("date", queryValues.get("date"));
        values.put("time", queryValues.get("time"));

        database.insert("newGroup", null, values);

        database.close();
    }

    /**
     * Get list of NEW GROUPS from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllGroups() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM newGroup";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("groupName", cursor.getString(0));
                map.put("groupId", cursor.getString(1));
                map.put("adminMobileNo", cursor.getString(2));
                map.put("date", cursor.getString(3));
                map.put("time", cursor.getString(4));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }
}