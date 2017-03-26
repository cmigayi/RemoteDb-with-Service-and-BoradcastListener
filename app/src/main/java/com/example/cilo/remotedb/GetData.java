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
 * Created by cilo on 3/17/17.
 */

public class GetData extends AsyncTask<Void,Void,String> {

    String url;
    String response = null;
    UrlCallback urlCallback;

    public GetData(String url, UrlCallback urlCallback){
        this.url = url;
        this.urlCallback = urlCallback;
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, String> data = new HashMap<String,String>();
        data.put("data","any");

        try{

            URL durl = new URL(url);
            Log.d("cecil",url);

            HttpURLConnection conn = (HttpURLConnection)
                    durl.openConnection();
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
            Log.d("cecil",""+responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=
                        new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
                Log.d("cecil",response);
            }
            else {
                response="Error Posting";
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        urlCallback.done(s);
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
