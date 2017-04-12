package com.shobhit.pooltool.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.shobhit.pooltool.adapter.ManageGroupAdapter;
import com.shobhit.pooltool.network.ManageGroupBackgroundWorker;
import com.shobhit.pooltool.model.ManageGroupObject;
import com.shobhit.pooltool.database.MemberDB;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.utils.RecyclerTouchListener;
import com.shobhit.pooltool.listeners.StringInterface;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.database.DBController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shobhit-pc on 12/25/2016.
 */

public class ManageGroup extends AppCompatActivity {
    String groupId, groupname, mycontact, admin, date, time;
    TextView groupName, createdOn, createdBy;
    Toolbar toolbarManage;
    RecyclerView recyclerView;
    ManageGroupAdapter manageGroupAdapter;
    MemberDB memberDB;
    ContactDatabase contactDb;
    DBController dbController;
    public static final String UserDetail = "UserDetail";
    public static final String UserContact = "UserContact";
    List<ManageGroupObject> groupMemberList;
    ArrayList<HashMap<String, String>> userList;
    int requestCode =10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managegroup);
        groupName = (TextView) findViewById(R.id.textViewManageGroupName);
        createdOn = (TextView) findViewById(R.id.textViewManageGroupCreateGroupOn);
        createdBy = (TextView) findViewById(R.id.textViewManageGroupCreateGroupBy);
        toolbarManage = (Toolbar) findViewById(R.id.tbmanageGroup);
        recyclerView  = (RecyclerView) findViewById(R.id.manage_group_recycler_view);
        SharedPreferences shared = getSharedPreferences(UserDetail, MODE_PRIVATE);
        groupId = (shared.getString("groupid", ""));
        groupname = (shared.getString("groupName", ""));
        shared = getSharedPreferences(UserContact, MODE_PRIVATE);
        mycontact = (shared.getString("UserContact", ""));

        setSupportActionBar(toolbarManage);
        getSupportActionBar().setTitle(groupname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userList = new ArrayList<>();
        groupMemberList = new ArrayList<>();
        memberDB = new MemberDB(this);
        contactDb = new ContactDatabase(this);
        dbController = new DBController(this);

        userList = memberDB.getAllMember(groupId);
        ArrayList<HashMap<String, String>> groupdetail = dbController.getGroupDetails(groupId);

        System.out.println(userList.toString());
        System.out.println(groupdetail.toString());
        String SgroupName = groupdetail.get(0).get("groupName").toString();
        admin = groupdetail.get(0).get("admin").toString();
        String Stime = groupdetail.get(0).get("time").toString();

        createdOn.setText(Stime);
        groupName.setText(groupname);
        createdBy.setText(getGroupCreatorName(groupdetail.get(0).get("mobileNo").toString()));

        manageGroupAdapter = new ManageGroupAdapter(groupMemberList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManageGroup.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(ManageGroup.this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(manageGroupAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ManageGroup.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                final ManageGroupObject manageGroupObject = groupMemberList.get(position);

                if (position == 0 && admin.equals("1")) {


                    Intent i = new Intent(ManageGroup.this , AddMember.class);
                    startActivityForResult(i , requestCode);
                    manageGroupAdapter.notifyDataSetChanged();
                }else{
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    date = df.format(Calendar.getInstance().getTime());
                    df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                    time = df.format(Calendar.getInstance().getTime());

                final Dialog dialog = new Dialog(ManageGroup.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.menudialog);
                Button makeACall = (Button) dialog.findViewById(R.id.buttonmenumakeacall);
                Button makeAdmin = (Button) dialog.findViewById(R.id.buttonmenumakeadmin);
                final String memberAdminState = memberDB.getMemberAdminState(manageGroupObject.getMobileNo());
                if (memberAdminState.equals("1"))
                    makeAdmin.setText("Remove Admin");
                if (admin.equals("0")) {
                    makeAdmin.setVisibility(View.GONE);
                }

                makeACall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", manageGroupObject.getMobileNo(), null)));
                    }
                });
                makeAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String adminState = "";
                        if (memberAdminState.equals(1)) {
                            adminState = "removeAdmin";
                        } else {
                            adminState = "makeAdmin";
                        }
                        ManageGroupBackgroundWorker manageGroupBackgroundWorker = new ManageGroupBackgroundWorker(new StringInterface() {
                            @Override
                            public void StringOutput(String output) {

                                if(output.equals("Successfull")){
                                    dbController.Update(groupId , time);
                                }

                                final Dialog dialog = new Dialog(ManageGroup.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog);
                                ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                                if(output.equals("Successfull"))
                                    balanceimage.setImageResource(R.drawable.accountcreated);
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
                                            dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }, ManageGroup.this);
                        manageGroupBackgroundWorker.execute(adminState, manageGroupObject.getMobileNo(), groupId , groupname, date, time);
                    }
                } );
                dialog.show();
            }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        group_member_list();

    }

    private void group_member_list() {
        if(admin.equals("1")) {
            ManageGroupObject groupObj = new ManageGroupObject("2" , "ADD MEMBERS+", 0 ,"");
            groupMemberList.add(groupObj);
        }
        for(int i=0; i < userList.size() ;i++){
            String memberAdminState = memberDB.getMemberAdminState(userList.get(i).get("member"));
            System.out.println(memberAdminState);
            ManageGroupObject groupObj1 = new ManageGroupObject(memberAdminState , getContactName(userList.get(i).get("member")).toString() , R.drawable.contactimage , userList.get(i).get("member"));
        groupMemberList.add(groupObj1);
        }
        manageGroupAdapter.notifyDataSetChanged();
    }

    public String getContactName(String contact){
        String name;
        name = contactDb.getContact(contact);
        if(name.isEmpty())
            return contact;
        else
            return name;
    }

    public String getGroupCreatorName(String contact){
        String name;
        name = contactDb.getContact(contact);
        if(name.isEmpty()){
            if(contact.equals(mycontact))
                return "YOU";
            else
                return contact;
        }
        else
            return name;
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageGroupAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manageGroupAdapter.notifyDataSetChanged();
    }

}