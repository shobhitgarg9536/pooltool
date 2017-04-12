package com.shobhit.pooltool.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shobhit.pooltool.model.ContactObject;
import com.shobhit.pooltool.utils.DividerItemDecoration;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.utils.RecyclerTouchListener;
import com.shobhit.pooltool.adapter.ContactAdapter;
import com.shobhit.pooltool.database.ContactDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shobhit-pc on 1/4/2017.
 */

public class ContactInviteFriends extends AppCompatActivity{

    RecyclerView contactRecyclerView;
    ContactAdapter mcontactAdapter;
    List<ContactObject> inviteContactList;
    ArrayList<HashMap<String, String>> contact_from_database ;
    ContactDatabase contactDatabase;
    EditText etSearch;
    String text = "";
    Button btinvite;
    Toolbar tbinvite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invitecontacts);
        contact_from_database = new ArrayList<HashMap<String, String>>();
        contactDatabase = new ContactDatabase(ContactInviteFriends.this);
        contact_from_database  = contactDatabase.getAllContact();

        contactRecyclerView = (RecyclerView) findViewById(R.id.invite_contact_recycler_view);
        etSearch = (EditText) findViewById(R.id.edittextsearch2);
        tbinvite = (Toolbar) findViewById(R.id.toolbarinvitefriends);
        setSupportActionBar(tbinvite);
        getSupportActionBar().setTitle("Invite Friend's");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text = etSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                inviteContactList.clear();
                contact_List(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inviteContactList = new ArrayList<>();
        mcontactAdapter = new ContactAdapter(inviteContactList , 0);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ContactInviteFriends.this);
        mLayoutManager = new LinearLayoutManager(ContactInviteFriends.this);
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.addItemDecoration(new DividerItemDecoration(ContactInviteFriends.this, LinearLayoutManager.VERTICAL));
        contactRecyclerView.setAdapter(mcontactAdapter);

        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(ContactInviteFriends.this, contactRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactObject contactObjct = inviteContactList.get(position);
                Toast.makeText(ContactInviteFriends.this, contactObjct.getName() + " is selected!", Toast.LENGTH_SHORT).show();

                //write code to send invitation
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactObjct.getNumber()));
                intent.putExtra("sms_body", "Try pooltool");
                startActivity(intent);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        contact_List(text);

    }

    private void contact_List(String textSearch) {
        for(int i =0 ; i<contact_from_database.size();i++){
            if(contact_from_database.get(i).get("phoneName").toLowerCase().contains(textSearch) || textSearch.equals("")) {
                ContactObject contactObject = new ContactObject(contact_from_database.get(i).get("phoneName"), contact_from_database.get(i).get("phoneNumber") , R.drawable.contactimage);
                if (contact_from_database.get(i).get("invite").equals("1")) {
                    inviteContactList.add(contactObject);
                }
            }
        }


        mcontactAdapter.notifyDataSetChanged();
    }
}
