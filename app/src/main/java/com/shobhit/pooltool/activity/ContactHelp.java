package com.shobhit.pooltool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shobhit.pooltool.R;

/**
 * Created by Shobhit-pc on 1/7/2017.
 */

public class ContactHelp extends AppCompatActivity{

    Toolbar tbContactHelp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacthelp);

        tbContactHelp =(Toolbar) findViewById(R.id.tbcontactHelp);
        setSupportActionBar(tbContactHelp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
