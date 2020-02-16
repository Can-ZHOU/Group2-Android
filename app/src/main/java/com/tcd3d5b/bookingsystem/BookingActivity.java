package com.tcd3d5b.bookingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;

public class BookingActivity extends AppCompatActivity {

    EditText date,time,room;
    Button submit;

    DatabaseReference dbDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        room = findViewById(R.id.room);
        submit = findViewById(R.id.savedb);

        dbDate = FirebaseDatabase.getInstance().getReference("date");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate();
            }
        });
    }
    private void addDate(){
        String d = date.getText().toString().trim();
        String t = time.getText().toString().trim();
        String r = room.getText().toString().trim();

        if(!TextUtils.isEmpty(d)) {
        String id = dbDate.push().getKey();

        Database datesave = new Database(d, t, r);
        dbDate.child(id).setValue(datesave);

        Toast.makeText(this, "Added to DB",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "404: enter some date",Toast.LENGTH_LONG).show();
        }

    }
}
