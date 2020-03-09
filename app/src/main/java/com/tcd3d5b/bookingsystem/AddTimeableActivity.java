package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        SharedPreferences mpreferences = getSharedPreferences(myprefs,MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreferences.edit();

        final String professorEmail = mpreferences.getString(getString(R.string.email),"");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                if (checkValid(date.getText().toString())) {
                    if (time.getText().toString().matches("(1[0-9]|2[0-1]):(00)")) {
                        String professor;
                        if(professorEmail.equals("professor1@tcd.ie")) {
                            professor = "p1";
                        } else {
                            professor = "p2";
                        }
                mDatabase.child("professor").child(professor).child("timetable").child(date.getText().toString()).child(time.getText().toString()).setValue("valid");
                startActivity(new Intent(AddTimeableActivity.this, AddTimetableComfirmActivity.class));
                    }
                    else
                        Toast.makeText(getApplicationContext(), "TimeFormatError: Use {HH:mm} Format", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getApplicationContext(), "DateFormatError: Use {20YY-MM-DD} Format", Toast.LENGTH_LONG).show();
            }
            public boolean checkValid(String strDate)
            {
                /* Check if date is 'null' */
                if (strDate.trim().equals(""))
                {
                    return true;
                }
                /* Date is not 'null' */
                else
                {
                    SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
                    strdate.setLenient(false);
                    try
                    {
                        Date javaDate = strdate.parse(strDate);
                        System.out.println(strDate+" is valid date format");
                    }
                    /* Date format is invalid */
                    catch (ParseException e)
                    {
                        System.out.println(strDate+" is Invalid Date format");
                        return false;
                    }
                    /* Return true if date format is valid */
                    return true;
                }
            }
        });
    }
}
