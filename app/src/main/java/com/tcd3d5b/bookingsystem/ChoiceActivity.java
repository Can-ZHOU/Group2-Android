package com.tcd3d5b.bookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {
    Button userProfile, booking, pastPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        userProfile = findViewById(R.id.button_profile);
        booking = findViewById(R.id.button_booking);
        pastPaper = findViewById(R.id.button_pastpaper);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, UserProfileActivity.class));
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, BookingActivity.class));
            }
        });

        pastPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, PastpaperActivity.class));
            }
        });
    }
}
