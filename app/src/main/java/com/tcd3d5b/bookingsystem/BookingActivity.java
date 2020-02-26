package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Date;


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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Formatting for Date & Time
                if (checkValid(date.getText().toString())) {
                    if (time.getText().toString().matches("(1[0-9]|2[0-1]):(00)")) {
                        search(date.getText().toString(), time.getText().toString());
                    } else
                        Toast.makeText(getApplicationContext(), "TimeFormatError: Use {XX:00} Format", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "DateFormatError: Use {20YY-MM-DD} Format", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void search(String date_string, String time_string) {
        DBref = FirebaseDatabase.getInstance().getReference();

        final Query myQuery = DBref.child("glassroom").child("date").child(date_string).child(time_string);

        myQuery.addValueEventListener(new ValueEventListener() {
            private int roomNumber;
            private boolean flag = false;
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
                if(roomNumber <= 9 && !flag){
                    flag = true;
                    addRooom(date.getText().toString(), time.getText().toString(), Integer.toString(roomNumber));
                    Toast.makeText(getApplicationContext(), "Confirmed: " + date.getText().toString()  + time.getText().toString() + "Room: " + (char) roomNumber, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BookingActivity.this, ConfirmationActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "RoomError: Select another date", Toast.LENGTH_LONG).show();
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