package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationGlassroomActivity extends AppCompatActivity {

    TextView d,t,r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_glassroom);

        d = (TextView) findViewById(R.id.d_id);
        t = (TextView) findViewById(R.id.t_id);
        r = (TextView) findViewById(R.id.r_id);

        Intent intent = getIntent();

        d.setText(intent.getStringExtra("date_key"));
        t.setText(intent.getStringExtra("time_key"));
        r.setText(intent.getStringExtra("room_key"));
    }
}
