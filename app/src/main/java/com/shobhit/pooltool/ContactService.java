package com.shobhit.pooltool;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Shobhit-pc on 12/27/2016.
 */

public class ContactService extends Service{
    HashMap<String , String> hashMap;
    ArrayList<HashMap<String,String>> contact_list;
    HashMap<String ,String> queryValues;
    ContactDatabase contactDatabase;
    ArrayList<String> contact_list_from_database;
    ArrayList<HashMap<String , String>> contact_list_from_database_alldetails;
    CheckInternetConnectivity checkInternetConnectivity;


    @Override
    public void onCreate() {
        super.onCreate();
        contactDatabase = new ContactDatabase(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        contact_list = new ArrayList<HashMap<String, String>>();
        contact_list_from_database = new ArrayList<String>();
        contact_list_from_database_alldetails = new ArrayList<HashMap<String, String>>();
        try {
        contact_list_from_database = contactDatabase.getAllPhoneNumber();
            contact_list_from_database_alldetails = contactDatabase.getAllContact();

            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);

            while (phones.moveToNext()) {
                String phoneName = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String phoneImage = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                //to check wheather contact is present in contact database or not if not then add in arraylist

                    hashMap = new HashMap<String, String>();
                    hashMap.put("phoneName", phoneName);
                    hashMap.put("phoneNumber", phoneNumber);
                    contact_list.add(hashMap);

            }
            phones.close();
            //to sort the contact list
            Collections.sort(contact_list, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> stringStringHashMap, HashMap<String, String> t1) {

                    return stringStringHashMap.get("phoneName").compareTo(t1.get("phoneName"));
                }
            });
            System.out.print(contact_list_from_database);
            System.out.println();
            for(int i = 0 ; i < contact_list.size() ; i++){
                String phoneNo = tendigitMobileNo(contact_list.get(i).get("phoneNumber"));
                if(!contact_list_from_database.contains(phoneNo)){
                    System.out.print(contact_list.get(i).get("phoneNumber")+" , ");
                    hashMap = new HashMap<String, String>();
                    hashMap.put("phoneName", contact_list.get(i).get("phoneName"));
                    //phoneNumber is unique in database
                    hashMap.put("phoneNumber", phoneNo );
                    hashMap.put("invite" , "1");
                    contactDatabase.insertContact(hashMap);
                }
            }

            if(checkInternetConnectivity.isNetConnected(ContactService.this)) {
      /*          ContactBackgroundworker contactBackgroundworker = new ContactBackgroundworker(new ContactInterface() {
                    @Override
                    public void phoneNoList(String output) {
                        System.out.println(output);
                        try {
                            JSONArray jsonArray = new JSONArray(output);

                            System.out.println(jsonArray.length());
                            if (jsonArray.length() != 0) {
                                System.out.println("rtyuuu");
                                System.out.println();
                                // Loop through each array element, get JSON object which has userid and username
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    // Get JSON object
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    System.out.println(obj.get("phoneNumber") + ",");
                                    if (contact_list_from_database.contains(obj.get("phoneNumber"))) {
                                        int index = contact_list_from_database.indexOf(obj.get("phoneNumber").toString().trim());
                                        System.out.println("index " + index + ",");
                                        if (contact_list_from_database_alldetails.get(index).get("invite").equals("1")) {
                                            contactDatabase.update(obj.get("phoneNumber").toString());
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                },ContactService.this);
                contactBackgroundworker.execute();
*/
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String tendigitMobileNo(String mobileNo){
        String tendigitNumber = "";
        char ch;
        int c=10;
        for(int i= mobileNo.length()-1 ; i>=0 ; i--){
            ch = mobileNo.charAt(i);
            if(c>0 && (ch=='0' || ch=='1' || ch=='2' || ch=='3' || ch=='4' || ch=='5' || ch=='6' || ch=='7' ||
                    ch=='8' || ch=='9' )){
                tendigitNumber = ch +tendigitNumber;
                c--;
            }
        }
        return tendigitNumber;
    }

}
