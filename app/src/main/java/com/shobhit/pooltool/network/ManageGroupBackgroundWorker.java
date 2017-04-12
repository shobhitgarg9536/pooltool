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
 * Created by Shobhit-pc on 1/15/2017.
 */

public class ManageGroupBackgroundWorker extends AsyncTask<String, String, String> {

    public StringInterface delecate = null;
    Context ctx;
    ProgressDialog progressDialog;

    public ManageGroupBackgroundWorker(StringInterface stringInterface , Context context){
        this.delecate = stringInterface;
        this.ctx = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Requesting...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String adminState = params[0];
            String memberNo = params[1];
            String groupId = params[2];
            String groupName = params[3];
            String date = params[4];
            String time = params[5];
            URL url = new URL(UrlList.manage_group_admin_state);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("adminState", "UTF-8") + "=" + URLEncoder.encode(adminState, "UTF-8") + "&" +
                    URLEncoder.encode("memberNo", "UTF-8") + "=" + URLEncoder.encode(memberNo, "UTF-8") + "&" +
                    URLEncoder.encode("groupId", "UTF-8") + "=" + URLEncoder.encode(groupId, "UTF-8") + "&" +
                    URLEncoder.encode("groupName", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8") + "&" +
                    URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                    URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        delecate.StringOutput(s);
    }
}
