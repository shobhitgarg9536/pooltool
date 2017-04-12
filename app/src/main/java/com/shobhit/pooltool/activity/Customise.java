package com.shobhit.pooltool.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shobhit.pooltool.utils.GenerateUniqueString;
import com.shobhit.pooltool.database.MemberDB;
import com.shobhit.pooltool.R;
import com.shobhit.pooltool.database.ContactDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Shobhit-pc on 12/25/2016.
 */
public class Customise extends AppCompatActivity {
    private List<EditText> editTextList = new ArrayList<EditText>();
    public static final String UserDetail = "UserDetail";
    public static final String UserContact = "UserContact";
    Toolbar toolbarcustomise;
  //  public static final String MYUID = "UID";
    MemberDB controller;
    ContactDatabase contactDatabase;
    ArrayList<HashMap<String, String>> userList;
    ArrayList<HashMap<String, String>> moneylist;
    String money , groupid ,userContact , date ,time;
    GenerateUniqueString generateUniqueString;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customise);
        toolbarcustomise = (Toolbar) findViewById(R.id.tbcustomise);

        setSupportActionBar(toolbarcustomise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        generateUniqueString = new GenerateUniqueString();
        controller = new MemberDB(this);
        contactDatabase = new ContactDatabase(this);

        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llcustomise);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        // linearLayout.setLayoutParams(params);
        //linearLayout.setOrientation(VERTICAL);

        SharedPreferences shared = getSharedPreferences(UserDetail, MODE_PRIVATE);
        groupid = (shared.getString("groupid", ""));
       // shared = getSharedPreferences(MYUID, MODE_PRIVATE);
       // Uid = (shared.getString("UID", ""));
        shared = getSharedPreferences(UserContact, MODE_PRIVATE);
         userContact = (shared.getString("UserContact", ""));

            userList = controller.getAllMember(groupid);

            int count = userList.size();
            System.out.println(userList.toString());
            System.out.println(count);

            if(count == 0){

                final Dialog dialog = new Dialog(Customise.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                balanceimage.setImageResource(R.drawable.error);
                TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                alerttext.setText("Check your Internet Connection and Try again!");
                Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                yesButton.setText("OK");
                Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
                noButton.setText("See Details");
                noButton.setVisibility(View.GONE);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                dialog.show();

                /*
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Customise.this);
                alertDialog.setTitle("Network Error");
                alertDialog.setMessage("Check your Internet Connection and Try again!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog.create();
                alertDialog.show();*/
            }
            linearLayout.addView(tableLayout(count));
            linearLayout.addView(submitButton());

    }

    private Button submitButton() {
        Button button = new Button(this);
        button.setHeight(WRAP_CONTENT);
        button.setText("Submit");
        button.setBackgroundResource(R.drawable.curve_shape);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(submitListener);
        return button;
    }

    // Access the value of the EditText

   /* private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View view) {
               int i=0;
            moneylist = new ArrayList<HashMap<String, String>>();
            DateFormat df = new SimpleDateFormat("yyMMdd");
            date = df.format(Calendar.getInstance().getTime());
            df = new SimpleDateFormat("h:mm a");
            time = df.format(Calendar.getInstance().getTime());

                for (EditText editText : editTextList) {
                    HashMap<String, String> moneymap = new HashMap<String, String>();
                    moneymap.put("Uid", Uid);
                    moneymap.put("toMobileNo", userList.get(i).get("member"));
                    moneymap.put("amount", editText.getText().toString());
                    moneymap.put("groupId", groupid);
                    moneymap.put("date", date);
                    moneymap.put("time", time);
                    moneylist.add(moneymap);
                    i++;
                }

            //Coverting Arraylist to JsonArray
            List<JSONObject> jsonList = new ArrayList<JSONObject>();

                for (HashMap<String, String> data : moneylist) {

                    JSONObject jsonObject = new JSONObject(data);
                    jsonList.add(jsonObject);
                }

                JSONArray jsonMoneyList = new JSONArray(jsonList);

                String SJsonMoneyList = jsonMoneyList.toString();

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

                RequestParams params = new RequestParams();

                params.put("moneyList", SJsonMoneyList);

                asyncHttpClient.post("http://10.0.2.2/ptexpenditure1.php",params , new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String response) {
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Customise.this);
                        alertDialog.setTitle("Status");
                        alertDialog.setMessage(response);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        alertDialog.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        super.onSuccess(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        super.onFailure(statusCode, error, content);
                    }
                });

        }
    };
*/
    // Using a TableLayout as it provides you with a neat ordering structure

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i=0,totalMoney = 0;
            moneylist = new ArrayList<HashMap<String, String>>();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(Calendar.getInstance().getTime());
            df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            time = df.format(Calendar.getInstance().getTime());
            String itemId =  groupid+generateUniqueString.getUniqueString();
            for (EditText editText : editTextList) {
                if(!editText.getText().toString().isEmpty() && !editText.getText().toString().equals("0")) {
                    HashMap<String, String> moneymap = new HashMap<String, String>();
                    moneymap.put("fromMobileNo" , userContact);
                    moneymap.put("itemId", itemId);
                    moneymap.put("toMobileNo", userList.get(i).get("member"));
                    moneymap.put("amount", editText.getText().toString());
                    moneymap.put("groupId", groupid);
                    moneymap.put("date", date);
                    moneymap.put("time", time);
                    moneylist.add(moneymap);
                    totalMoney+=Integer.valueOf(editText.getText().toString());
                }
                i++;
            }
            if(totalMoney <= Integer.valueOf(money.toString())) {
                Intent intent = new Intent();

                Bundle bundle = new Bundle();
                bundle.putSerializable("money", moneylist);
                bundle.putInt("totalMoney" , totalMoney);
                bundle.putString("itemId" , itemId);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }else {

                final Dialog dialog = new Dialog(Customise.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
                    balanceimage.setImageResource(R.drawable.error);
                TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
                alerttext.setText("Exceeds money spend");
                Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
                yesButton.setText("OK");
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
/*
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Customise.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Exceeds money spend");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.create();
                alertDialog.show();*/
            }
        }
    };
    private TableLayout tableLayout(int count) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        int noOfRows = count / 1;
        for (int i = 0; i < noOfRows; i++) {
            String membername = userList.get(i).get("member");
            tableLayout.addView(createOneFullRow(i , membername));
        }
       /* int individualCells = count % 5;
        for(int i=0;i<individualCells;i++)
            tableLayout.addView(createLeftOverCells(individualCells, count));*/
        return tableLayout;
    }

   /* private TableRow createLeftOverCells(int individualCells, int count) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        int rowId = count - individualCells;

        tableRow.addView(editText(String.valueOf(rowId )));

        return tableRow;
    }*/

    private TableRow createOneFullRow(int rowId, String membername ) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 5, 0, 0);

        android.support.design.widget.TextInputLayout textInputLayout = new android.support.design.widget.TextInputLayout(this);
        textInputLayout.addView(editText(String.valueOf(rowId) ,membername));
        tableRow.addView(textInputLayout);
        tableRow.addView(textView(String.valueOf(rowId)));
        return tableRow;
    }

    private EditText editText(String hint, String membername) {

        EditText editText = new EditText(this);
        editText.setId(Integer.valueOf(hint));
        editText.setHint(getContactName(membername));
        editTextList.add(editText);

        return editText;
    }

    private TextView textView(String hint){
        TextView textView = new TextView(this);
        textView.setId(Integer.valueOf(hint));
        textView.setText(money);
        return textView;
    }
    public String getContactName(String contact){
        String name;
        name = contactDatabase.getContact(contact);
        if(name.isEmpty())
            return contact;
        else
            return name;
    }
}