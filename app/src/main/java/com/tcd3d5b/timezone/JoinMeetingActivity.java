package com.tcd3d5b.timezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinMeetingActivity extends AppCompatActivity {

    private DatabaseReference DBref;
    EditText join_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_meeting);

        Button save_join_btn;

        save_join_btn = findViewById(R.id.save_join_btn);
        join_ID = findViewById(R.id.join_ID);

        save_join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBref = FirebaseDatabase.getInstance().getReference();
                final String confirmId = join_ID.getText().toString();
                final Query myQuery = DBref.child("meeting").child(confirmId);

                myQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() || dataSnapshot.hasChildren()) {
                            Toast.makeText(JoinMeetingActivity.this, "ID Exists - adding user!", Toast.LENGTH_SHORT).show();
                            DBref.child("meeting").child(confirmId).child("attendee").child("2").child("userid").setValue("dummy");
                        }
                        else{
                            Toast.makeText(JoinMeetingActivity.this, "Wrong ID!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(JoinMeetingActivity.this, "Lmao Fail!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.meetings:
                        startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
    }
}
