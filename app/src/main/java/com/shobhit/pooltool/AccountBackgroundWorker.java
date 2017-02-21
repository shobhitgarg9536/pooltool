package com.shobhit.pooltool;

 import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
 import android.app.ProgressDialog;
 import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;


 import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.net.URLEncoder;

 import cz.msebera.android.httpclient.Header;

public class AccountBackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog.Builder alertDialog;
    ProgressDialog progressDialog;

    public StringInterface delegate = null;

    public AccountBackgroundWorker(StringInterface stringInterface , Context c){
        this.context = c;
        this.delegate = stringInterface;
    }

    AccountBackgroundWorker(Context c){
        context = c;
    }
    @Override
    protected String doInBackground(String... params) {
        //creating user account
        if(params[0] == "createAccount") {
            try {
                String mobileNo = params[1];
                String name = params[2];
                String countryCode = params[3];
                String email = params[4];
                String firebaseUid = params[5];
                URL url = new URL(UrlList.register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mobileNo", "UTF-8") + "=" + URLEncoder.encode(mobileNo, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("countryCode", "UTF-8") + "=" + URLEncoder.encode(countryCode, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("firebaseUid", "UTF-8") + "=" + URLEncoder.encode(firebaseUid, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(params[0] == "createGroup"){
            try {
                String create_group_list = params[1];
                URL url = new URL(UrlList.create_group);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("create_group_list", "UTF-8") + "=" + URLEncoder.encode(create_group_list, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registering");
        progressDialog.setCancelable(false);

        progressDialog.show();


    }

    @Override
    protected void onPostExecute(String result) {
            progressDialog.dismiss();
        delegate.StringOutput(result);
      /*  alertDialog.setTitle("Register");
        alertDialog.setMessage(result);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.setCancelable(false);
        alertDialog.create();
        alertDialog.show();*/
    }

    public void alertDialog( String title , String message){

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    public void startGroupActivity(){
        Intent i = new Intent(context , Groups.class);
        context.startActivity(i);
    }
}
