package com.example.services_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("sleepTime", 5);
        startService(intent);
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
