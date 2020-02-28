package com.tcd3d5b.bookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcd3d5b.bookingsystem.game2048.Game2048MainActivity;

public class ChoiceActivity extends AppCompatActivity {
    Button userProfile, booking, professorTimetable, game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        userProfile = findViewById(R.id.button_profile);
        booking = findViewById(R.id.button_booking);
        professorTimetable = findViewById(R.id.button_professor_timetable);
        game = findViewById(R.id.button_game);


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

        professorTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, ProfessorTimetableActivity.class));
            }
        });

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, Game2048MainActivity.class));
            }
        });

    }
}
