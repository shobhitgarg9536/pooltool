package com.shobhit.pooltool.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shobhit.pooltool.utils.DividerItemDecoration;
import com.shobhit.pooltool.database.NotificationDatabase;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.utils.RecyclerTouchListener;
import com.shobhit.pooltool.listeners.StringInterface;
import com.shobhit.pooltool.adapter.MultipleContactAdapter;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.database.DBController;
import com.shobhit.pooltool.database.MemberDB;
import com.shobhit.pooltool.model.MultipleContactObject;
import com.shobhit.pooltool.network.AccountBackgroundWorker;
import com.shobhit.pooltool.utils.CheckInternetConnectivity;
import com.shobhit.pooltool.utils.GenerateUniqueString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shobhit-pc on 12/29/2016.
 */

public class MultipleContactSelect extends AppCompatActivity {

    RecyclerView contactRecyclerView;
    MultipleContactAdapter mutiplecontactAdapter;
    List<MultipleContactObject> contactList;
    ArrayList<HashMap<String, String>> contact_from_database ;
    ContactDatabase contactDatabase;
    EditText etSearch;
    String text = "";
    Toolbar toolbar;
    ProgressDialog progressDialog;
    String[] member = new String[100];
    int c=0;
    String groupName,userContact,groupId,date,time,admin="1";
    public static final String MyPREFERENCES = "UserContact";
    public static final String MyPREFERENCES2 = "Creategroupname" ;
    HashMap<String, String> queryValues;
    ArrayList<HashMap<String, String>> userList;
    DBController controller;
    MemberDB memberDB;
    GenerateUniqueString generateUniqueString;
    CheckInternetConnectivity checkInternetConnectivity;
    NotificationDatabase notificationDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showPB();

        notificationDatabase = new NotificationDatabase(this);
        controller = new DBController(this);
        memberDB = new MemberDB(this);
        generateUniqueString = new GenerateUniqueString();
        checkInternetConnectivity = new CheckInternetConnectivity();

        setContentView(R.layout.multiple_contact_select);
        contact_from_database = new ArrayList<HashMap<String, String>>();
        contactDatabase = new ContactDatabase(this);
        contact_from_database  = contactDatabase.getAllContact();

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userContact = (shared.getString("UserContact", ""));

        SharedPreferences shared2 = getSharedPreferences(MyPREFERENCES2, MODE_PRIVATE);
        groupName = (shared2.getString("GroupName", ""));
        shared2.edit().clear().commit();

        contactList = new ArrayList<>();
        contactRecyclerView = (RecyclerView) findViewById(R.id.multiple_contact_recycler_view);
        etSearch = (EditText) findViewById(R.id.edittextmutiplesearch);
        toolbar = (Toolbar) findViewById(R.id.toolbarMultipleContact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text = etSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                contactList.clear();
                contact_List(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSearch.setVisibility(View.GONE);

        mutiplecontactAdapter = new MultipleContactAdapter(contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        contactRecyclerView.setAdapter(mutiplecontactAdapter);
        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, contactRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final MultipleContactObject mContactObject = contactList.get(position);
                final CheckBox ckselected = (CheckBox) view.findViewById(R.id.checkboxMultipleselected);
                ckselected.setOnCheckedChangeListener(null);
                ckselected.setChecked(mContactObject.isSelected());
                ckselected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mContactObject.setSelected(b);

                    }
                });

                if(mContactObject.isSelected()){
                    mContactObject.setSelected(false);
                    ckselected.setChecked(false);
                }else{
                    mContactObject.setSelected(true);
                    ckselected.setChecked(true);

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        contact_List(text);
        hidePB();
    }

    private void contact_List(String textSearch) {
        for (int i = 0; i < contact_from_database.size(); i++) {
            if (contact_from_database.get(i).get("phoneName").toLowerCase().contains(textSearch) || textSearch.equals("")) {
                if (contact_from_database.get(i).get("invite").equals("0")) {
                    MultipleContactObject multiplecontactObject = new MultipleContactObject(contact_from_database.get(i).get("phoneName"), contact_from_database.get(i).get("phoneNumber") , false);
                    contactList.add(multiplecontactObject);
                }
            }
        }
        mutiplecontactAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contact, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createnewgroup:
                getSelectedContacts();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSelectedContacts() {

        StringBuffer sb = new StringBuffer();
        for (MultipleContactObject bean : contactList) {
            if (bean.isSelected()) {
                member[c] = bean.getNumber().toString();
                sb.append(bean.getName());
                sb.append(",");
                System.out.println(member[c]);
                c++;

            }
        }
        String s = sb.toString().trim();
        System.out.println(s);

        if (TextUtils.isEmpty(s)) {
            Toast.makeText(this, "Select atleast one Contact",
                    Toast.LENGTH_SHORT).show();
        }

        else {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(Calendar.getInstance().getTime());
            df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            time = df.format(Calendar.getInstance().getTime());

            groupId = userContact+generateUniqueString.getUniqueString();
            userList = new ArrayList<HashMap<String, String>>();
            queryValues = new HashMap<String, String>();
            // Add values extracted from Object
            queryValues.put("mobileNo", userContact);
            queryValues.put("groupName", groupName);
            queryValues.put("groupMember", userContact);
            queryValues.put("groupId", groupId);
            queryValues.put("date", date);
            queryValues.put("time", time);
            // Insert User into SQLite DB
          //  controller.insertUser(queryValues);
           queryValues.put("admin", admin);
            userList.add(queryValues);

/*
            queryValues = new HashMap<String, String>();
            // Add values extracted from Object
            queryValues.put("mobileNo", userContact);
            queryValues.put("groupName", groupName);
            queryValues.put("groupMember", userContact);
            queryValues.put("groupId", groupId);
            queryValues.put("date", date);
            queryValues.put("time", time);

*/

            admin="0";
            for(int i=0;i<c;i++) {
                queryValues = new HashMap<String, String>();
                // Add values extracted from Object
                queryValues.put("mobileNo", userContact);
                queryValues.put("groupName", groupName);
                queryValues.put("groupMember", member[i]);
                queryValues.put("groupId", groupId);
                queryValues.put("date", date);
                queryValues.put("time", time);
                queryValues.put("admin", admin);
                userList.add(queryValues);

            }
            arrayToJson(userList);



         hidePB();




        }
    }

    private void showPB() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Group...");
        progressDialog.show();
    }

    private void hidePB() {
        progressDialog.dismiss();
        progressDialog.cancel();
    }

    private void arrayToJson(ArrayList<HashMap<String, String>> equalorCustoMoneylist) {

        //Coverting Arraylist to JsonArray
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (HashMap<String, String> data : equalorCustoMoneylist) {

            JSONObject jsonObject = new JSONObject(data);
            jsonList.add(jsonObject);
        }

        JSONArray jsonMoneyList = new JSONArray(jsonList);

        String SJsonMoneyList = jsonMoneyList.toString();

        System.out.println(SJsonMoneyList);
        System.out.println(userContact + "  "+groupName+"  "+groupId);

        if(checkInternetConnectivity.isNetConnected(this)) {

            AccountBackgroundWorker accountBackgroundWorker = new AccountBackgroundWorker(new StringInterface() {
                @Override
                public void StringOutput(String output) {
                    if (output.equals("Group Successfully created")) {
                        queryValues = new HashMap<String, String>();
                        // Add values extracted from Object
                        queryValues.put("mobileNo", userContact);
                        queryValues.put("groupName", groupName);
                        queryValues.put("groupMember", userContact);
                        queryValues.put("groupId", groupId);
                        queryValues.put("date", date);
                        queryValues.put("time", time);
                        queryValues.put("admin", "1");
                        queryValues.put("latestTime", time);
                        controller.insertUser(queryValues);

                        queryValues = new HashMap<String, String>();
                        // Add values extracted from Object
                        queryValues.put("groupName", groupName);
                        queryValues.put("groupId", groupId);
                        queryValues.put("addedMobileNo", userContact);
                        queryValues.put("date", date);
                        queryValues.put("time", time);
                        queryValues.put("itemId", "");
                        queryValues.put("item", "");
                        queryValues.put("amountGiverType", "newGroup");
                        queryValues.put("amount", "");
                        notificationDatabase.insertNotification(queryValues);

                        for (int i = 0; i < c; i++) {
                            queryValues = new HashMap<String, String>();
                            // Add values extracted from Object
                            queryValues.put("groupcreator", userContact);
                            queryValues.put("member", member[i]);
                            queryValues.put("groupId", groupId);
                            queryValues.put("date", date);
                            queryValues.put("time", time);
                            queryValues.put("admin", "0");
                            memberDB.insertUser(queryValues);

                        }
                    }

                    final Dialog dialog = new Dialog(MultipleContactSelect.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog);
                    ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                    balanceimage.setImageResource(R.drawable.creategroup);
                    TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                    alerttext.setText(output);
                    Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                    yesButton.setText("OK");
                    Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                    noButton.setText("See Details");
                    noButton.setVisibility(View.GONE);
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startCreateAccount();
                        }
                    });
                    dialog.show();
                    /*
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MultipleContactSelect.this);
                    alertDialog.setTitle("Status");
                    alertDialog.setMessage(output);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startCreateAccount();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.create();
                    alertDialog.show();*/
                }
            } , this);
            accountBackgroundWorker.execute("createGroup"  , SJsonMoneyList);

        /*
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            RequestParams params1 = new RequestParams();

            params1.put("create_group_list", SJsonMoneyList);

            asyncHttpClient.get(UrlList.create_group, params1, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    String ndj = response.toString();
                    System.out.println(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);

                    System.out.println(responseString);

                    if (responseString.equals("Group Successfully created")) {
                        queryValues = new HashMap<String, String>();
                        // Add values extracted from Object
                        queryValues.put("mobileNo", userContact);
                        queryValues.put("groupName", groupName);
                        queryValues.put("groupMember", userContact);
                        queryValues.put("groupId", groupId);
                        queryValues.put("date", date);
                        queryValues.put("time", time);
                        controller.insertUser(queryValues);
                    }
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MultipleContactSelect.this);
                    alertDialog.setTitle("Status");
                    alertDialog.setMessage(responseString);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startCreateAccount();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.create();
                    alertDialog.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                }

            });*/

        }else{
            checkInternetConnectivity.alertDialog(this);
        }
    }


    private void startCreateAccount(){
        Intent i = new Intent(this, ViewPagerActivity.class);
        startActivity(i);
    }
}
