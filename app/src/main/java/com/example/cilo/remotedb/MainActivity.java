package com.example.cilo.remotedb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText usernameET,
            emailET;
    Button submitBtn;
    TextView dataTV;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataTV = (TextView) findViewById(R.id.dataTV);

        startService(new Intent(this,
                MyService.class));

        usernameET = (EditText) findViewById(R.id.username);
        emailET = (EditText)findViewById(R.id.email);
        submitBtn = (Button) findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("cecil","--start--");
                new SendData(usernameET.getText().toString(),
                        emailET.getText().toString(),"http://10.0.2.2/remote/send.php").execute();

            }
        });

    }

     @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    dataTV.setText("");
                    dataTV.append(""+intent.getExtras().get("data"));
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("remote_data"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }
}
