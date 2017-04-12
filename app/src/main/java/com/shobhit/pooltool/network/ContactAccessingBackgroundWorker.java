package com.shobhit.pooltool.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.shobhit.pooltool.database.ContactDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Shobhit-pc on 1/17/2017.
 */

public class ContactAccessingBackgroundWorker extends AsyncTask<String, String ,String> {
    HashMap<String , String> hashMap;
    ArrayList<HashMap<String,String>> contact_list;
    HashMap<String ,String> queryValues;
    ContactDatabase contactDatabase;
    ArrayList<String> contact_list_from_database;
    ArrayList<HashMap<String , String>> contact_list_from_database_alldetails;
    Context ctx;
    ProgressDialog progressDialog;

    public ContactAccessingBackgroundWorker(Context context){
        this.ctx = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Accessing");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        contactDatabase = new ContactDatabase(ctx);
        contact_list = new ArrayList<HashMap<String, String>>();
        contact_list_from_database = new ArrayList<String>();
        contact_list_from_database_alldetails = new ArrayList<HashMap<String, String>>();
        try {
            contact_list_from_database = contactDatabase.getAllPhoneNumber();
            contact_list_from_database_alldetails = contactDatabase.getAllContact();

            Cursor phones = ctx.getContentResolver().query(
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
            for (int i = 0; i < contact_list.size(); i++) {
                String phoneNo = tendigitMobileNo(contact_list.get(i).get("phoneNumber"));
                if (!contact_list_from_database.contains(phoneNo)) {
                    System.out.print(contact_list.get(i).get("phoneNumber") + " , ");
                    hashMap = new HashMap<String, String>();
                    hashMap.put("phoneName", contact_list.get(i).get("phoneName"));
                    //phoneNumber is unique in database
                    hashMap.put("phoneNumber", phoneNo);
                    hashMap.put("invite", "1");
                    contactDatabase.insertContact(hashMap);
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
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
