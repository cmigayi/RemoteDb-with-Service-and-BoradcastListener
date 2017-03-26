package com.example.cilo.remotedb;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
 * Created by cilo on 3/16/17.
 */

public class RemoteDatabase {
    String url = "http://localhost/androidremotedb";




    //Async Task class
    public class PostData extends AsyncTask<Void,Void,String>{
        String username;
        String dUrl = url+"/senddata.php";

        public PostData(String username){
            this.username = username;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            HashMap<String, String> data =
                    new HashMap<String,String>();

            data.put("username",username);

            URL mainUrl;

            try {
                mainUrl = new URL(dUrl);

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
                    result = br.readLine();
                    Log.d("cecil",result);
                }
                else {
                    result="Error Posting";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
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


    public class GetData{

    }

}
