package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTimetableActivity extends AppCompatActivity {
    TextView timetable;
    private DatabaseReference DBref;
    Button add, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timetable);

        search("p1");

        add = findViewById(R.id.button_add_my_timetable);
        back = findViewById(R.id.button_my_timetable_back);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyTimetableActivity.this, AddTimeableActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyTimetableActivity.this, ProfessorChoiceActivity.class));
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
                    ListView lv = (ListView) findViewById(R.id.listView_mytimetable);
                    final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                    String result = "";
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        for(DataSnapshot time : child.getChildren()) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("ItemTitle", " Time: " + child.getKey() + "  " + time.getKey());
                            map.put("ItemText", " Status: " + time.getValue());
                            listItem.add(map);
                            Log.i(TAG, "onDataChange:timetable : " + time.getKey().toString() + "  " +time.getValue().toString());
                        }
                    }

                    MyAdapter adapter = new MyAdapter(MyTimetableActivity.this, listItem);
                    lv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
