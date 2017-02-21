package com.shobhit.pooltool;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import static android.content.Context.MODE_PRIVATE;


public class Groups extends Fragment {

    // DB Class to perform DB related operations
    DBController controller;
    TextView nogroup;
    SharedPreferences sharedpreferences;
    public static final String UserDetails = "UserDetail";
    List<Group> groupList;

     GroupAdapter getGroupAdapter;
    private RecyclerView recyclerView;

    ArrayList<HashMap<String, String>> userList;

    public Groups(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new DBController(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        android.support.constraint.ConstraintLayout linearLayout = (android.support.constraint.ConstraintLayout)  inflater.inflate(R.layout.groups, container, false);

        nogroup = (TextView) linearLayout.findViewById(R.id.textviewnogroup);

        userList= new ArrayList<HashMap<String, String>>();


            userList = controller.getAllUsers();
            groupList = new ArrayList<>();


            recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recycler_view);


            getGroupAdapter = new GroupAdapter(groupList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(getGroupAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Group group = groupList.get(position);
                    Toast.makeText(getContext(),group.getgroupName()+" and "+ group.getGroupId() + " is selected!", Toast.LENGTH_SHORT).show()
                    ;
                    sharedpreferences = getActivity().getSharedPreferences(UserDetails, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("groupid", group.getGroupId());
                    editor.putString("groupName", group.getgroupName());
                    editor.commit();

                    Intent intent = new Intent(getActivity(), GroupDetails.class);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));



        group_list();


        if (groupList.size() == 0) {
            nogroup.setVisibility(View.VISIBLE);
            nogroup.setText("No Group is  Active. Tap 'Let's start Pool' to start your PoolTool");
        }
        return linearLayout;
    }

    public void group_list(){
        for(int i =0 ; i<userList.size();i++){
            Group group = new Group(userList.get(i).get("groupName") , userList.get(i).get("groupId") , R.drawable.groupicon , userList.get(i).get("date") );
            groupList.add(group);
        }
        getGroupAdapter.notifyDataSetChanged();
    }

}
