package com.shobhit.pooltool.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.shobhit.pooltool.listeners.StringInterface;
import com.shobhit.pooltool.utils.UrlList;

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
 * Created by Shobhit-pc on 1/7/2017.
 */

public class ExpenditureBackgroundWorker extends AsyncTask<String , String , String> {

    public StringInterface delecate = null;
    ProgressDialog progressDialog;
    public Context ctx;

    public ExpenditureBackgroundWorker(StringInterface stringInterface , Context context){
        this.delecate = stringInterface;
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Adding Expense...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {


            try {
                String jsonAddItem = params[0];
                String jsonAddExpense = params[1];
                URL url = new URL(UrlList.add_expense);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("jsonAddItem", "UTF-8") + "=" + URLEncoder.encode(jsonAddItem, "UTF-8") + "&" +
                        URLEncoder.encode("jsonAddExpense", "UTF-8") + "=" + URLEncoder.encode(jsonAddExpense, "UTF-8");
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

        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        progressDialog.dismiss();
        delecate.StringOutput(s);
        super.onPostExecute(s);
    }


}
