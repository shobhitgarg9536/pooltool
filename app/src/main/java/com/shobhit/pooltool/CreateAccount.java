package com.shobhit.pooltool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class CreateAccount extends AppCompatActivity{
    EditText name,email,mobile;
    CheckBox agreement;
    String sname,semail,countrycode="+91",smobile,firebaseUid;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "UserContact";
    Toolbar toolbarcreategroup;
    TextInputLayout textInputName,textInputMobile,textInputEmail;
    CheckInternetConnectivity checkInternetConnectivity;
    AlertDialog.Builder alertDialog;
    int flag=0;
    android.support.v7.widget.AppCompatAutoCompleteTextView autoCompleteTextViewCountryName;
    Spinner spinnerCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        name = (EditText) findViewById(R.id.etname);
        email = (EditText) findViewById(R.id.etemail);
        mobile = (EditText) findViewById(R.id.etmobile);
        agreement = (CheckBox) findViewById(R.id.cbterms);
        textInputName = (TextInputLayout)  findViewById(R.id.tilname);
        textInputMobile = (TextInputLayout)  findViewById(R.id.tilmobile);
        textInputEmail = (TextInputLayout)  findViewById(R.id.tilemail);
        toolbarcreategroup = (Toolbar)findViewById(R.id.tbcreateaccount);
        autoCompleteTextViewCountryName = (android.support.v7.widget.AppCompatAutoCompleteTextView) findViewById(R.id.autoCompleteTextViewcountryname);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnercurrency);
        setSupportActionBar(toolbarcreategroup);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        firebaseUid =  pref.getString("regId" , "");

        checkInternetConnectivity = new CheckInternetConnectivity();

        String[] countries = getResources().getStringArray(R.array.country_phone_number);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,countries);
        autoCompleteTextViewCountryName.setAdapter(adapter);

        String[] currency = getResources().getStringArray(R.array.currency);
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,currency);
        spinnerCurrency.setAdapter(adapterCurrency);

        name.addTextChangedListener(new MyTextWatcher(name));
        mobile.addTextChangedListener(new MyTextWatcher(mobile));
        email.addTextChangedListener(new MyTextWatcher(email));

    }
    public void CreateUserAccount(View view) {
        AssignTheValues();


        if (!agreement.isChecked()) {
            Toast.makeText(this, "Accept the terms and conditions", Toast.LENGTH_SHORT).show();
        }
        if ((validateName()) && validateMobile() && validateEmail() && (agreement.isChecked())) {



            final AlertDialog[] alertDialog = new AlertDialog[1];
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering");
            progressDialog.setCancelable(false);

            //    progressDialog.show();
            if(checkInternetConnectivity.isNetConnected(this)) {
                AccountBackgroundWorker accountBackgroundWorker = new AccountBackgroundWorker(new StringInterface() {
                    @Override
                    public void StringOutput(String output) {

                        if (output.equals("Registration Successfull")) {
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("UserName", sname);
                            editor.putString("UserContact", smobile);
                            editor.commit();
                            alertDialog(output);
                        } else if (output.equals("User already exist")) {
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("UserName", sname);
                            editor.putString("UserContact", smobile);
                            editor.commit();
                            alertDialog(output);
                        } else {
                            checkInternetConnectivity.alertDialog(CreateAccount.this);
                        }

                    }
                } , this);
                accountBackgroundWorker.execute("createAccount" , smobile, sname, countrycode, semail, firebaseUid);
            }else{
                checkInternetConnectivity.alertDialog(CreateAccount.this);
            }

      /*      AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params =  new RequestParams();
            params.put("mobileNo",smobile);
            params.put("name",sname);
            params.put("countryCode",countrycode);
            params.put("email",semail);
            params.put("firebaseUid",firebaseUid);

            asyncHttpClient.post(UrlList.register_url , params ,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    progressDialog.dismiss();
                    alertDialog[0] = new AlertDialog.Builder(CreateAccount.this).create();
                    alertDialog[0].setTitle("Register");
                    alertDialog[0].setMessage(responseString);
                    alertDialog[0].setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog[0].setCancelable(false);
                    alertDialog[0].show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
*/


        }
    }
    public void alertDialog(String message){
      /*  alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Register");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                insertDummyContactWrapper();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
*/
        final Dialog dialog = new Dialog(CreateAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
        if(message.equals("Registration Successfull") || message.equals("User already exist"))
            balanceimage.setImageResource(R.drawable.accountcreated);
        else
            balanceimage.setImageResource(R.drawable.interneterror);
        TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
        alerttext.setText(message);
        Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
        yesButton.setText("Ok");
        Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
        noButton.setText("See Details");
        noButton.setVisibility(View.GONE);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(CreateAccount.this , Welcome.class);
                startActivity(i);

            }
        });
        dialog.show();
    }

    private void startCreateAccount(){
        Intent i = new Intent(this, ViewPagerActivity.class);
        startActivity(i);
    }

    private void AssignTheValues() {
        sname = name.getText().toString();
        semail = email.getText().toString();
        smobile = mobile.getText().toString();
    }
    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            textInputName.setError("Enter your full name");
            //requsting focus on editText
            requestFocus(name);
            return false;
        } else {
            textInputName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateMobile() {
        int mobileNoLength =  mobile.getText().toString().length();
        if (mobile.getText().toString().trim().isEmpty() || mobileNoLength != 10) {
            if(mobile.getText().toString().trim().isEmpty())
                textInputMobile.setError("Enter your Mobile No.");
            if(mobileNoLength != 10 )
                textInputMobile.setError("Enter your correct Mobile No.");
            //requsting focus on editText
            requestFocus(mobile);
            return false;
        }
        else {
            textInputMobile.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateEmail() {
        if (email.getText().toString().trim().isEmpty() || !semail.contains("@") || !semail.contains(".com") ) {
            if(semail.trim().isEmpty())
                textInputEmail.setError("Enter your Email Id");
            if(!semail.contains("@") || !semail.contains(".com"))
                textInputEmail.setError("Enter your correct Email Id");
            //requsting focus on editText
            requestFocus(email);
            return false;
        }
        else {
            textInputEmail.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            //showing keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.etname:
                    textInputName.setErrorEnabled(false);
                    break;
                case R.id.etmobile:
                    textInputMobile.setErrorEnabled(false);
                    break;
                case R.id.etemail:
                    textInputEmail.setErrorEnabled(false);
                    break;
            }
        }
        public void afterTextChanged(Editable editable) {

        }
    }


}

