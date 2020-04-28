package com.example.services_test;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {
    private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {
        super("MyWorkerThread");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate, Thread: " + Thread.currentThread().getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent, Thread: " + Thread.currentThread().getName());
        int sleepTime = intent.getIntExtra("sleepTime", 1);

        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");

        int ctr = 1;
        // Dummy long operation
        while(ctr <= sleepTime){
            Log.i(TAG, "Counter: " + ctr);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctr++;
        }

        Bundle bundle = new Bundle();
        bundle.putString("resultIntentService", "Counter stopped at " + ctr);

        //https://stackoverflow.com/questions/4510974/using-resultreceiver-in-android
        resultReceiver.send(18, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy, Thread: " + Thread.currentThread().getName());
    }
}
