package com.tcd3d5b.bookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcd3d5b.bookingsystem.game2048.Game2048MainActivity;

public class ProfessorChoiceActivity extends AppCompatActivity {
    Button userProfile, booking, professorTimetable, game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_choice);

        userProfile = findViewById(R.id.button_professor_profile);
        booking = findViewById(R.id.button_professor_glassroom);
        professorTimetable = findViewById(R.id.button_professor_add_time);
        game = findViewById(R.id.button_professor_game);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfessorChoiceActivity.this, UserProfileActivity.class));
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfessorChoiceActivity.this, BookingActivity.class));
            }
        });

        professorTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfessorChoiceActivity.this, ProfessorTimetableActivity.class));
            }
        });

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfessorChoiceActivity.this, Game2048MainActivity.class));
            }
        });
    }

}
