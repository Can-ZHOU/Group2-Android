package com.tcd3d5b.timezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.TextUtils.isEmpty;

public class sign_up extends AppCompatActivity {
EditText name,email,phone,info,location,password;
Button sign;
DatabaseReference dref;
private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long dphone=Long.parseLong(phone.getText().toString().trim());
                user.setName(name.getText().toString().trim());
                final String eml=email.getText().toString().trim();
                user.setEmail(eml);
                user.setInfo(info.getText().toString().trim());
                user.setLocation(location.getText().toString().trim());
                final String pwd= password.getText().toString().trim();
                user.setMobile((int) dphone);

                if(isEmpty(pwd)){
                    Toast.makeText(sign_up.this,"Password cannot be remain Empty!!",Toast.LENGTH_LONG).show();
                     password.requestFocus();
                }
                if((pwd).length()<6){
                    Toast.makeText(sign_up.this,"Password length should be more than 6",Toast.LENGTH_LONG).show();
                    password.requestFocus();
                }

               if(isEmpty(eml)){
                   Toast.makeText(sign_up.this,"Please enter the valid email id",Toast.LENGTH_LONG).show();
                   email.requestFocus();
               }
                int index = eml.indexOf("@");
                final String id= eml.substring(0,index);
                mAuth.fetchSignInMethodsForEmail(eml).addOnCompleteListener(sign_up.this, new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check= !task.getResult().getSignInMethods().isEmpty();
                        if(!check){
                            mAuth.createUserWithEmailAndPassword(eml,pwd).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        dref = FirebaseDatabase.getInstance().getReference().child("user").child(id);
                                        dref.setValue(user);
                                        Toast.makeText(sign_up.this,"Sign up successfully",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),login.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(sign_up.this,"Email ID already Present!!",Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }

        });

    }
    private boolean validateEmailAddress(EditText email){
        String emailInput = email.getText().toString();
        if(!emailInput.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            Toast.makeText(this, "Email Address Validated Successfully!",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(this,"Invalid Email Address! ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
