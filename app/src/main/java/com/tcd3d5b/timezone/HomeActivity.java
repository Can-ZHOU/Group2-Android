package com.tcd3d5b.timezone;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    TextView mname,mmobile,memail,minfo,mlocation,muserid ;
    ImageView mimage;
    Button rst;

    DatabaseReference myRef;
    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mname = findViewById(R.id.name);
        mmobile = findViewById(R.id.mobile);
        mlocation =findViewById(R.id.location);
        minfo = findViewById(R.id.info);
        memail = findViewById(R.id.email);
        mimage=findViewById(R.id.pic);
        muserid = findViewById(R.id.user_id);
        rst = findViewById(R.id.reset_pwd);

        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    //show popup
                    requestPermissions(permission,PERMISSION_CODE);
                }
                else{
                    //permisiion already granted
                    pickimagefromgallery();

                }
            }
        });
        Intent intent = getIntent();

        final String myprefs = "myprefs";

        SharedPreferences mpreferences = getSharedPreferences(myprefs,MODE_PRIVATE);

        String email = mpreferences.getString(getString(R.string.email),"");

        //Toast.makeText(getApplicationContext(), "Login successful:" + email, Toast.LENGTH_LONG).show();
        int index = email.indexOf("@");
        final String id= email.substring(0,index);

        muserid.setText(id);
        myRef = FirebaseDatabase.getInstance().getReference().child("user").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue() .toString();
                String email=dataSnapshot.child("email").getValue() .toString();
                String mobile=dataSnapshot.child("mobile").getValue() .toString();
                String info=dataSnapshot.child("info").getValue() .toString();
                String location=dataSnapshot.child("location").getValue() .toString();

                mname.setText(name);
                mmobile.setText(mobile);
                mlocation.setText(location);
                minfo.setText(info);
                memail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(memail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Email sent.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        TextView eml;
        eml = findViewById(R.id.email);

        SharedPreferences mpreferences;
        SharedPreferences.Editor meditor;

        mpreferences =getSharedPreferences("myprefs",MODE_PRIVATE);
        meditor = mpreferences.edit();

        String email = eml.getText().toString();
        meditor.putString(getString(R.string.email),email);
        meditor.commit();


        mpreferences =getSharedPreferences("myprefs",MODE_PRIVATE);

        mpreferences.edit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
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
                        return true;
                }
                return false;
            }
        });

    }


    private void pickimagefromgallery() {
        Intent intent = new Intent((Intent.ACTION_PICK));
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    pickimagefromgallery();
                }
                else{
                    Toast.makeText(this,"Permission Denied....!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mimage.setImageURI(data.getData());
        }
    }
}

