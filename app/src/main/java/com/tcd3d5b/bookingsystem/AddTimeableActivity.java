package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTimeableActivity extends AppCompatActivity {
    private TextView date, time;
    private Button add;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timeable);

        date = findViewById(R.id.editText_add_date);
        time = findViewById(R.id.editText_add_time);
        add = findViewById(R.id.button_add_timetable);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String professor = "p1";
                mDatabase.child("professor").child(professor).child("timetable").child(date.getText().toString()).child(time.getText().toString()).setValue("valid");
                startActivity(new Intent(AddTimeableActivity.this, AddTimetableComfirmActivity.class));
            }
        });
    }
}
