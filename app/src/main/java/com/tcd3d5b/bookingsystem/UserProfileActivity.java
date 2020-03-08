package com.tcd3d5b.bookingsystem;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private TextView memail;
    ImageView mimage;

    static final String myprefs = "myprefs";
    Button mbutton, resetpwd;


    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

            memail =(TextView) findViewById(R.id.showemail);

        SharedPreferences mpreferences = getSharedPreferences(myprefs,MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreferences.edit();

        final String name = mpreferences.getString(getString(R.string.email),"");
        memail.setText(name);

        mimage = findViewById(R.id.imageView5);
        mbutton=findViewById(R.id.imgbt);
        resetpwd = findViewById(R.id.button_reset_pwd);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permisiion
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
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
                else{
                    pickimagefromgallery();
                }
            }
        });

        resetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(name)
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


