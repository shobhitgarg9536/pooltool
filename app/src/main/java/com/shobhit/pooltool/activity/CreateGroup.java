package com.shobhit.pooltool.activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shobhit.pooltool.R;

public class CreateGroup extends AppCompatActivity implements View.OnClickListener {
    EditText groupname;
    Button addmember;
    TextInputLayout textInputGroupName;
    SharedPreferences sharedpreferences;
    Toolbar toolbargroup;
    public static final String MyPREFERENCES = "Creategroupname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup);
        groupname = (EditText) findViewById(R.id.etgroupname);
        addmember = (Button) findViewById(R.id.baddmember);
        textInputGroupName = (TextInputLayout) findViewById(R.id.tilgroupname);
        toolbargroup = (Toolbar) findViewById(R.id.tbcreategroup);

        setSupportActionBar(toolbargroup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addmember.setOnClickListener(this);

        groupname.addTextChangedListener(new MyTextWatcher(groupname));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.baddmember:
                if (validateGroupName()) {
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("GroupName", groupname.getText().toString());
                    editor.commit();
                    insertDummyContactWrapper();

                } else {
                    Toast.makeText(this, "Enter Group Name", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean validateGroupName() {
        if (groupname.getText().toString().trim().isEmpty()) {
            textInputGroupName.setError("Enter your Group Name");
            //requsting focus on editText
            requestFocus(groupname);
            return false;
        } else {
            textInputGroupName.setErrorEnabled(false);
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

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.etgroupname:
                    textInputGroupName.setErrorEnabled(false);
                    break;
            }
        }
        @Override
        public void afterTextChanged (Editable editable){

        }
    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private void insertDummyContactWrapper() {
        int hasReadContactsPermission = ContextCompat.checkSelfPermission(CreateGroup.this,
                Manifest.permission.READ_CONTACTS);
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(CreateGroup.this,
                    Manifest.permission.READ_CONTACTS)) {
                showMessageOKCancel("You need to allow access to Contacts",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CreateGroup.this,
                                        new String[] {Manifest.permission.READ_CONTACTS},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(CreateGroup.this,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);

            return;

        }
        Intent i = new Intent(this, MultipleContactSelect.class);
        startActivity(i);
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CreateGroup.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_ASK_PERMISSIONS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(this, MultipleContactSelect.class);
                startActivity(i);
            }
        }
    }
}

