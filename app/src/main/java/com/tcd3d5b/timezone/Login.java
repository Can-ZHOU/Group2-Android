package com.tcd3d5b.timezone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {
    ImageView mImageView;
    Button mChoosebtn;

    private static final int Image_Pick_Code = 1000;
    private static final int Permission_Code = 1001;

    @Override
    protected void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.login);

        //Views
        mImageView = findViewById(R.id.img);
        mChoosebtn = findViewById(R.id.imppic);

        //buttonclick
        mChoosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        //permission not granted so request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, Permission_Code);
                    }
                    else
                    {
                        pickImagefromgallery();
                    }
                }
                else {
                    pickImagefromgallery();
                }
            }
        });
    }

    private void pickImagefromgallery()
    {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Image_Pick_Code);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Permission_Code:
            {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickImagefromgallery();
                }
                else{
                    Toast.makeText(this, "Access not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Image_Pick_Code) {
            mImageView.setImageURI(data.getData());
        }
    }
}
