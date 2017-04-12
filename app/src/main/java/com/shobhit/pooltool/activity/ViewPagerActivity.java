package com.shobhit.pooltool.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.fragment.Groups;
import com.shobhit.pooltool.fragment.Notifications;
import com.shobhit.pooltool.fragment.PooltoolContactsFragment;
import com.shobhit.pooltool.listeners.StringInterface;
import com.shobhit.pooltool.network.FirebaseTokenBackgroundWorker;
import com.shobhit.pooltool.utils.CheckInternetConnectivity;
import com.shobhit.pooltool.utils.Config;
import com.shobhit.pooltool.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.LocalBroadcastManager.getInstance;

public class ViewPagerActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String TAG = Groups.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static final String MyPREFERENCES = "UserContact";
    CheckInternetConnectivity checkInternetConnectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpageractivity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        checkInternetConnectivity = new CheckInternetConnectivity();



        if(checkInternetConnectivity.isNetConnected(ViewPagerActivity.this)) {

            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF, 0);
            String firebase_id_send_to_server_or_not = sharedPreferences.getString("FirebaseIdSendToServer", "");

            if (firebase_id_send_to_server_or_not.equals("0")) {
                String token = sharedPreferences.getString("regId", "");
                sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String UserContact = sharedPreferences.getString("UserContact", "");
                FirebaseTokenBackgroundWorker firebaseTokenBackgroundWorker = new FirebaseTokenBackgroundWorker(new StringInterface() {
                    @Override
                    public void StringOutput(String output) {
                        System.out.println(output);
                    }
                });
                firebaseTokenBackgroundWorker.execute(UserContact, token);
            }

        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // fcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);



                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Groups(), "GROUPS");
        adapter.addFragment(new PooltoolContactsFragment(), "CONTACTS");
        adapter.addFragment(new Notifications(), "NOTIFICATIONS");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>(); //for fragments
        private final List<String> mFragmentTitleList = new ArrayList<>(); //for fragment titles

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.groupmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.creategroup:
                Intent i = new Intent(this, CreateGroup.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}