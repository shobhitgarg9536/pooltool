package com.shobhit.pooltool;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;


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

/**
 * Created by Shobhit-pc on 9/25/2016.
 */

public class MyBalance extends AsyncTask<String ,String ,String> {

    Context mcontext;
    ProgressDialog progressDialog;
    String data = "";

    public BalanceInterface delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("Requesting...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public MyBalance(BalanceInterface balanceInterface , Context ctx) {
        delegate = balanceInterface;//Assigning call back interfacethrough constructor
        mcontext = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        if(type == "myBalance") {
            String mobileNo = params[1];
            String groupId = params[2];
            try {
                URL url = new URL(UrlList.get_my_expenditure);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mobileNo", "UTF-8") + "=" + URLEncoder.encode(mobileNo, "UTF-8") + "&" +
                        URLEncoder.encode("groupId", "UTF-8") + "=" + URLEncoder.encode(groupId, "UTF-8");
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
        if(type == "myBalanceSheet"){
            String mobileNo = params[1];
            String groupId = params[2];
            try {
                URL url = new URL(UrlList.my_balance_sheet);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mobileNo", "UTF-8") + "=" + URLEncoder.encode(mobileNo, "UTF-8") + "&" +
                        URLEncoder.encode("groupId", "UTF-8") + "=" + URLEncoder.encode(groupId, "UTF-8");
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
    protected void onPostExecute(String response) {
       // float famount = Float.valueOf(response);
       // delegate.money(String.valueOf((int) famount));
        progressDialog.dismiss();
        delegate.money(response);


        super.onPostExecute(response);
    }


}
