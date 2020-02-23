package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.util.ArrayList;


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

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        date = findViewById(R.id.book_glassroom_date);
        time = findViewById(R.id.book_glassroom_time);
        search = findViewById(R.id.button_book_glassroom_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Formatting for Date & Time
                if (date.getText().toString().matches("^(((2[0-9])[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]))")) {
                    if (time.getText().toString().matches("(1[0-9]|2[0-1]):(00)")) {
                        Toast.makeText(getApplicationContext(), "Search: " + date.getText().toString() + time.getText().toString(), Toast.LENGTH_LONG).show();
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

        Query myQuery = DBref.child("glassroom").child("date").child(date_string).child(time_string);

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    /*if(!date_string){

                        // At this date and time, all rooms are available,
                        // add this data, time and room 1 to the database.
                        roomList.add(room.getValue().toString())
                    }*/
                } else {
                    ArrayList<String> roomList = new ArrayList<String>();
                    for (DataSnapshot room : dataSnapshot.getChildren()) {
                        roomList.add(room.getValue().toString());
                        // for debug
                        Log.i(TAG, "onDataChange: room number : " + room.getValue());
                    }

                    int roomNumber = roomList.size();
                    //TODO
                    // At this date and time with this roomNumber to the database.

                    // Jump to AvailableGlassroomActivity
                    startActivity(new Intent(BookingActivity.this, AvailableGlassroomActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Fail......");
            }
        });
    }
}
