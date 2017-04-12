package com.shobhit.pooltool.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhit.pooltool.utils.DividerItemDecoration;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.utils.RecyclerTouchListener;
import com.shobhit.pooltool.adapter.SettleUpAdapter;
import com.shobhit.pooltool.network.SettleUpBackgroundWorker;
import com.shobhit.pooltool.model.SettleUpObject;
import com.shobhit.pooltool.listeners.StringInterface;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.database.MemberDB;
import com.shobhit.pooltool.utils.CheckInternetConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shobhit-pc on 1/7/2017.
 */

public class SettleUp extends AppCompatActivity{

    public static final String UserDetail = "UserDetail";
    public static final String MYCONTACT = "UserContact";
    String groupId,mycontact,groupName;
    MemberDB memberDB;
    ArrayList<HashMap<String , String>> memberContactFromDb;
    CheckInternetConnectivity checkInternetConnectivity;
    RecyclerView payRecyclerView , askRecyclerView;
    List<SettleUpObject> paidToList;
    List<SettleUpObject> paidByList;
    SettleUpAdapter settleUpAdapter ,msettleUpAdapter;
    Toolbar tbsettleUp;
    TextView paidBytext , paidToText;
    ContactDatabase contactDatabase;
    String date,time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settleup);
        payRecyclerView = (RecyclerView) findViewById(R.id.settle_up_paid_by_you_recycler_view);
        askRecyclerView = (RecyclerView ) findViewById(R.id.settle_up_paid_to_you_recycler_view);
        tbsettleUp = (Toolbar) findViewById(R.id.tbsettleUp);
        paidBytext = (TextView) findViewById(R.id.textViewSettleUpPaidByText);
        paidToText = (TextView) findViewById(R.id.textViewSettleUpPaidToText);

        setSupportActionBar(tbsettleUp);
        getSupportActionBar().setTitle("Settle Up");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences shared = getSharedPreferences(UserDetail, MODE_PRIVATE);
        groupId = (shared.getString("groupid", ""));
        groupName = (shared.getString("groupName", ""));
        shared = getSharedPreferences(MYCONTACT, MODE_PRIVATE);
        mycontact = (shared.getString("UserContact", ""));

        memberDB = new MemberDB(SettleUp.this);
        contactDatabase = new ContactDatabase(SettleUp.this);
        memberContactFromDb = new ArrayList<HashMap<String, String>>();
        paidByList = new ArrayList<>();
        paidToList = new ArrayList<>();
        memberContactFromDb = memberDB.getMemberContactNo(groupId);
        checkInternetConnectivity = new CheckInternetConnectivity();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        time = df.format(Calendar.getInstance().getTime());

        settleUpAdapter = new SettleUpAdapter(paidByList , 0 );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SettleUp.this);
        payRecyclerView.setLayoutManager(mLayoutManager);
        payRecyclerView.setItemAnimator(new DefaultItemAnimator());
        payRecyclerView.addItemDecoration(new DividerItemDecoration(SettleUp.this, LinearLayoutManager.VERTICAL));
        payRecyclerView.setAdapter(settleUpAdapter);
        payRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, payRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final SettleUpObject settleUpObject = paidByList.get(position);
                final Dialog dialog = new Dialog(SettleUp.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);

                balanceimage.setImageResource(R.drawable.settlemoneypayicon);
                TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                alerttext.setText("Pay "+settleUpObject.getAmount() +" to "+settleUpObject.getUserName() );
                Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                yesButton.setText("Accept");
                Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                noButton.setText("Reject");
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkInternetConnectivity.isNetConnected(SettleUp.this)){
                            SettleUpBackgroundWorker settleUpBackgroundWorker = new SettleUpBackgroundWorker(new StringInterface() {
                                @Override
                                public void StringOutput(String output) {
                                    System.out.println(output);
                                    final Dialog dialog2 = new Dialog(SettleUp.this);
                                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog2.setContentView(R.layout.dialog);
                                    ImageView balanceimage = (ImageView) dialog2.findViewById(R.id.imageViewalert);
                                    balanceimage.setImageResource(R.drawable.settlemoneypayicon);
                                    TextView alerttext2 = (TextView) dialog2.findViewById(R.id.textViewalertdialog);
                                    alerttext2.setText(output);
                                    Button yesButton2 = (Button) dialog2.findViewById(R.id.buttonYesAlertDialog);
                                    yesButton2.setText("OK");
                                    Button noButton = (Button) dialog2.findViewById(R.id.buttonNoAlertDialog);
                                    noButton.setText("Reject");
                                    noButton.setVisibility(View.GONE);
                                    yesButton2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog2.dismiss();
                                        }
                                    });
                                    dialog2.show();
                                }
                            } , SettleUp.this);
                            settleUpBackgroundWorker.execute("settleUpPayAmount" , mycontact ,settleUpObject.getUserMobileNo(), groupId , settleUpObject.getAmount(), date, time , groupName);
                        }else {
                            checkInternetConnectivity.alertDialog(SettleUp.this);
                        }
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        msettleUpAdapter = new SettleUpAdapter(paidToList, 1);
        mLayoutManager = new LinearLayoutManager(SettleUp.this);
        askRecyclerView.setLayoutManager(mLayoutManager);
        askRecyclerView.setItemAnimator(new DefaultItemAnimator());
        askRecyclerView.addItemDecoration(new DividerItemDecoration(SettleUp.this, LinearLayoutManager.VERTICAL));
        askRecyclerView.setAdapter(msettleUpAdapter);
        askRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, askRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final SettleUpObject settleUpObject = paidToList.get(position);
                final Dialog dialog = new Dialog(SettleUp.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);

                balanceimage.setImageResource(R.drawable.askmoney);
                TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                alerttext.setText("Request to give you money");
                Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                yesButton.setText("Send Request");
                Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                noButton.setText("Reject");
                noButton.setVisibility(View.GONE);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if(checkInternetConnectivity.isNetConnected(SettleUp.this)){
                            SettleUpBackgroundWorker settleUpBackgroundWorker = new SettleUpBackgroundWorker(new StringInterface() {
                                @Override
                                public void StringOutput(String output) {
                                    dialog.dismiss();
                                    System.out.println(output);
                                    final Dialog dialog = new Dialog(SettleUp.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.dialog);
                                    ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                                    balanceimage.setImageResource(R.drawable.settlemoneypayicon);
                                    TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                                    alerttext.setText(output );
                                    Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                                    yesButton.setText("OK");
                                    Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                                    noButton.setText("Reject");
                                    noButton.setVisibility(View.GONE);
                                    yesButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            } , SettleUp.this);
                            settleUpBackgroundWorker.execute("settleUpSendRequestToPay" , settleUpObject.getUserMobileNo(), mycontact , groupId , settleUpObject.getAmount(), date, time, groupName);
                        }else {
                            checkInternetConnectivity.alertDialog(SettleUp.this);
                        }
                    }
                });
                dialog.show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if(checkInternetConnectivity.isNetConnected(SettleUp.this)){
            SettleUpBackgroundWorker settleUpBackgroundWorker = new SettleUpBackgroundWorker(new StringInterface() {
                @Override
                public void StringOutput(String output) {
                    System.out.println(output);
                    handleResponse(output);
                    if(paidByList.isEmpty())
                        paidBytext.setVisibility(View.VISIBLE);
                    if(paidToList.isEmpty())
                        paidToText.setVisibility(View.VISIBLE);

                }
            } , this);
            settleUpBackgroundWorker.execute("settleUpList" , mycontact , groupId , arrayToJson(memberContactFromDb));
        }else {
            checkInternetConnectivity.alertDialog(SettleUp.this);
        }


    }

    public void handleResponse(String output){
        try {
            JSONArray jsonArray = new JSONArray(output);

            System.out.println(jsonArray.length());
            if (jsonArray.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    System.out.println(obj.get("GiverMobileNo") + ",");
                    System.out.println(obj.get("TakerMobileNo") + ",");
                    System.out.println(obj.get("amount") + ",");

                    if (obj.get("TakerMobileNo").equals(mycontact)) {
                            SettleUpObject settleUpObject = new SettleUpObject(contactName(obj.get("GiverMobileNo").toString()), obj.get("amount").toString(), R.drawable.settleup2 , obj.get("GiverMobileNo").toString());
                            paidToList.add(settleUpObject);
                    }
                    if (obj.get("GiverMobileNo").equals(mycontact)) {
                            SettleUpObject settleUpObject = new SettleUpObject(contactName(obj.get("TakerMobileNo").toString()), obj.get("amount").toString(), R.drawable.settleup2, contactName(obj.get("TakerMobileNo").toString()));
                            paidByList.add(settleUpObject);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        settleUpAdapter.notifyDataSetChanged();
        msettleUpAdapter.notifyDataSetChanged();
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

        return SJson;
    }

    public String contactName(String mobileNo){
        String name ="";
        name = contactDatabase.getContact(mobileNo);
        if(name.isEmpty())
           return mobileNo;
        else
        return name;
    }
}