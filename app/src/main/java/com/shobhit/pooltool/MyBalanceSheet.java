package com.shobhit.pooltool;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


public class MyBalanceSheet extends AppCompatActivity {

    Toolbar tbBalanceSheet;
    SharedPreferences sharedPreferences;
    public static final String MyGroupId = "UserDetail";
    public static final String MYCONTACT = "UserContact" ;
    String groupid , myContact;
    ListView lvBalanceSheet;
    ContactDatabase contactDb;
    HashMap<String, String> queryValues;
    ArrayList<HashMap<String, String>> itemDetails;
    CheckInternetConnectivity checkInternetConnectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybalancesheet);

        tbBalanceSheet = (Toolbar) findViewById(R.id.tbbalancesheet);
        lvBalanceSheet = (ListView) findViewById(R.id.lvbalancesheet);

        setSupportActionBar(tbBalanceSheet);
        getSupportActionBar().setTitle("My Balance Sheet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkInternetConnectivity = new CheckInternetConnectivity();
        contactDb = new ContactDatabase(this);

        sharedPreferences = getSharedPreferences(MyGroupId, MODE_PRIVATE);
        groupid = (sharedPreferences.getString("groupid", ""));
        sharedPreferences = getSharedPreferences(MYCONTACT, MODE_PRIVATE);
        myContact = (sharedPreferences.getString("UserContact", ""));

        if(checkInternetConnectivity.isNetConnected(MyBalanceSheet.this)){
        MyBalance myBalance = new MyBalance(new BalanceInterface() {
            @Override
            public void money(String output) {
                if (output.equals("Something went wrong.Try Again") || output.equals("None of the transaction take place")) {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MyBalanceSheet.this);
                    alertDialog.setTitle("Status");
                    alertDialog.setMessage(output);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                } else {
                    try {
                        // Extract JSON array from the response
                        JSONArray arr = new JSONArray(output);
                        // If no of array elements is not zero
                        if (arr.length() != 0) {
                            itemDetails = new ArrayList<HashMap<String, String>>();
                            // Loop through each array element, get JSON object which has userid and username
                            for (int i = 0; i < arr.length(); i++) {
                                // Get JSON object
                                JSONObject obj = (JSONObject) arr.get(i);
                                System.out.println(obj.get("amount"));
                                System.out.println(obj.get("item"));
                                System.out.println(obj.get("money"));
                                System.out.println(obj.get("date"));
                                System.out.println(obj.get("time"));
                                // DB QueryValues Object to insert into SQLite

                                queryValues = new HashMap<String, String>();
                                // Add values extracted from Object
                                queryValues.put("amount", obj.get("amount").toString());
                                queryValues.put("item", obj.get("item").toString());
                                queryValues.put("money", obj.get("money").toString());
                                queryValues.put("date", obj.get("date").toString());
                                queryValues.put("time", obj.get("time").toString());
                                queryValues.put("groupMember", getContactName(obj.get("groupMember").toString()));
                                // Insert User into SQLite DB
                                itemDetails.add(queryValues);
                            }

                        }

                        ListAdapter adapter = new SimpleAdapter(MyBalanceSheet.this, itemDetails, R.layout.mybalancesheetview, new String[]{
                                "item", "money", "amount", "date", "time", "groupMember"}, new int[]{R.id.tvItemName, R.id.tvTotalMoney,
                                R.id.tvMySharing, R.id.tvDate, R.id.tvTime, R.id.tvSharedBy});
                        lvBalanceSheet.setAdapter(adapter);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }, MyBalanceSheet.this);
        myBalance.execute("myBalanceSheet", myContact, groupid);
    }
/*
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        params.put("groupid", groupid);
        params.put("myContact", myContact);

        client.post(UrlList.my_balance_sheet, params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                // Hide ProgressBar
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    private void jsontoarray(String response) {


        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            // If no of array elements is not zero
            if(arr.length() != 0){
                itemDetails = new ArrayList<HashMap<String, String>>();
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("amount"));
                    System.out.println(obj.get("item"));
                    System.out.println(obj.get("money"));
                    System.out.println(obj.get("date"));
                    System.out.println(obj.get("time"));
                    // DB QueryValues Object to insert into SQLite

                    queryValues = new HashMap<String, String>();
                    // Add values extracted from Object
                    queryValues.put("amount", obj.get("amount").toString());
                    queryValues.put("item", obj.get("item").toString());
                    queryValues.put("money", obj.get("money").toString());
                    queryValues.put("date", obj.get("date").toString());
                    queryValues.put("time", obj.get("time").toString());
                    // Insert User into SQLite DB
                    itemDetails.add(queryValues);
                }

            }

            ListAdapter adapter = new SimpleAdapter(MyBalanceSheet.this , itemDetails , R.layout.mybalancesheetview , new String[] {
                    "item" ,"money","amount","date","time"  }, new int[] { R.id.tvItemName , R.id.tvTotalMoney ,
                    R.id.tvMySharing , R.id.tvDate , R.id.tvTime });
            lvBalanceSheet.setAdapter(adapter);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String getContactName(String contact){
        String name;
        name = contactDb.getContact(contact);
        if(name.isEmpty())
            return contact;
        else
            return name;
    }
}
