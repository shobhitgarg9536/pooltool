package com.shobhit.pooltool.network;

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
 * Created by Shobhit-pc on 1/8/2017.
 */

public class FirebaseTokenBackgroundWorker extends AsyncTask<String , String , String> {

    public StringInterface delecate  = null;

    public FirebaseTokenBackgroundWorker(StringInterface stringInterface){
        this.delecate = stringInterface;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String mobileNo = params[0];
            String firebaseUid = params[1];
            URL url = new URL(UrlList.update_firebase_uid);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("mobileNo", "UTF-8") + "=" + URLEncoder.encode(mobileNo, "UTF-8") + "&" +
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
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        delecate.StringOutput(s);
        super.onPostExecute(s);
    }
}
