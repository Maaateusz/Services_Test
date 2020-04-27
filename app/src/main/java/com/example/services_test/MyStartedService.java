package com.example.services_test;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyStartedService extends Service {

    private static final String TAG = MyStartedService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate, Thread: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand, Thread: " + Thread.currentThread().getName());

        int sleepTime = intent.getIntExtra("sleepTime", 1);
        new MyAsyncTask().execute(sleepTime);

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy, Thread: " + Thread.currentThread().getName());
    }

    class MyAsyncTask extends AsyncTask<Integer, String, Void> {

        private final String TAG = MyAsyncTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Integer... voids) {
            Log.i(TAG, "DoInBackground, Thread: " + Thread.currentThread().getName());
            int sleepTime = voids[0];
            int ctr = 1;
            // Dummy long operation
            while(ctr <= sleepTime){
                publishProgress("Counter: " + ctr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctr++;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute, Thread: " + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Auto Destroy
            stopSelf();
            Log.i(TAG, "onPostExecute, Thread: " + Thread.currentThread().getName());
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onProgressUpdate, Thread: " + Thread.currentThread().getName() + " Counter: " + values[0]);
        }
    }

}
