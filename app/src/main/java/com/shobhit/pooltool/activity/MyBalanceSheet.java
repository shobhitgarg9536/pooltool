package com.shobhit.pooltool.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.shobhit.pooltool.R;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.listeners.BalanceInterface;
import com.shobhit.pooltool.network.MyBalance;
import com.shobhit.pooltool.utils.CheckInternetConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


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
                               // queryValues.put("date", obj.get("date").toString());
                                queryValues.put("time", obj.get("time").toString());
                                queryValues.put("groupMember", getContactName(obj.get("groupMember").toString()));
                                // Insert User into SQLite DB
                                itemDetails.add(queryValues);
                            }

                        }

                        ListAdapter adapter = new SimpleAdapter(MyBalanceSheet.this, itemDetails, R.layout.mybalancesheetview, new String[]{
                                "item", "money", "amount", "time", "groupMember"}, new int[]{R.id.tvItemName, R.id.tvTotalMoney,
                                R.id.tvMySharing, R.id.tvTime, R.id.tvSharedBy});
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
                    R.id.tvMySharing  , R.id.tvTime });
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
