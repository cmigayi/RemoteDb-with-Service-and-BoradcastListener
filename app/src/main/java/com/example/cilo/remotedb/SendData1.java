package com.example.cilo.remotedb;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cilo on 3/20/17.
 */











/*
    1. Create an hotspot on your phone.
    2. Connect your computer to your phone.
    3. Go to your computer CMD, type "ipconfig"
    4. Get the computer IP generated on the CMD
    5. Type the IP on your phone's browser

192.168.43.12/androidremotedb/senddata.php

*/

































public class SendData1 extends AsyncTask<Void,Void,String> {
    String username;
    String url;
    String response;

    public SendData1(String username, String url){
        this.username = username;
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {

        HashMap<String, String> data =
                new HashMap<String,String>();

        data.put("username",username);

        URL mainUrl;

        try {
            mainUrl = new URL(url);

            HttpURLConnection conn =
                    (HttpURLConnection) mainUrl.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(data));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
                Log.d("cecil",response);
            }
            else {
                response="Error Posting";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private String getPostDataString(HashMap<String, String> params)
            throws UnsupportedEncodingException {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.
                    encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.
                    encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
