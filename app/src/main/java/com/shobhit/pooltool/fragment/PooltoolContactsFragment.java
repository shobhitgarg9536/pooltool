package com.shobhit.pooltool.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shobhit.pooltool.utils.DividerItemDecoration;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.utils.RecyclerTouchListener;
import com.shobhit.pooltool.activity.ContactHelp;
import com.shobhit.pooltool.activity.ContactInviteFriends;
import com.shobhit.pooltool.adapter.ContactAdapter;
import com.shobhit.pooltool.database.ContactDatabase;
import com.shobhit.pooltool.model.ContactObject;
import com.shobhit.pooltool.network.ContactAccessingBackgroundWorker;
import com.shobhit.pooltool.network.ContactBackgroundworker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shobhit-pc on 11/13/2016.
 */
public class PooltoolContactsFragment extends Fragment {

    RecyclerView contactRecyclerView;
    ContactAdapter contactAdapter,mcontactAdapter;
    List<ContactObject> contactList;
    List<ContactObject> inviteContactList;
    ArrayList<HashMap<String, String>> contact_from_database ;
    ContactDatabase contactDatabase;
    EditText etSearch;
    String text = "";
  //  Button buttoninvitefriends;
    TextView tvinactivefriends;
    ProgressDialog progressDialog;

    public PooltoolContactsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        android.support.constraint.ConstraintLayout linearLayout = (android.support.constraint.ConstraintLayout) inflater.inflate(R.layout.contacts, container, false);

        contact_from_database = new ArrayList<HashMap<String, String>>();
        contactDatabase = new ContactDatabase(getContext());
        contact_from_database  = contactDatabase.getAllContact();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating Contact List");

        contactList = new ArrayList<>();
        contactRecyclerView = (RecyclerView) linearLayout.findViewById(R.id.contact_recycler_view);
        etSearch = (EditText) linearLayout.findViewById(R.id.edittextsearch);
      //  buttoninvitefriends = (Button) linearLayout.findViewById(R.id.buttoninvtefriends);
        tvinactivefriends = (TextView) linearLayout.findViewById(R.id.textviewinactivefriend);
      /*  buttoninvitefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext() , ContactInviteFriends.class);
                startActivity(i);
            }
        });*/
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text = etSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                contactList.clear();
             //   inviteContactList.clear();
                contact_List(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactAdapter = new ContactAdapter(contactList , 1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), contactRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactObject contactObjct = contactList.get(position);
                Toast.makeText(getContext(), contactObjct.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                if (contactObjct.getName().equals("Invite friend's")) {
                    //write code to send invitation
                    final Intent intent = new Intent(getContext(), ContactInviteFriends.class);
                    getContext().startActivity(intent);
                }
                if (contactObjct.getName().equals("Contact help")) {

                    //write code to send invitation
                    final Intent intent = new Intent(getContext(), ContactHelp.class);
                    getContext().startActivity(intent);
                }
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

/*
        inviteContactList = new ArrayList<>();
        contactRecyclerView = (RecyclerView) linearLayout.findViewById(R.id.invite_contact_recycler_view);
        mcontactAdapter = new ContactAdapter(inviteContactList , 0);
        mLayoutManager = new LinearLayoutManager(getContext());
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        contactRecyclerView.setAdapter(mcontactAdapter);

        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), contactRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactObject contactObjct = inviteContactList.get(position);
                Toast.makeText(getContext(), contactObjct.getName() + " is selected!", Toast.LENGTH_SHORT).show();

                //write code to send invitation
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactObjct.getNumber()));
                intent.putExtra("sms_body", "Try pooltool");
                getContext().startActivity(intent);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

*/
        contact_List(text);

        if(contactList.size() == 2)
            tvinactivefriends.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.GONE);

        return linearLayout;
    }

    private void contact_List(String textSearch) {
        for(int i =0 ; i<contact_from_database.size();i++){
            if(contact_from_database.get(i).get("phoneName").toLowerCase().contains(textSearch) || textSearch.equals("")) {
                ContactObject contactObject = new ContactObject(contact_from_database.get(i).get("phoneName"), contact_from_database.get(i).get("phoneNumber") , R.drawable.contactimage);
                if (contact_from_database.get(i).get("invite").equals("0")) {
                    contactList.add(contactObject);
                }
                /*else {
                    inviteContactList.add(contactObject);
                }*/
            }
        }
        ContactObject contactObject = new ContactObject("Invite friend's" , "" , android.R.drawable.ic_menu_share);
        contactList.add(contactObject);
        contactObject = new ContactObject("Contact help" , "" , android.R.drawable.ic_menu_help);
        contactList.add(contactObject);

        contactAdapter.notifyDataSetChanged();
     //   mcontactAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pooltoolcontactfragmentmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshContacts:/*
                progressDialog.setCancelable(false);
                progressDialog.show();
                Intent i = new Intent(getContext(), ContactService.class);
                getContext().startService(i);
                contactList.clear();
                contactAdapter = new ContactAdapter(contactList , 1);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                contactRecyclerView.setLayoutManager(mLayoutManager);
                contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
                contactRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                contactRecyclerView.setAdapter(contactAdapter);
                contact_List(text);
                progressDialog.dismiss();*/

                ContactAccessingBackgroundWorker contactAccessingBackgroundWorker = new ContactAccessingBackgroundWorker(getContext());
                contactAccessingBackgroundWorker.execute();
                ContactBackgroundworker contactBackgroundworker =new ContactBackgroundworker(getContext());
                contactBackgroundworker.execute();
                contactAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
