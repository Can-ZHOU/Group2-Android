package com.tcd3d5b.bookingsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfessorTimeConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_time_confirm);

        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("timetable");
        List<Map<String, Object>> lists= (List<Map<String, Object>>)list.get(0);

        String sResult = "";
        for (Map<String, Object> m : lists) {
            for (String k : m.keySet()) {
                sResult += "\r\n" + k + " : " + m.get(k);
            }
        }

        TextView tv = findViewById(R.id.textView_timetable);
        tv.setText(sResult);
    }
}
