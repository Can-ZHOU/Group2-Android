package com.tcd3d5b.bookingsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.tcd3d5b.bookingsystem.R.style.myTimePickerStyle;


public class BookingActivity extends AppCompatActivity {
    private static final String TAG = "RoomNUmber";
    private DatabaseReference DBref;
    Button search;
    EditText date;
    EditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        date = findViewById(R.id.book_glassroom_date);
        time = findViewById(R.id.book_glassroom_time);
        search = findViewById(R.id.button_book_glassroom_search);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new DatePickerDialog(BookingActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                picker = new TimePickerDialog(BookingActivity.this,
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(date.getText().toString(), time.getText().toString());
            }
        });
    }

    private void search(String date_string, String time_string) {
        DBref = FirebaseDatabase.getInstance().getReference();

        final Query myQuery = DBref.child("glassroom").child("date").child(date_string).child(time_string);

        myQuery.addValueEventListener(new ValueEventListener() {
            private int roomNumber, flag = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    roomNumber = 1;
                } else {
                    ArrayList<String> roomList = new ArrayList<>();
                    for (DataSnapshot room : dataSnapshot.getChildren()) {
                        roomList.add(room.getValue().toString());
                        // for debug
                        Log.i(TAG, "onDataChange: room number : " + room.getValue());
                    }
                    roomNumber = roomList.size()+1;
                }
                if(roomNumber <= 9 && flag == 0){
                    flag = 1;
                    addRooom(date.getText().toString(), time.getText().toString(), Integer.toString(roomNumber));
                    Intent intent = new Intent(getApplicationContext(), ConfirmationGlassroomActivity.class);

                    intent.putExtra("date_key", date.getText().toString());
                    intent.putExtra("time_key", time.getText().toString());
                    intent.putExtra("room_key", Integer.toString(roomNumber));

                    startActivity(intent);
                }
                else{
                    //Toast.makeText(getApplicationContext(), "RoomError: Select another date", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Fail......");
            }
        });
    }

    private void addRooom(String date, String time, String roomNumber) {
        DBref.child("glassroom").child("date").child(date).child(time).child(roomNumber).setValue(roomNumber);
    }
    public static boolean checkValid(String strDate)
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
}