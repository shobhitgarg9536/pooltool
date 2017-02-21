package com.shobhit.pooltool;

        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.AsyncTask;

public class DBController  extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }



    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;

        query = "CREATE TABLE users ( mobileNo TEXT, groupName TEXT, groupMember TEXT, groupId TEXT UNIQUE, date TEXT, time TEXT,admin TEXT, latestTime TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        onCreate(database);
    }

    public void Update(String groupid , String latestTime){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "UPDATE users SET latestTime='"+latestTime+"' WHERE groupId = '"+groupid+"'";
        database.execSQL(query);
    }

    public void UpdateAdmin(String groupid , String latestTime, String admin){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "UPDATE users SET admin='"+admin+"' AND latestTime='"+latestTime+"' WHERE groupId = '"+groupid+"'";
        database.execSQL(query);
    }

    public void delete(String groupid){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM users WHERE groupId = "+groupid+"";
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
        values.put("mobileNo", queryValues.get("mobileNo"));
        values.put("groupName", queryValues.get("groupName"));
        values.put("groupMember", queryValues.get("groupMember"));
        values.put("groupId", queryValues.get("groupId"));
        values.put("date", queryValues.get("date"));
        values.put("time", queryValues.get("time"));
        values.put("admin", queryValues.get("admin"));
        values.put("latestTime", queryValues.get("latestTime"));
        database.insert("users", null, values);
        database.close();

    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM users ORDER BY latestTime DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mobileNo", cursor.getString(0));
                map.put("groupName", cursor.getString(1));
                map.put("groupMember", cursor.getString(2));
                map.put("groupId", cursor.getString(3));
                map.put("date", cursor.getString(4));
                map.put("time", cursor.getString(5));
                map.put("admin", cursor.getString(6));
                map.put("latestTime", cursor.getString(7));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }
    public ArrayList<HashMap<String, String>> getGroupDetails( String groupid) {
        ArrayList<HashMap<String, String>> groupDetail;
        groupDetail = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM users WHERE groupId= '"+groupid+"'";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mobileNo", cursor.getString(0));
                map.put("groupName", cursor.getString(1));
                map.put("groupMember", cursor.getString(2));
                map.put("groupId", cursor.getString(3));
                map.put("date", cursor.getString(4));
                map.put("time", cursor.getString(5));
                map.put("admin", cursor.getString(6));
                map.put("latestTime", cursor.getString(7));
                groupDetail.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return groupDetail;
    }
    public String getGroupName( String groupid) {
        ArrayList<HashMap<String, String>> groupDetail;
        groupDetail = new ArrayList<HashMap<String, String>>();
        String groupName = "";
        String selectQuery = "SELECT * FROM users WHERE groupId= '" + groupid + "'";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            groupName = cursor.getString(1);
        }
        database.close();
        return groupName;
    }
}