package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.Gravity.CENTER;

public class ProfessorTimeConfirmActivity extends AppCompatActivity {
    private static final String TAG = "status_p";
    private ListView lv;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_time_confirm);

        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("timetable");
        final String chosenDate = bundle.getString("chosenDate").substring(3);
        final String professor = bundle.getString("chosenDate").substring(0,2);
        Log.i(TAG, "onCreate: " + professor + "   " + chosenDate);
        List<Map<String, Object>> lists= (List<Map<String, Object>>)list.get(0);

        Intent intent = getIntent();

        lv = (ListView) findViewById(R.id.listView1);
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        TextView tv = findViewById(R.id.header_date);
        tv.setText(chosenDate);

        for (Map<String, Object> m : lists) {
            for (String k : m.keySet()) {
                if(! m.get(k).toString().equals("booked")) {

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
                String date = chosenDate;
                String time = map.get("ItemTitle").toString().substring(7);
                mDatabase.child("professor").child(professor).child("timetable").child(date).child(time).setValue("booked");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "The meeting has booked!", Toast.LENGTH_LONG);
                toast.setGravity(CENTER, 0, 0);
                toast.show();
                //startActivity(new Intent(ProfessorTimeConfirmActivity.this, BookedTimeConfirmActivity.class));
            }
        });
    }
}
