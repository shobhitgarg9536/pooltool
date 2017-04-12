package com.shobhit.pooltool.service;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.shobhit.pooltool.database.NotificationDatabase;
import com.shobhit.pooltool.utils.NotificationUtils;
import com.shobhit.pooltool.activity.MainActivity;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.database.DBController;
import com.shobhit.pooltool.utils.Config;

import java.util.HashMap;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    public static final String MyPREFERENCES = "UserContact" ;
    String userContact;
    ContactDatabase contactDatabase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

         /*   String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp); */
            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            userContact = (shared.getString("UserContact", ""));

            DBController dbController = new DBController(this);
            contactDatabase = new ContactDatabase(this);
            HashMap<String , String> hashMap = null;

            String groupName="",groupId="",addedMobileNo="",date="",time="",itemId="",item="",message=""
               ,adminState="",latestTime ="" ,amountGiverMobileNo="",amount="";
            String group_or_item = data.getString("group_or_item");
            if(group_or_item.equals("newGroup")) {
                groupName = data.getString("groupName");
                groupId = data.getString("groupId");
                addedMobileNo = data.getString("mobileNo");
                date = data.getString("date");
                time = data.getString("time");
                latestTime = data.getString("time");
                message = getContactName(addedMobileNo) + " has added you to the new group";

                hashMap = new HashMap<String, String>();
                hashMap.put("mobileNo" , addedMobileNo);
                hashMap.put("groupName" , groupName);
                hashMap.put("groupMember" , userContact);
                hashMap.put("groupId" , groupId);
                hashMap.put("date" , date);
                hashMap.put("time" , time);
                hashMap.put("latestTime" , time);
                hashMap.put("admin" , "0");
                dbController.insertUser(hashMap);
            }

           else if(group_or_item.equals("newItem")){
                groupId = data.getString("groupId");
                groupName = dbController.getGroupName(groupId);
                addedMobileNo = data.getString("fromMobileNo");
                date = data.getString("date");
                time = data.getString("time");
                itemId = data.getString("itemId");
                item = data.getString("item");
                message = getContactName(addedMobileNo) + "  has added "+item;

                dbController.Update(groupId , time);
            }

            else if(group_or_item.equals("settleUpPay") || group_or_item.equals("settleUpPayRequest")) {
                amountGiverMobileNo = data.getString("amountGiverMobileNo");
                date = data.getString("date");
                groupName = data.getString("groupName");
                time = data.getString("time");
                groupId = data.getString("groupId");
                amount = data.getString("amount");
                if(group_or_item.equals("settleUpPay"))
                message = getContactName(addedMobileNo) + "  of group "+groupName+" has paid you the money";
                if(group_or_item.equals("settleUpPayRequest"))
                message = getContactName(addedMobileNo) + "  of group "+groupName+" has requested to his money";
                dbController.Update(groupId , time);

            }

            else if(group_or_item.equals("adminState")) {
                adminState = data.getString("adminState");
                date = data.getString("date");
                groupName = data.getString("groupName");
                time = data.getString("time");
                groupId = data.getString("groupId");
                if(adminState.equals("removeAdmin"))
                message = "You are removed from admin of group "+groupName;
                if(adminState.equals("makeAdmin"))
                    message = "You are now the admin of group "+groupName;
                dbController.UpdateAdmin(groupId , time , adminState);

            }

            else {}

            if(group_or_item.equals("newGroup") || group_or_item.equals("newItem")) {
                hashMap = new HashMap<String, String>();
                hashMap.put("groupName", groupName);
                hashMap.put("groupId", groupId);
                hashMap.put("addedMobileNo", addedMobileNo);
                hashMap.put("date", date);
                hashMap.put("time", time);
                if (group_or_item.equals("newGroup")) {
                    hashMap.put("itemId", "");
                    hashMap.put("item", "");
                    hashMap.put("amountGiverType" , group_or_item);
                    hashMap.put("amount" , "");
                } else if (group_or_item.equals("newItem")) {
                    hashMap.put("itemId", itemId);
                    hashMap.put("item", item);
                    hashMap.put("amountGiverType" , group_or_item);
                    hashMap.put("amount" , "");

                }
            }
            else if(group_or_item.equals("settleUpPay") || group_or_item.equals("settleUpPayRequest")) {
                hashMap = new HashMap<String, String>();
                hashMap.put("groupName", groupName);
                hashMap.put("amountGiverType", group_or_item);
                hashMap.put("addedMobileNo", amountGiverMobileNo);
                hashMap.put("groupId", groupId);
                hashMap.put("amount", amount);
                hashMap.put("date", date);
                hashMap.put("time", time);
                hashMap.put("itemId", "");
                hashMap.put("item", "");
            }
            else if(group_or_item.equals("adminState")) {
                hashMap = new HashMap<String, String>();
                hashMap.put("groupName", groupName);
                hashMap.put("amountGiverType", group_or_item);
                hashMap.put("addedMobileNo", "");
                hashMap.put("groupId", groupId);
                hashMap.put("amount", adminState);//adminstate will be save in amount
                hashMap.put("date", date);
                hashMap.put("time", time);
                hashMap.put("itemId", "");
                hashMap.put("item", "");
            }

            NotificationDatabase notificationDatabase = new NotificationDatabase(this);
            notificationDatabase.insertNotification(hashMap);

            String timestamp = date +','+time;
            String title = groupName;

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
             //   if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            //    } else {
                    // image is present, show notification with image
             //       showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
             //   }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
    public String getContactName(String contact){
        String name;
        name = contactDatabase.getContact(contact);
        if(name.isEmpty())
            return contact;
        else
            return name;
    }
}