package com.tcd3d5b.timezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateMeetingActivity extends AppCompatActivity {

    TextView confirmID, create_date, create_s_time, create_e_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        confirmID = (TextView) findViewById(R.id.confirmID);
        create_date = (TextView) findViewById(R.id.create_date);
        create_s_time = (TextView) findViewById(R.id.create_s_time);
        create_e_time = (TextView) findViewById(R.id.create_e_time);

        Intent intent = getIntent();

        confirmID.setText(intent.getStringExtra("confirm_id_key"));
        create_date.setText(intent.getStringExtra("date_key"));
        create_s_time.setText(intent.getStringExtra("s_time_key"));
        create_e_time.setText(intent.getStringExtra("e_time_key"));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
}