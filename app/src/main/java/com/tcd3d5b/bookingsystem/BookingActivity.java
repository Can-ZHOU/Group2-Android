package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        date = findViewById(R.id.book_glassroom_date);
        time = findViewById(R.id.book_glassroom_time);
        search = findViewById(R.id.button_book_glassroom_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                // check whether input date and time are valid.
                search(date.getText().toString(), time.getText().toString());
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
                    ArrayList<String> roomList = new ArrayList<String>();
                    for (DataSnapshot room : dataSnapshot.getChildren()) {
                        roomList.add(room.getValue().toString());
                        // for debug
                        Log.i(TAG, "onDataChange: room number : " + room.getValue());
                    }

                    roomNumber = roomList.size()+1;
                    Log.i(TAG, "onDataChange: roomList size: "+roomList.size());

                    // Jump to AvailableGlassroomActivity
                    startActivity(new Intent(BookingActivity.this, AvailableGlassroomActivity.class));
                }

                if (flag==false) {
                    flag = true;
                    addRooom(date.getText().toString(), time.getText().toString(), Integer.toString(roomNumber));
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
}
