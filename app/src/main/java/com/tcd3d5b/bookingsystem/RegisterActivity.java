package com.tcd3d5b.bookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText email, pwd;
    Button register, signIn;
    FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_register);
        pwd = findViewById(R.id.pwd_register);
        register = findViewById(R.id.button_register_register);
        signIn = findViewById(R.id.button_register_signIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_r = email.getText().toString();
                String pwd_r = pwd.getText().toString();

                if (email_r.isEmpty()) {
                    email.setError("Please input email");
                    email.requestFocus();
                } else if (pwd_r.isEmpty()) {
                    pwd.setError("Please input password");
                    pwd.requestFocus();
                } else {
                    myFirebaseAuth.createUserWithEmailAndPassword(email_r, pwd_r).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    });
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }
    private boolean validateEmailAddress(EditText email ){
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
