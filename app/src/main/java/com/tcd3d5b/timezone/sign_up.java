package com.tcd3d5b.timezone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {
EditText name,email,phone,info,location,password;
Button sign;
DatabaseReference dref;
user user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.nme);
        phone =findViewById(R.id.phne);
        location =findViewById(R.id.cty);
        info = findViewById(R.id.clg);
        email = findViewById(R.id.mail);
        password = findViewById(R.id.pwd);
        sign = findViewById(R.id.sgnup);
        user = new user();
        final String str = name.getText().toString();
        final String id= str.substring(str.indexOf("@")+1);



        dref= FirebaseDatabase.getInstance().getReference().child("user");
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dphone=Integer.parseInt(phone.getText().toString().trim());
                user.setName(name.getText().toString().trim());
                user.setEmail(email.getText().toString().trim());
                user.setInfo(info.getText().toString().trim());
                user.setLocation(location.getText().toString().trim());
                user.setMobile(dphone);
                dref.child(id).push().setValue(user);
                Toast.makeText(sign_up.this,"Sign up successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("userid", id);
                startActivity(intent);


            }

        });

    }
}
