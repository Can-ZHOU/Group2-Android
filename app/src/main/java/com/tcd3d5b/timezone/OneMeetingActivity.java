package com.tcd3d5b.timezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OneMeetingActivity extends AppCompatActivity {
    private static final Object TAG = "one_meeting";
    TextView Name, Date, Time_start, Time_end, SuggestTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_meeting);

        Date = findViewById(R.id.one_meeting_date);
        Name = findViewById(R.id.one_meeting_name);
        Time_start = findViewById(R.id.editStartTime);
        Time_end = findViewById(R.id.editEndTime);
        SuggestTime = findViewById(R.id.one_meeting_suggest);

        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("meeting_detail");
        String meetingID =bundle.getString("meetingID");

        getInfo(meetingID);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.meetings);
        // Perform ItemSeletedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.meetings:
                        startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
    }

    public void getInfo(String meetingID) {

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d((String) TAG, "connected");
                } else {
                    Log.d((String) TAG, "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w((String) TAG, "Listener was cancelled");
            }
        });


        final DatabaseReference DBref;
        DBref = FirebaseDatabase.getInstance().getReference();
        final Query attendeeQuery = DBref.child("meeting").child(meetingID).child("attendee");
        final Query detailQuery = DBref.child("meeting").child(meetingID);

        attendeeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    Toast.makeText(getApplicationContext(), "You haven't any attendee.", Toast.LENGTH_LONG).show();
                } else {
                    ListView lv = (ListView) findViewById(R.id.lv_group_member);
                    final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        int i=0;
                        String localTime = "";
                        String standardTime = "";
                        for(DataSnapshot attendee : child.getChildren()) {
                            if(i==0) {
                                localTime = attendee.getValue().toString();
                            } else if(i==1) {
                                localTime = attendee.getValue().toString() + " - " + localTime;
                                map.put("attendeeLocalTime", "Local Time: " + localTime);
                                Log.d((String) TAG, localTime);
                            } else if(i==2) {
                                map.put("attendeeLocation", attendee.getValue().toString());
                                Log.d((String) TAG, attendee.getValue().toString());
                            } else if(i==3) {
                                standardTime = attendee.getValue().toString();
                            } else if(i==4) {
                                standardTime = attendee.getValue().toString() + " - " + standardTime;
                                map.put("attendeeStandardTime", "Standard Time: " + standardTime);
                                Log.d((String) TAG, standardTime);
                            } else {
                                map.put("attendeeName", attendee.getValue().toString());
                                Log.d((String) TAG, attendee.getValue().toString());
                            }
                            i++;
                        }
                        listItem.add(map);
                    }
                    AttendeeAdapter adapter = new AttendeeAdapter(OneMeetingActivity.this, listItem);
                    lv.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        detailQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    Toast.makeText(getApplicationContext(), "You haven't any meeting.", Toast.LENGTH_LONG).show();
                } else {
                    int i=0;
                    String suggestTime="";
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if(i==1) {
                            Date.setText("Meeting Date: " + child.getValue().toString());
                            Log.d((String) TAG, child.getValue().toString());
                        } else if(i==2) {
                            Time_end.setText(child.getValue().toString());
                            Log.d((String) TAG, child.getValue().toString());
                        } else if(i==3){
                            Name.setText(child.getValue().toString());
                            Log.d((String) TAG, child.getValue().toString());
                        } else if(i==4) {
                            Time_start.setText(child.getValue().toString());
                            Log.d((String) TAG, child.getValue().toString());
                        } else if(i==5) {
                            suggestTime = child.getValue().toString();
                        } else if(i==6) {
                            suggestTime = "Suggest Time: " + child.getValue().toString() + " - " + suggestTime;
                            SuggestTime.setText(suggestTime);
                        }
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
