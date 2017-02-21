package com.shobhit.pooltool;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Shobhit-pc on 1/2/2017.
 */

public class ContactBackgroundworker extends AsyncTask<String , String , String> {

    Context ctx;
    ContactDatabase contactDatabase;
    ArrayList<String> allContactList;
    ProgressDialog progressDialog;
    ArrayList<String> contact_list_from_database;
    ArrayList<HashMap<String , String>> contact_list_from_database_alldetails;

    public ContactInterface delegate = null;

    public ContactBackgroundworker(Context context){
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Accessing Contacts...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {


        try{
            URL url = new URL(UrlList.get_contact_list);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result = "";
            String line ="";
            while((line = bufferedReader.readLine())!= null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            System.out.println(result);
            contact_list_from_database = new ArrayList<String>();
            contact_list_from_database_alldetails = new ArrayList<HashMap<String, String>>();
            contactDatabase = new ContactDatabase(ctx);

            try {
                contact_list_from_database = contactDatabase.getAllPhoneNumber();
                contact_list_from_database_alldetails = contactDatabase.getAllContact();
                JSONArray jsonArray = new JSONArray(result);

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
            }catch (JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return  result;
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
        contactDatabase = new ContactDatabase(ctx);
        AsyncHttpClient send_contact_list = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("contact_list" , strings[0]);
        send_contact_list.get(ctx, UrlList.get_contact_list , params , new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                System.out.println("contact list connect sussessfully");
                try {
                    contactDatabase = new ContactDatabase(ctx);
                    JSONArray jsonArray = new JSONArray(responseString);

                    if(jsonArray.length()!=0){
                        // Loop through each array element, get JSON object which has userid and username
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get JSON object
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            System.out.println(obj.get("phoneName"));
                            System.out.println(obj.get("phoneNumber"));
                            System.out.println(obj.get("invite"));
                            // DB QueryValues Object to insert into SQLite

                            queryValues = new HashMap<String, String>();
                            // Add values extracted from Object
                            queryValues.put("phoneName", obj.get("phoneName").toString());
                            queryValues.put("phoneNumber", obj.get("phoneNumber").toString());
                            queryValues.put("invite", obj.get("invite").toString());
                            // Insert User into SQLite DB
                            contactDatabase.insertContact(queryValues);

                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("conatct list connection failed");

            }
        });
        */
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
}
