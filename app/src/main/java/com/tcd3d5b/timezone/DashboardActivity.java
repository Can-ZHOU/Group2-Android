package com.tcd3d5b.timezone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static com.tcd3d5b.timezone.R.style.myTimePickerStyle;

public class DashboardActivity extends AppCompatActivity {
    EditText start_time, end_time, date_choice, meeting_title;
    TextView  join_meeting_text, create_meeting_text;
    Spinner tzones;
    private DatabaseReference DBref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        date_choice = findViewById(R.id.date_choice);

        final String myprefs = "myprefs";

        SharedPreferences mpreferences = getSharedPreferences(myprefs,MODE_PRIVATE);

        final String email = mpreferences.getString(getString(R.string.email),"");

        tzones = findViewById(R.id.tz_spinner);

        join_meeting_text = findViewById(R.id.join_meeting_text);
        create_meeting_text = findViewById(R.id.create_meeting_text);

        meeting_title = findViewById(R.id.meeting_title);

        join_meeting_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jmt_intent = new Intent(getApplicationContext(), JoinMeetingActivity.class);
                startActivity(jmt_intent);
            }
        });
        date_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new DatePickerDialog(DashboardActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_choice.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, 2020, 03, 28).show();
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog picker;
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(DashboardActivity.this,
                        myTimePickerStyle,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                start_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog picker;
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(DashboardActivity.this,
                        myTimePickerStyle,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                end_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        create_meeting_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valiDate(date_choice.getText().toString())) {
                    if ((start_time.getText().toString().matches("((0[0-9]|1[0-9]|2[0-3]):[0-5][0-9])")) && (end_time.getText().toString().matches("((0[0-9]|1[0-9]|2[0-3]):[0-5][0-9])"))) {
                        Toast.makeText(getApplicationContext(), "Adding meeting", Toast.LENGTH_LONG).show();
                        long id = Instant.now().getEpochSecond();
                        createMeeting(date_choice.getText().toString(), start_time.getText().toString(), end_time.getText().toString(), Long.toString(id), meeting_title.getText().toString(), email, tzones.getSelectedItem().toString());

                        Intent cmt_intent = new Intent(getApplicationContext(), CreateMeetingActivity.class);
                        cmt_intent.putExtra("confirm_id_key", Long.toString(id));
                        cmt_intent.putExtra("date_key", date_choice.getText().toString());
                        cmt_intent.putExtra("s_time_key", start_time.getText().toString());
                        cmt_intent.putExtra("e_time_key", end_time.getText().toString());

                        startActivity(cmt_intent);
                    } else
                        Toast.makeText(getApplicationContext(), "TimeFormatError: Use {XX:00} Format", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "DateFormatError: Use {20YY-MM-DD} Format", Toast.LENGTH_LONG).show();
            }

        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        // Perform ItemSeletedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.meetings:
                        startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        return true;
                }
                return false;
            }
        });
    }

    private void createMeeting(String date, String start_time, String end_time, String confirmId, String title, String email, String loc) {
        DBref = FirebaseDatabase.getInstance().getReference();
        LocalTime t1 = LocalTime.parse(start_time);
        LocalTime t2 = LocalTime.parse(end_time);
        Duration diff = Duration.between(t1, t2);

        DBref.child("meeting").child(confirmId).child("attendee").child("1").child("local_start").setValue(start_time);
        DBref.child("meeting").child(confirmId).child("attendee").child("1").child("local_end").setValue(end_time);
        DBref.child("meeting").child(confirmId).child("attendee").child("1").child("standard_start").setValue("");
        DBref.child("meeting").child(confirmId).child("attendee").child("1").child("standard_end").setValue("");
        DBref.child("meeting").child(confirmId).child("attendee").child("1").child("location").setValue(loc);
        DBref.child("meeting").child(confirmId).child("attendee").child("1").child("userid").setValue(email);

        DBref.child("meeting").child(confirmId).child("date").setValue(date);
        DBref.child("meeting").child(confirmId).child("start_time").setValue(start_time);
        DBref.child("meeting").child(confirmId).child("meeting_name").setValue(title);
        DBref.child("meeting").child(confirmId).child("duration").setValue(diff.toHours());
        DBref.child("meeting").child(confirmId).child("suggest_start").setValue(start_time);
        DBref.child("meeting").child(confirmId).child("suggest_end").setValue(end_time);

        Toast.makeText(getApplicationContext(), "Your Unique ID is" + confirmId, Toast.LENGTH_LONG).show();

        return;
    }

    public static boolean valiDate(String strDate)  {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))  {
            return true;
        }
        /* Date is not 'null' */
        else    {
            SimpleDateFormat strdate = new SimpleDateFormat("yyyy-MM-dd");
            strdate.setLenient(false);
            try {
                Date javaDate = strdate.parse(strDate);
                System.out.println(strDate+" is valid date format");
            }
            /* Date format is invalid */
            catch (ParseException e)    {
                System.out.println(strDate+" is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }

    public void launchJoinMeeting(View view) {
        Intent intent = new Intent(this, JoinMeetingActivity.class);
        startActivity(intent);
    }
}