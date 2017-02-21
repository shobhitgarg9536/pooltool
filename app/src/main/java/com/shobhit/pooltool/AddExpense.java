package com.shobhit.pooltool;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Shobhit-pc on 12/25/2016.
 */

public class AddExpense extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    EditText item,money;
    TextInputLayout inputTextItem,inputTextMoney;
    Switch shared;
    RadioGroup moneySharing;
    LinearLayout notshared;
    Button addexpense;
    Toolbar toolbarExpense;
    String Sshared ="1",Sequalorcustomise = "equal",date,time,Sitem,Smoney , groupName;
   // public static final String MyPREFERENCES = "UID";
    public static final String UserDetail = "UserDetail";
    public static final String MYCONTACT = "UserContact" ;
    ArrayList<HashMap<String,String>> moneylist = null;
    MemberDB controller = new MemberDB(this);
    ArrayList<HashMap<String, String>> userList;
    ArrayList<HashMap<String, String>> equalmoneylist = null;
    ArrayList<HashMap<String, String>> addItemArrayList = null;
    int totalMoney;
    String uid,groupId,mycontact,customiseitemId,itemId;
    GenerateUniqueString generateUniqueString;
    String jsonAddItem,jsonAddExpense;
    HashMap<String, String> moneymap;
    NotificationDatabase notificationDatabase;
    CheckInternetConnectivity checkInternetConnectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addexpense);

        item = (EditText) findViewById(R.id.etitem);
        money = (EditText) findViewById(R.id.etmoney);
        shared = (Switch) findViewById(R.id.swshared);
        moneySharing = (RadioGroup)findViewById(R.id.rgmoneysharing);
        notshared = (LinearLayout)  findViewById(R.id.llnotshared);
        addexpense = (Button) findViewById(R.id.btaddexpense);
        inputTextItem = (TextInputLayout) findViewById(R.id.tilitem);
        inputTextMoney = (TextInputLayout) findViewById(R.id.tilmoney);
        toolbarExpense = (Toolbar) findViewById(R.id.tbaddexpense);
        moneySharing.setOnCheckedChangeListener(this);
        shared.setOnCheckedChangeListener(this);
        addexpense.setOnClickListener(this);

        item.addTextChangedListener(new MyTextWatcher(item));
        money.addTextChangedListener(new MyTextWatcher(money));

        setSupportActionBar(toolbarExpense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        generateUniqueString = new GenerateUniqueString();
        notificationDatabase = new NotificationDatabase(this);
        checkInternetConnectivity = new CheckInternetConnectivity();

       // SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
       // uid = (shared.getString("UID", ""));
       SharedPreferences shared = getSharedPreferences(UserDetail, MODE_PRIVATE);
        groupId = (shared.getString("groupid", ""));
        groupName = shared.getString("groupName", "");
        shared = getSharedPreferences(MYCONTACT, MODE_PRIVATE);
        mycontact = (shared.getString("UserContact", ""));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rbequal:
                Sequalorcustomise ="equal";
                break;
            case R.id.rbcustomise:
                Sequalorcustomise = "customise";
                if(validateItem() && validateMoney()){
                    try {
                        Intent intent = new Intent(AddExpense.this, Customise.class);
                        intent.putExtra("money" ,money.getText().toString());
                        startActivityForResult(intent , 2);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bumoney = data.getExtras();

            moneylist = (ArrayList<HashMap<String,String>>)bumoney.getSerializable("money");
            totalMoney = bumoney.getInt("totalMoney");
            customiseitemId = bumoney.getString("itemId");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(!b) {
            notshared.setVisibility(View.GONE);
            Sequalorcustomise = "Not Shared";
            Sshared = "0";
        }
        else {
            notshared.setVisibility(View.VISIBLE);
            Sequalorcustomise = "equal";
            Sshared = "1";
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btaddexpense) {

            if (validateItem() && validateMoney()) {
                Sitem = item.getText().toString();
                Smoney = money.getText().toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date = df.format(Calendar.getInstance().getTime());
                df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                time = df.format(Calendar.getInstance().getTime());

                itemId = groupId+generateUniqueString.getUniqueString();

                moneymap = new HashMap<String, String>();
                addItemArrayList = new ArrayList<>();
                //  moneymap.put("Uid", uid);
                moneymap.put("groupId" , groupId);
                moneymap.put("mobileNo", mycontact);
                if(Sequalorcustomise.equals("customise"))
                moneymap.put("itemId", customiseitemId);
                else
                moneymap.put("itemId", itemId);
                moneymap.put("item", Sitem);
                moneymap.put("money", Smoney);
                moneymap.put("equalOrCustomise", Sequalorcustomise);
                moneymap.put("date", date);
                moneymap.put("time", time);
                addItemArrayList.add(moneymap);
                jsonAddItem = arrayToJson(addItemArrayList);

/*
                AsyncHttpClient client = new AsyncHttpClient();
                // Http Request Params Object
                RequestParams params = new RequestParams();
               // params.put("uid", uid);
                params.put("groupId" , groupId);
                params.put("mobileNo" , mycontact);
                if(Sequalorcustomise.equals("customise"))
                    params.put("itemId", customiseitemId);
                else
                    params.put("itemId", itemId);
                params.put("item", Sitem);
                params.put("money", Smoney);
                params.put("equalOrCustomise", Sequalorcustomise);
                params.put("date", date);
                params.put("time", time);

                // Make Http call to getusers.php
                client.post(UrlList.add_expense, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddExpense.this);
                        alertDialog.setTitle("Status");
                        alertDialog.setMessage(responseString);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.create();
                        alertDialog.show();
                        super.onSuccess(statusCode, headers, responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
*/
                if(Sshared.equals("0")){
                    equalmoneylist = new ArrayList<HashMap<String, String>>();

                    moneymap = new HashMap<String, String>();
                  //  moneymap.put("Uid", uid);
                    moneymap.put("fromMobileNo" , mycontact);
                    moneymap.put("itemId", itemId);
                    moneymap.put("toMobileNo", mycontact);
                    moneymap.put("amount", Smoney);
                    moneymap.put("groupId", groupId);
                    moneymap.put("date", date);
                    moneymap.put("time", time);
                    equalmoneylist.add(moneymap);
                    if(equalmoneylist!=null)
                     jsonAddExpense =  arrayToJson(equalmoneylist);
                }

                if(Sequalorcustomise.equals("equal")){
                    equalmoneylist = new ArrayList<HashMap<String, String>>();
                    userList = controller.getAllMember(groupId);
                    int count = userList.size();
                    System.out.println(userList);
                    float equalamount = (float)Integer.valueOf(Smoney) / (count+1);
                    HashMap<String, String> moneymap;
                    for(int i =0 ; i< count ; i++){
                        moneymap = new HashMap<String, String>();
                      //  moneymap.put("Uid", uid);
                        moneymap.put("fromMobileNo" , mycontact);
                        moneymap.put("itemId",  itemId);
                        moneymap.put("toMobileNo", userList.get(i).get("member"));
                        moneymap.put("amount", String.valueOf(equalamount));
                        moneymap.put("groupId", groupId);
                        moneymap.put("date", date);
                        moneymap.put("time", time);
                        equalmoneylist.add(moneymap);
                    }
                    moneymap = new HashMap<String, String>();
                //    moneymap.put("Uid", uid);
                    moneymap.put("fromMobileNo" , mycontact);
                    moneymap.put("itemId",  itemId);
                    moneymap.put("toMobileNo", mycontact);
                    moneymap.put("amount", String.valueOf(equalamount));
                    moneymap.put("groupId", groupId);
                    moneymap.put("date", date);
                    moneymap.put("time", time);
                    equalmoneylist.add(moneymap);
                   jsonAddExpense =  arrayToJson(equalmoneylist);
                }

                if(Sequalorcustomise.equals("customise") && moneylist!=null) {
                    int myMoney = Integer.valueOf(Smoney)-totalMoney;


                    if(myMoney != 0) {
                        HashMap<String, String> moneymap = new HashMap<String, String>();
                    //    moneymap.put("Uid", uid);
                        moneymap.put("fromMobileNo" , mycontact);
                        moneymap.put("itemId", customiseitemId);
                        moneymap.put("toMobileNo", mycontact);
                        moneymap.put("amount", String.valueOf(myMoney));
                        moneymap.put("groupId", groupId);
                        moneymap.put("date", date);
                        moneymap.put("time", time);
                        moneylist.add(moneymap);
                    }
                  jsonAddExpense =   arrayToJson(moneylist);
                }
                System.out.println(checkInternetConnectivity.isNetConnected(AddExpense.this));
                if(checkInternetConnectivity.isNetConnected(AddExpense.this))
                sendToServer(jsonAddItem , jsonAddExpense);
                else
                    checkInternetConnectivity.alertDialog(AddExpense.this);
            }
        }
    }

    private String arrayToJson(ArrayList<HashMap<String, String>> equalorCustoMoneylist) {

        //Coverting Arraylist to JsonArray
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (HashMap<String, String> data : equalorCustoMoneylist) {

            JSONObject jsonObject = new JSONObject(data);
            jsonList.add(jsonObject);
        }

        JSONArray jsonMoneyList = new JSONArray(jsonList);

        String SJson = jsonMoneyList.toString();



/*
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("moneyList", SJsonMoneyList);

        asyncHttpClient.post(UrlList.add_expenditure, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

*/
        return SJson;
    }

    public void sendToServer(String jsonAddItem , String jsonaddExpense){
        System.out.println(jsonAddItem);
        System.out.println(jsonaddExpense);

        ExpenditureBackgroundWorker expenditureBackgroundWorker = new ExpenditureBackgroundWorker(new StringInterface() {
            @Override
            public void StringOutput(final String output) {


                if(output.equals("item inserted successfully")){
                    moneymap = new HashMap<String, String>();
                    moneymap.put("groupName", groupName);
                    moneymap.put("groupId" , groupId);
                    moneymap.put("addedMobileNo", mycontact);
                    moneymap.put("date", date);
                    moneymap.put("time", time);
                    if(Sequalorcustomise.equals("customise"))
                        moneymap.put("itemId", customiseitemId);
                    else
                        moneymap.put("itemId", itemId);
                    moneymap.put("item", Sitem);
                    moneymap.put("amountGiverType", "newItem");
                    moneymap.put("amount", "");
                    notificationDatabase.insertNotification(moneymap);

                }

                final Dialog dialog = new Dialog(AddExpense.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                if(output.equals("item inserted successfully"))
                balanceimage.setImageResource(R.drawable.addexpense);
                else
                    balanceimage.setImageResource(R.drawable.error);
                final TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                alerttext.setText(output);
                Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                yesButton.setText("Ok");
                Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                noButton.setText("See Details");
                noButton.setVisibility(View.GONE);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(output.equals("item inserted successfully")){
                            startGroupsActivity();
                        }else{
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

/*
                final String response = output;
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddExpense.this);
                alertDialog.setTitle("Status");
                alertDialog.setMessage(output);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(response.equals("item inserted successfully")){
                            startGroupsActivity();
                        }
                    }
                });
                alertDialog.create();
                alertDialog.show();*/
            }
        } , AddExpense.this);
        expenditureBackgroundWorker.execute(jsonAddItem , jsonaddExpense);
    }

    public void startGroupsActivity(){
        Intent i = new Intent(this , ViewPagerActivity.class);
        startActivity(i);
    }

    private boolean validateItem() {
        if (item.getText().toString().trim().isEmpty()) {
            inputTextItem.setError("Enter Item name");
            //requsting focus on editText
            requestFocus(item);
            return false;
        } else {
            inputTextItem.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateMoney() {
        if (money.getText().toString().trim().isEmpty()) {
            inputTextMoney.setError("Enter money spend");
            //requsting focus on editText
            requestFocus(money);
            return false;
        } else {
            inputTextMoney.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            //showing keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.etitem:
                    inputTextItem.setErrorEnabled(false);
                    break;
                case R.id.etmoney:
                    inputTextMoney.setErrorEnabled(false);
                    break;
            }
        }
        public void afterTextChanged(Editable editable) {

        }
    }
}