package com.tcd3d5b.timezone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsActivity extends AppCompatActivity {
    private final Object TAG = "my_meeting";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

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

        final ArrayList<HashMap<String, Object>> myMeeting = getListItem();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(myMeeting.size()!=0) {
                    SimpleAdapter mSimpleAdapter = new SimpleAdapter(NotificationsActivity.this,
                            myMeeting,
                            R.layout.item_list,
                            new String[] {"meetingName","meetingDate", "meetingTime", "meetingDuration", "meetingID", "button_detail"},
                            new int[] {R.id.meetingName,R.id.meetingDate,R.id.meetingTime,R.id.meetingDuration,R.id.meetingID,R.id.button_detail}) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final View v = super.getView(position, convertView, parent);

                            Button b=(Button)v.findViewById(R.id.button_detail);
                            final String str = b.getText().toString();
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    Bundle bundle = new Bundle();
                                    Intent intent=new Intent();
                                    intent.setClass(NotificationsActivity.this,OneMeetingActivity.class);
                                    bundle.putString("meetingID", str);
                                    intent.putExtra("meeting_detail", bundle);
                                    startActivity(intent);
                                }
                            });
                            return v;
                        }
                    };
                    ListView list= (ListView) findViewById(R.id.lv_news_list);
                    list.setAdapter(mSimpleAdapter);
                } else {
                    Log.d((String) TAG, "wtf: " + myMeeting.size());
                }

            }
        },2000);


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
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.meetings:
                        return true;
                }
                return false;
            }
        });
    }


    public ArrayList<HashMap<String, Object>> getListItem() {
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        final DatabaseReference DBref;
        DBref = FirebaseDatabase.getInstance().getReference();
        final Query userQuery = DBref.child("user").child("useremail_one").child("meetings");

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    Toast.makeText(getApplicationContext(), "You haven't any meeting.", Toast.LENGTH_LONG).show();
                } else {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        final String meetingID = child.getValue().toString();
                        final HashMap<String, Object> map = new HashMap<String, Object>();
                        Log.d((String) TAG, child.getValue().toString());

                        final Query meetingQuery = DBref.child("meeting").child(meetingID);
                        meetingQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                                    Toast.makeText(getApplicationContext(), "You haven't any meeting.", Toast.LENGTH_LONG).show();
                                } else {
                                    int i=0;
                                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                                        if(i==1) {
                                            map.put("meetingDate", child.getValue().toString());
                                            Log.d((String) TAG, "meetingDate: " + child.getValue().toString());
                                        } else if(i==2) {
                                            map.put("meetingDuration", child.getValue().toString());
                                            Log.d((String) TAG, "meetingDuration: " + child.getValue().toString());
                                        } else if(i==3) {
                                            map.put("meetingName", child.getValue().toString());
                                            Log.d((String) TAG, "meetingName: " + child.getValue().toString());
                                        } else if(i==4) {
                                            map.put("meetingTime", child.getValue().toString());
                                            Log.d((String) TAG, "meetingTime: " + child.getValue().toString());
                                        }
                                        i++;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Fail 2 !!!!", Toast.LENGTH_LONG).show();
                            }
                        });

                        map.put("meetingID", meetingID);
                        map.put("button_detail", meetingID);

                        listItem.add(map);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Fail!!!!", Toast.LENGTH_LONG).show();
            }
        });


//        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,
//                listItem,
//                R.layout.item_list,
//                new String[] {"meetingName","meetingDate", "meetingTime", "meetingDuration", "meetingID", "button_detail"},
//                new int[] {R.id.meetingName,R.id.meetingDate,R.id.meetingTime,R.id.meetingDuration,R.id.meetingID,R.id.button_detail}) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
//
//                Button b=(Button)v.findViewById(R.id.button_detail);
//                final String str = b.getText().toString();
//                b.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        Toast.makeText(NotificationsActivity.this,str,Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return v;
//            }
//        };
//
//        ListView list= (ListView) findViewById(R.id.lv_news_list);
//        list.setAdapter(mSimpleAdapter);

        return listItem;
    }





    public HashMap<String, Object> getMeetingDetail(String meetingID) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        DatabaseReference DBref;
        DBref = FirebaseDatabase.getInstance().getReference();
        final Query meetingQuery = DBref.child("meeting").child(meetingID);
        meetingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( !dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    Toast.makeText(getApplicationContext(), "You haven't any meeting.", Toast.LENGTH_LONG).show();
                } else {
                    int i=0;
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if(i==1) {
                            map.put("meetingDate", child.getValue().toString());
                            Log.d((String) TAG, "meetingDate: " + child.getValue().toString());
                        } else if(i==2) {
                            map.put("meetingDuration", child.getValue().toString());
                            Log.d((String) TAG, "meetingDuration: " + child.getValue().toString());
                        } else if(i==3) {
                            map.put("meetingName", child.getValue().toString());
                            Log.d((String) TAG, "meetingName: " + child.getValue().toString());
                        } else if(i==4) {
                            map.put("meetingTime", child.getValue().toString());
                            Log.d((String) TAG, "meetingTime: " + child.getValue().toString());
                        }
                        i++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Fail 2 !!!!", Toast.LENGTH_LONG).show();
            }
        });

        map.put("meetingID", meetingID);
        map.put("button_detail", meetingID);

        return map;
    }
}
