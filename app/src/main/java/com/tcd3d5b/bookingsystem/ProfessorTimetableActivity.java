package com.tcd3d5b.bookingsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.HashMap;
import java.util.Map;

public class ProfessorTimetableActivity extends AppCompatActivity {

    private EditText date;
    private Button professor01, professor02;
    private DatabaseReference DBref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_timetable);

        date = findViewById(R.id.professor_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new DatePickerDialog(ProfessorTimetableActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, 2020, 02, 10).show();
            }
        });
        professor01 = findViewById(R.id.button_professor_01);
        professor02 = findViewById(R.id.button_professor_02);

            professor01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search("p1", date.getText().toString());
                }
            });

            professor02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search("p2", date.getText().toString());
                }
            });
    }

    private void search(final String professor, final String date) {
        DBref = FirebaseDatabase.getInstance().getReference();
        final Query myQuery = DBref.child("professor").child(professor).child("timetable").child(date);

        myQuery.addValueEventListener(new ValueEventListener() {
            private static final String TAG2 = "TAG2";
            private static final String TAG = "Professor";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    Toast.makeText(getApplicationContext(), "No slot available", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    ArrayList<Map<String, String>> timetable = new ArrayList<>();
                    for(DataSnapshot time : dataSnapshot.getChildren()) {
                        map.put(time.getKey().toString(),time.getValue().toString());
                        Log.i(TAG, "onDataChange:timetable : " + time.getKey().toString() + "  " +time.getValue().toString());
                    }

                    timetable.add(map);

                    Intent intent = new Intent();
                    intent.setClass(ProfessorTimetableActivity.this,ProfessorTimeConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList bundlelist = new ArrayList();
                    bundlelist.add(timetable);
                    bundle.putParcelableArrayList("timetable",bundlelist);
                    bundle.putString("chosenDate",professor+"/"+date);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
