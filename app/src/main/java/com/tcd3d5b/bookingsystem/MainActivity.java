package com.tcd3d5b.bookingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email, pwd;
    Button register, studentSignIn, professorSignIn;
    FirebaseAuth myFirebaseAuth;
    FirebaseAuth.AuthStateListener myAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_login);
        pwd = findViewById(R.id.pwd_login);
        register = findViewById(R.id.button_login_register);
        studentSignIn = findViewById(R.id.button_login_signIn);
        professorSignIn = findViewById(R.id.button_login__professor);

        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser myFirebaseUser = myFirebaseAuth.getCurrentUser();
                if(myFirebaseUser != null) {
                    startActivity(new Intent(MainActivity.this, BookingActivity.class));
                }
            }
        };

        studentSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_s = email.getText().toString();
                String pwd_s = pwd.getText().toString();

                if (email_s.isEmpty()) {
                    email.setError("Please input email");
                    email.requestFocus();
                } else if (pwd_s.isEmpty()) {
                    pwd.setError("Please input password");
                    pwd.requestFocus();
                } else {
                    myFirebaseAuth.signInWithEmailAndPassword(email_s, pwd_s).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, ChoiceActivity.class));
                            }
                        }
                    });
                }
            }
        });

        professorSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_s = email.getText().toString();
                String pwd_s = pwd.getText().toString();

                if (email_s.isEmpty()) {
                    email.setError("Please input email");
                    email.requestFocus();
                } else if (pwd_s.isEmpty()) {
                    pwd.setError("Please input password");
                    pwd.requestFocus();
                } else {
                    myFirebaseAuth.signInWithEmailAndPassword(email_s, pwd_s).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, ProfessorChoiceActivity.class));
                            }
                        }
                    });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
