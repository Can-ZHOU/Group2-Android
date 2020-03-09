package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorTimeConfirmActivity extends AppCompatActivity {
    private static final String TAG = "status_p";
    private ListView lv;
    private DatabaseReference mDatabase;
    static final String myprefs = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_time_confirm);

        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("timetable");
        final String chosenDate = bundle.getString("chosenDate");
        List<Map<String, Object>> lists= (List<Map<String, Object>>)list.get(0);

        SharedPreferences mpreferences = getSharedPreferences(myprefs,MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreferences.edit();

        final String professorEmail = mpreferences.getString(getString(R.string.email),"");

        Intent intent = getIntent();

        lv = (ListView) findViewById(R.id.listView1);
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        TextView tv = findViewById(R.id.header_date);
        tv.setText(chosenDate);

        for (Map<String, Object> m : lists) {
            for (String k : m.keySet()) {
                if(! m.get(k).toString().equals("booked")) {
                    Log.i(TAG, "onCreate: m.get(k)" + m.get(k));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemTitle", " Time: " + k);
                    map.put("ItemText", " Status: " + m.get(k));
                    listItem.add(map);
                }
            }
        }
        MyAdapter adapter = new MyAdapter(this, listItem);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                HashMap<String, Object> map = listItem.get(arg2);
                String professor;
                if(professorEmail.equals("professor1@tcd.ie")) {
                    professor = "p1";
                } else {
                    professor = "p2";
                }
                String date = chosenDate;
                String time = map.get("ItemTitle").toString().substring(7);
                mDatabase.child("professor").child(professor).child("timetable").child(date).child(time).setValue("booked");
                //startActivity(new Intent(ProfessorTimeConfirmActivity.this, BookedTimeConfirmActivity.class));
            }
        });
    }
}