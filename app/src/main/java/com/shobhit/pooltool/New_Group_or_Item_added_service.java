package com.shobhit.pooltool;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class New_Group_or_Item_added_service extends Service{

    NewGroupDatabase newGroupDatabase;
    ArrayList<HashMap<String, String>> groupList;
    ArrayList<HashMap<String, String>> groups_accept_or_reject;
    HashMap<String , String> groups;
    DBController dbController;
    SharedPreferences sharedPreferences;
    private final String USER_CONTACT = "UserContact";
    int flag= 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        newGroupDatabase = new NewGroupDatabase(this);
        dbController = new DBController(this);

        groupList = new ArrayList<HashMap<String, String>>();
        groupList = newGroupDatabase.getAllGroups();

        sharedPreferences = getSharedPreferences(USER_CONTACT , MODE_PRIVATE);


        groups_accept_or_reject = new ArrayList<HashMap<String, String>>();

        for(int i=0 ; i< groupList.size() ; i++){

            String groupName = groupList.get(i).get("groupName");
            String groupId = groupList.get(i).get("groupId");
            String adminMobileNo = groupList.get(i).get("adminMobileNo");
            String date = groupList.get(i).get("date");
            String time = groupList.get(i).get("time");

            String message = "You are requested to join new group \n Created By: "+adminMobileNo+"\n on "+date +" "+time;
            int group_result = Dialog(groupName , message);
            if(group_result == 1){
                groups_accept_or_reject_json_file(groupName , groupId , adminMobileNo, date, time, "accept" );
                add_to_DBController_database(groupName ,sharedPreferences.getString("UserContact" , ""), groupId , adminMobileNo, date, time);
            }
            else{
                groups_accept_or_reject_json_file(groupName , groupId , adminMobileNo, date, time, "reject" );
            }
        }

        //Coverting Arraylist to JsonArray
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (HashMap<String, String> data : groups_accept_or_reject) {

            JSONObject jsonObject = new JSONObject(data);
            jsonList.add(jsonObject);
        }

        JSONArray json_groups_accept_or_reject = new JSONArray(jsonList);

        String SJson_json_groups_accept_or_reject = json_groups_accept_or_reject.toString();
/*
        //Send json file....URl is not Set..set in Url list
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("groups_accept_or_reject" , SJson_json_groups_accept_or_reject);

        asyncHttpClient.get(this , UrlList.group_accept_or_not_json_file, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }


        });
*/
        return super.onStartCommand(intent, flags, startId);
    }

    public void add_to_DBController_database(String groupName ,String userContact,String groupId,String MobileNo,String date,String time){
        groups = new HashMap<String, String>();
        groups.put("mobileNo" , MobileNo);
        groups.put("groupName" , groupName);
        groups.put("groupMember" , userContact);
        groups.put("groupId" , groupId);
        groups.put("date" , date);
        groups.put("time" , time);
        dbController.insertUser(groups);
    }

    public void groups_accept_or_reject_json_file(String groupName ,String groupId,String adminMobileNo,String date,String time,
                                                  String accept_or_reject ){

        groups = new HashMap<String, String>();
        groups.put("groupName" , groupName);
        groups.put("groupId" , groupId);
        groups.put("adminMobileNo" , adminMobileNo);
        groups.put("date" , date);
        groups.put("time" , time);
        groups.put("accept_or_reject" , accept_or_reject);
        groups_accept_or_reject.add(groups);

    }

    public int Dialog(String title , String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flag = 1;
            }
        });
        alertDialog.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flag = 0;
            }
        });
        alertDialog.create();
        alertDialog.show();
        return flag;
    }
}
