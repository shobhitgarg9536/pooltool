package com.shobhit.pooltool.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.shobhit.pooltool.R;


public class MainActivity extends Activity {
    public static final String MyPREFERENCES = "UserContact";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String userContact = (shared.getString("UserContact", ""));
        if (!userContact.isEmpty()) {

            Intent i = new Intent(this, ViewPagerActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, CreateAccount.class);
            startActivity(i);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}