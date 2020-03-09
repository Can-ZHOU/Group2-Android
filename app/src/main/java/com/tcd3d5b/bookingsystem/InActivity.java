package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class InActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        final Intent intent=new Intent(this,MainActivity.class);
        Timer timer=new Timer();
        TimerTask task=new TimerTask()
        {
            @Override
            public void run(){
                startActivity(intent);
            }
        };
        timer.schedule(task,2*1000);
    }
}
