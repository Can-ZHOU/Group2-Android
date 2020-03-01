package com.tcd3d5b.bookingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private TextView memail;
    ImageView mimage;
    Button mbutton;

    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

            memail =(TextView) findViewById(R.id.showemail);

        SharedPreferences mpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mpreferences.edit();

        String name = mpreferences.getString(getString(R.string.email),"");
        memail.setText(name);

        mimage = findViewById(R.id.imageView5);
        mbutton=findViewById(R.id.imgbt);

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


