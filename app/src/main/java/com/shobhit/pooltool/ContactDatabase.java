package com.shobhit.pooltool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shobhit-pc on 12/28/2016.
 */

public class ContactDatabase extends SQLiteOpenHelper {

    public ContactDatabase(Context applicationcontext) {
        super(applicationcontext, "contact_database.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE contact ( phoneName TEXT, phoneNumber TEXT UNIQUE, invite TEXT )";//check unique query
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS contact";
        database.execSQL(query);
        onCreate(database);
    }

    public void update(String phoneNumber){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "UPDATE contact SET invite=0 WHERE phoneNumber = "+phoneNumber+"";
        database.execSQL(query);
    }

    public void delete(String phoneNumber){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM contact WHERE phoneNumber = "+phoneNumber+"";
        database.execSQL(query);
        database.close();
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertContact(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phoneName", queryValues.get("phoneName"));
        values.put("phoneNumber", queryValues.get("phoneNumber"));
        values.put("invite", queryValues.get("invite"));
        database.insert("contact", null, values);
        database.close();

    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllContact() {
        ArrayList<HashMap<String, String>> contactList;
        contactList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM contact";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phoneName", cursor.getString(0));
                map.put("phoneNumber", cursor.getString(1));
                map.put("invite", cursor.getString(2));
                contactList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return contactList;
    }
    public ArrayList<String> getAllPhoneNumber() {
        ArrayList<String> contactList;
        contactList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM contact";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                contactList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        database.close();
        return contactList;
    }
    public String getContact( String phoneNumber) {
        ArrayList<HashMap<String, String>> contactDetail;
        contactDetail = new ArrayList<HashMap<String, String>>();
        String phoneName="";
        String selectQuery = "SELECT * FROM contact WHERE phoneNumber = '" + phoneNumber +"' ";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
         /*   do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phoneName", cursor.getString(0));
                map.put("phoneNumber", cursor.getString(1));
                map.put("invite", cursor.getString(2));
                contactDetail.add(map);
            } while (cursor.moveToNext());
            */
            phoneName = cursor.getString(0);
        }
        database.close();
        return phoneName;
    }
}