package com.tcd3d5b.bookingsystem.game2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcd3d5b.bookingsystem.R;

public class Game2048MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048_main);
        final Intent newGameField = new Intent(this, newGameField.class);
        final Intent easy = new Intent(this, easy.class);
        final Intent hard = new Intent(this, hard.class);

        Button standart = findViewById(R.id.standartGame);
        standart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(newGameField);
            }
        });


        View backgroundImage = findViewById(R.id.linearLayout);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(20);


         Button easyField = findViewById(R.id.easyGame);
        easyField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(easy);
            }
        });

        Button hardField = findViewById(R.id.hardGame);
        hardField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(hard);
            }
        });
    }
}
