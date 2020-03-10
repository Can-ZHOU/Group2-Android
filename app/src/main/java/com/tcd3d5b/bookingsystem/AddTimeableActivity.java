package com.tcd3d5b.bookingsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static com.tcd3d5b.bookingsystem.R.style.myTimePickerStyle;

public class AddTimeableActivity extends AppCompatActivity {
    private TextView date, time;
    private Button add;
    private DatabaseReference mDatabase;
    static final String myprefs = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timeable);

        date = findViewById(R.id.editText_add_date);
        time = findViewById(R.id.editText_add_time);
        add = findViewById(R.id.button_add_timetable);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new DatePickerDialog(AddTimeableActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, 2020, 02, 10).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog picker;
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(AddTimeableActivity.this,
                        myTimePickerStyle,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        SharedPreferences mpreferences = getSharedPreferences(myprefs,MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreferences.edit();

        final String professorEmail = mpreferences.getString(getString(R.string.email),"");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                        String professor;
                        if(professorEmail.equals("professor1@tcd.ie")) {
                            professor = "p1";
                        } else {
                            professor = "p2";
                        }
                mDatabase.child("professor").child(professor).child("timetable").child(date.getText().toString()).child(time.getText().toString()).setValue("valid");
                startActivity(new Intent(AddTimeableActivity.this, AddTimetableComfirmActivity.class));


            }
        });
    }
}
