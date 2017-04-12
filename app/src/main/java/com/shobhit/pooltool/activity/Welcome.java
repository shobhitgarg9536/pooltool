package com.shobhit.pooltool.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.shobhit.pooltool.R;
import com.shobhit.pooltool.network.ContactAccessingBackgroundWorker;
import com.shobhit.pooltool.network.ContactBackgroundworker;
import com.shobhit.pooltool.utils.CheckInternetConnectivity;

/**
 * Created by Shobhit-pc on 1/15/2017.
 */

public class Welcome extends AppCompatActivity {

    Button welcome;
    CheckInternetConnectivity checkInternetConnectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        checkInternetConnectivity = new CheckInternetConnectivity();

        welcome = (Button) findViewById(R.id.buttonWelcome);
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDummyContactWrapper();

                Intent i = new Intent(Welcome.this , ViewPagerActivity.class);
                startActivity(i);
            }
        });
        insertDummyContactWrapper();
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(Welcome.this,
                android.Manifest.permission.READ_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(Welcome.this,
                    android.Manifest.permission.READ_CONTACTS)) {

                ActivityCompat.requestPermissions(Welcome.this,
                        new String[] {android.Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);


                return;
            }
            ActivityCompat.requestPermissions(Welcome.this,
                    new String[] {android.Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);

            return;
        }
        ContactAccessingBackgroundWorker contactAccessingBackgroundWorker = new ContactAccessingBackgroundWorker(Welcome.this);
        contactAccessingBackgroundWorker.execute();
        if(checkInternetConnectivity.isNetConnected(Welcome.this)) {
            ContactBackgroundworker contactBackgroundworker = new ContactBackgroundworker(Welcome.this);
            contactBackgroundworker.execute();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContactAccessingBackgroundWorker contactAccessingBackgroundWorker = new ContactAccessingBackgroundWorker(Welcome.this);
                contactAccessingBackgroundWorker.execute();
                if(checkInternetConnectivity.isNetConnected(Welcome.this)) {
                    ContactBackgroundworker contactBackgroundworker = new ContactBackgroundworker(Welcome.this);
                    contactBackgroundworker.execute();
                }
            }
        }
    }


}
