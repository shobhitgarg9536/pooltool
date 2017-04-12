package com.shobhit.pooltool.activity;

 import android.app.Dialog;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.support.annotation.Nullable;
 import android.support.constraint.ConstraintLayout;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.Toolbar;
 import android.view.View;
 import android.view.Window;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.ListAdapter;
 import android.widget.ListView;
 import android.widget.TextView;

 import com.shobhit.pooltool.database.MemberDB;
 import com.shobhit.pooltool.network.MyBalance;
 import com.shobhit.pooltool.R;
 import com.shobhit.pooltool.listeners.StringInterface;
 import com.shobhit.pooltool.database.DBController;
 import com.shobhit.pooltool.listeners.BalanceInterface;
 import com.shobhit.pooltool.network.GroupBackgroundWorker;
 import com.shobhit.pooltool.utils.CheckInternetConnectivity;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import java.util.ArrayList;
 import java.util.HashMap;

public class GroupDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] itemslist;

    String[] items;
    ListView groupDetails;
    Toolbar toolbargroupdetails;
    String groupId,mycontact,groupName;
    public static final String UserDetail = "UserDetail";
    public static final String MYCONTACT = "UserContact";
    CheckInternetConnectivity checkInternetConnectivity;
    HashMap<String , String> queryValues;
    ArrayList<HashMap<String , String>> groupMemberDetailsFromDB;
    MemberDB memberDB;
    DBController dbController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdetails);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.contraintlayoutgroupdetail);
        constraintLayout.getBackground().setAlpha(100);
        groupDetails = (ListView) findViewById(R.id.lvgroupDetail);

        checkInternetConnectivity = new CheckInternetConnectivity();
        memberDB = new MemberDB(this);
        dbController = new DBController(this);

        SharedPreferences shared = getSharedPreferences(UserDetail, MODE_PRIVATE);
        groupId = (shared.getString("groupid", ""));
        groupName = (shared.getString("groupName", ""));
        shared = getSharedPreferences(MYCONTACT, MODE_PRIVATE);
        mycontact = (shared.getString("UserContact", ""));

        toolbargroupdetails = (Toolbar) findViewById(R.id.tbgroupdetails);
        setSupportActionBar(toolbargroupdetails);
        getSupportActionBar().setTitle(groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        itemslist = getResources().getStringArray(R.array.itemslist);
        items = getResources().getStringArray(R.array.items);
        ListAdapter adapter = new ArrayAdapter(GroupDetails.this, R.layout.view_user_entry,R.id.userName, itemslist);
        groupDetails.setAdapter(adapter);

        if(checkInternetConnectivity.isNetConnected(GroupDetails.this)){
        GroupBackgroundWorker groupBackgroundWorker = new GroupBackgroundWorker(new StringInterface() {
            @Override
            public void StringOutput(String output) {

                if(output!=null) {
                    try {
                        groupMemberDetailsFromDB = dbController.getGroupDetails(groupId);
                        String groupCreator = groupMemberDetailsFromDB.get(0).get("mobileNo");
                        String date = groupMemberDetailsFromDB.get(0).get("date");
                        String time = groupMemberDetailsFromDB.get(0).get("time");

                        //to delete all memberList from member DB
                        memberDB.delete(groupId);

                        System.out.println(output);
                        // Extract JSON array from the response
                        JSONArray arr = new JSONArray(output);
                        // If no of array elements is not zero
                        if (arr.length() != 0) {

                            for (int i = 0; i < arr.length(); i++) {
                                // Get JSON object
                                JSONObject obj = (JSONObject) arr.get(i);
                                System.out.println(obj.get("groupMember"));
                                // DB QueryValues Object to insert into SQLite

                                if (!obj.get("groupMember").toString().equals(mycontact)) {
                                    queryValues = new HashMap<String, String>();
                                    // Add values extracted from Object
                                    queryValues.put("groupcreator", groupCreator);
                                    queryValues.put("member", obj.get("groupMember").toString());
                                    queryValues.put("groupId", groupId);
                                    queryValues.put("date", date);
                                    queryValues.put("time", time);
                                    queryValues.put("admin", obj.get("admin").toString());
                                    // Insert User into SQLite DB
                                    memberDB.insertUser(queryValues);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        groupBackgroundWorker.execute("groupMemberList", groupId);
    }

        groupDetails.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String classes = items[position];
        try {
            Class ourclass = Class.forName("com.shobhit.pooltool.activity."+classes);
            Intent i = new Intent(GroupDetails.this, ourclass);
            startActivity(i);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(position == 3){
            if(checkInternetConnectivity.isNetConnected(GroupDetails.this)) {
                MyBalance myBalance = new MyBalance(new BalanceInterface() {
                    @Override
                    public void money(String output) {

                        final Dialog dialog = new Dialog(GroupDetails.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog);
                        ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                        balanceimage.setImageResource(R.drawable.balance);
                        TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                        alerttext.setText("Your total balance is "+output);
                        Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                        yesButton.setText("OK");
                        Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                        noButton.setText("See Details");
                        if(output.equals("0")){
                            noButton.setVisibility(View.GONE);
                        }
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startbalanceActivity();
                            }
                        });
                        dialog.show();

                  /*
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(GroupDetails.this);
                        alertDialog.setTitle("Balance");
                        alertDialog.setMessage(output);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        if (!output.equals("0")) {
                            alertDialog.setNegativeButton("See Details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startbalanceActivity();

                                }
                            });
                        }
                        alertDialog.setCancelable(false);
                        alertDialog.create();
                        alertDialog.show();*/
                    }
                } , GroupDetails.this);

                myBalance.execute("myBalance", mycontact, groupId);
            }else
                checkInternetConnectivity.alertDialog(GroupDetails.this);
        }
    }

    private void startbalanceActivity() {
        Intent i = new Intent(this , MyBalanceSheet.class);
        startActivity(i);
    }
}