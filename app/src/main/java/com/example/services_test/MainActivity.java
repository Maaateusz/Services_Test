package com.example.services_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView txvStartedServiceResult, txvIntentServiceResult;
    private Handler handler = new Handler();
    //private MyResultReceiver myResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvStartedServiceResult = (TextView) findViewById(R.id.txvStartedServiceResult);
        txvIntentServiceResult = (TextView) findViewById(R.id.txvIntentServiceResult);

        Button goToSecondActivityButton = (Button) findViewById(R.id.goToSecondActivityButton);
        goToSecondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startStartedService(View view){
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        intent.putExtra("sleepTime", 5);
        startService(intent);
    }

    public void stopStartedService(View view){
        Intent intent = new Intent(MainActivity.this, MyStartedService.class);
        stopService(intent);
    }

    public void startIntentService(View view){

        //ResultReceiver ResultReceiver = new ResultReceiver(null);
        MyResultReceiver myResultReceiver = new MyResultReceiver(new Handler());

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("sleepTime", 5);
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(myStartedServiceReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myStartedServiceReceiver);
    }

    private BroadcastReceiver myStartedServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("startServiceResult");
            txvStartedServiceResult.setText(result);
        }
    };

    // TO receive the data back from MyIntentService using ResultReceiver
    private class MyResultReceiver extends ResultReceiver{

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            Log.i("MyResultreceiver", Thread.currentThread().getName());

            if (resultCode == 18 && resultData != null){
                final String result = resultData.getString("resultIntentService");

                //help access ui element from worker thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyHandler", Thread.currentThread().getName());
                        txvIntentServiceResult.setText(result);
                    }
                });

            }
        }
    }
}

/*
Started Service
        Operates in Main Thread
        May block the UI
        For short operations

Intent Service
        Operates in Worker Thread
        Dont block the UI
        For long operations
*/
