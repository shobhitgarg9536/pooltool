package com.shobhit.pooltool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shobhit-pc on 12/25/2016.
 */

public class MemberDB extends SQLiteOpenHelper {

    public MemberDB(Context applicationcontext) {
        super(applicationcontext, "memberDB.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE member ( groupcreator TEXT, member TEXT, groupId TEXT, date TEXT, time TEXT, admin TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS member";
        database.execSQL(query);
        onCreate(database);
    }

    public void delete(String groupid){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM member WHERE groupId = '"+groupid+"'";
        database.execSQL(query);
        database.close();
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("groupcreator", queryValues.get("groupcreator"));
        values.put("member", queryValues.get("member"));
        values.put("groupId", queryValues.get("groupId"));
        values.put("date", queryValues.get("date"));
        values.put("time", queryValues.get("time"));
        values.put("admin", queryValues.get("admin"));
        database.insert("member", null, values);
        database.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllMember(String groupid) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM member WHERE groupId= '"+groupid+"'";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("groupcreator", cursor.getString(0));
                map.put("member", cursor.getString(1));
                map.put("groupId", cursor.getString(2));
                map.put("date", cursor.getString(3));
                map.put("time", cursor.getString(4));
                map.put("admin", cursor.getString(5));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getMemberContactNo(String groupid) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM member WHERE groupId= '"+groupid+"'";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("member", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getMemberContactNo2(String groupid) {
        ArrayList usersList;
        usersList = new ArrayList();
        String selectQuery = "SELECT * FROM member WHERE groupId= '"+groupid+"'";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                usersList.add(cursor.getString(1).toString());
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public String getMemberAdminState(String memberNo){
        String admin ="";
        String selectQuery = "SELECT * FROM member WHERE member= '"+memberNo+"'";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                admin = cursor.getString(5);

            } while (cursor.moveToNext());
        }
        database.close();
        return admin;
    }

}