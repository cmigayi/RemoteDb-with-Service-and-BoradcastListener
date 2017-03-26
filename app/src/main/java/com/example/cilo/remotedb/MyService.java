package com.example.cilo.remotedb;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

public class MyService extends Service {


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                new GetData("http://10.0.2.2/remote/getdata.php", new UrlCallback() {
                    @Override
                    public void done(String result) {
                        Intent i = new Intent("remote_data");
                        i.putExtra("data",result);
                        sendBroadcast(i);
                    }
                }).execute();
                handler.postDelayed(this,2000);
            }
        },2000);

    }
}

