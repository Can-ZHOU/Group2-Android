package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyTimetableActivity extends AppCompatActivity {
    TextView timetable;
    private DatabaseReference DBref;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timetable);

        timetable= findViewById(R.id.textView_my_timetable);

        search("p1");

        add = findViewById(R.id.button_add_my_timetable);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyTimetableActivity.this, AddTimeableActivity.class));
            }
        });


    }

    private void search(String professor) {
        DBref = FirebaseDatabase.getInstance().getReference();
        final Query myQuery = DBref.child("professor").child(professor).child("timetable");

        myQuery.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "Professor";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    // TODO
                } else {
                    String result = "";
                    Map<String, String> map = new HashMap<>();
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        result += "\r\n" + child.getKey().toString();
                        for(DataSnapshot time : child.getChildren()) {
                            map.put(time.getKey().toString(),time.getValue().toString());
                            Log.i(TAG, "onDataChange:timetable : " + time.getKey().toString() + "  " +time.getValue().toString());
                        }
                    }

                    for (String k : map.keySet()) {
                        result += "\r\n" + k + " : " + map.get(k);
                    }

                    TextView tv = findViewById(R.id.textView_my_timetable);
                    tv.setText(result);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
