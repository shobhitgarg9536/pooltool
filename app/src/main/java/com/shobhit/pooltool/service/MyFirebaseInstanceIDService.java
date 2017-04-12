package com.shobhit.pooltool.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shobhit.pooltool.listeners.StringInterface;
import com.shobhit.pooltool.network.FirebaseTokenBackgroundWorker;
import com.shobhit.pooltool.utils.Config;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    public static final String MyPREFERENCES = "UserContact";
    SharedPreferences pref;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("refreshedToken" +refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

    // sending reg id to your server
    sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {

            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String mobileNo = sharedpreferences.getString("UserContact", "");
        if(!mobileNo.equals("")) {
            // sending fcm token to server
            FirebaseTokenBackgroundWorker firebaseTokenBackgroundWorker = new FirebaseTokenBackgroundWorker(new StringInterface() {
                @Override
                public void StringOutput(String output) {

                    System.out.println(output);
                    if (output.equals("Firebase updated")) {
                        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("FirebaseIdSendToServer", "1");//1 means firebase id is registered
                        editor.commit();
                    }
                }
            });
            firebaseTokenBackgroundWorker.execute(mobileNo, token);
        }
       /*
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("firebaseUid", token);
            params.put("mobileNo", mobileNo);
            asyncHttpClient.post(UrlList.update_firebase_uid, params, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }
            });
        */
            Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.putString("FirebaseIdSendToServer", "0");//0 means firebase id is not updated to server
        editor.commit();
    }
}