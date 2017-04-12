package com.shobhit.pooltool.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shobhit.pooltool.utils.DividerItemDecoration;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.utils.RecyclerTouchListener;
import com.shobhit.pooltool.activity.GroupDetails;
import com.shobhit.pooltool.adapter.NotificationAdapter;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.database.DBController;
import com.shobhit.pooltool.database.NotificationDatabase;
import com.shobhit.pooltool.model.NotificationObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shobhit-pc on 12/21/2016.
 */

public class Notifications extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationObject> notificationList;
    NotificationDatabase notificationDatabase;
    ArrayList<HashMap<String, String>> notificationListByDatabase;
    String notificationMessage , addedMobileNo , addedphoneName , mycontact, groupId ;
    ContactDatabase contactDatabase;
    public static final String MYCONTACT = "UserContact";
    int notificationImage;
    TextView noNotification;
    DBController dbController;
    SharedPreferences sharedpreferences;
    public static final String UserDetails = "UserDetail";

    public Notifications(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationDatabase = new NotificationDatabase(getContext());
        contactDatabase = new ContactDatabase(getContext());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        android.support.constraint.ConstraintLayout linearLayout = (android.support.constraint.ConstraintLayout) inflater.inflate(R.layout.notification, container, false);

        dbController = new DBController(getContext());

        notificationList = new ArrayList<>();
        notificationListByDatabase = new ArrayList<>();

        notificationListByDatabase = notificationDatabase.getAllNotification();
       SharedPreferences shared = getContext().getSharedPreferences(MYCONTACT, Context.MODE_PRIVATE);
        mycontact = (shared.getString("UserContact", ""));

        noNotification = (TextView) linearLayout.findViewById(R.id.textViewNoNotification);

        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.notification_recycler_view);
        notificationAdapter = new NotificationAdapter(notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NotificationObject notificationObject = notificationList.get(position);
                String groupID = notificationObject.getGroupId();
                String groupName = dbController.getGroupName(groupID);
                sharedpreferences = getActivity().getSharedPreferences(UserDetails, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("groupid", groupID);
                editor.putString("groupName", groupName);
                editor.commit();
                Intent i = new Intent(getContext() , GroupDetails.class);
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        notification_list();

        if(notificationList.size() == 0){
            noNotification.setVisibility(View.VISIBLE);
        }
        return linearLayout;
    }

    private void notification_list() {
        for(int i =0 ; i<notificationListByDatabase.size() ; i++){
            groupId = notificationListByDatabase.get(i).get("groupId");
            addedMobileNo = notificationListByDatabase.get(i).get("addedMobileNo");
            System.out.println(addedMobileNo);
            if(addedMobileNo !=null) {
                if (addedMobileNo.equals(mycontact))
                    addedphoneName = "You";
                else
                    addedphoneName = contactDatabase.getContact(addedMobileNo);

                //if itemId is empty means notification is of group
                if (notificationListByDatabase.get(i).get("amountGiverType").equals("newGroup")) {
                    System.out.println(notificationListByDatabase.get(i).get("groupName"));
                    notificationImage = R.drawable.notificationgroup;
                    if (addedMobileNo.equals(mycontact))
                        notificationMessage = "You created the group "+notificationListByDatabase.get(i).get("groupName");
                    else if (addedphoneName.equals(""))
                        notificationMessage = addedMobileNo + " created the group " +notificationListByDatabase.get(i).get("groupName");
                    else
                        notificationMessage = addedphoneName + " created the group "+ notificationListByDatabase.get(i).get("groupName");
                }//notification is of item

                else if(notificationListByDatabase.get(i).get("amountGiverType").equals("newItem")){
                    notificationImage = R.drawable.notificationitem;
                    if (addedphoneName.equals(""))
                        notificationMessage = addedMobileNo + " added " + notificationListByDatabase.get(i).get("item") + " in " + notificationListByDatabase.get(i).get("groupName");
                    else
                        notificationMessage = addedphoneName + " added " + notificationListByDatabase.get(i).get("item") + " in " + notificationListByDatabase.get(i).get("groupName");

                }

                else {
                    if(notificationListByDatabase.get(i).get("amountGiverType").equals("settleUpPay")){
                        notificationImage = R.drawable.settleupnotification;
                        notificationMessage = addedphoneName +" has pay his money to you of group "+notificationListByDatabase.get(i).get("groupName");
                    }
                    else if(notificationListByDatabase.get(i).get("amountGiverType").equals("settleUpPayRequest")){
                        notificationImage = R.drawable.settleupnotification;
                        notificationMessage = addedphoneName +" has requested to pay his money of group "+notificationListByDatabase.get(i).get("groupName");
                    }
                    else if(notificationListByDatabase.get(i).get("amountGiverType").equals("adminState")){
                        notificationImage = R.drawable.admin;
                        if(notificationListByDatabase.get(i).get("amount").equals("0")){
                            notificationMessage = "You are removed from admin of group "+notificationListByDatabase.get(i).get("groupName");
                        }else{
                            notificationMessage = "You are now the admin of group "+notificationListByDatabase.get(i).get("groupName");
                        }
                    }
                    else {}
                }


                NotificationObject notificationOb = new NotificationObject(notificationMessage, notificationListByDatabase.get(i).get("time"), notificationImage , groupId);
                notificationList.add(notificationOb);
            }
        }
        System.out.println(notificationList.toString());
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notificationmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearNotification:
                notificationDatabase.delete();
                notificationList.clear();
                notificationAdapter.notifyDataSetChanged();
                noNotification.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
