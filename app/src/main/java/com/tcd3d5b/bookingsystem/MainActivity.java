package com.tcd3d5b.bookingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText email, pwd;
    Button register, studentSignIn, professorSignIn;
    FirebaseAuth myFirebaseAuth;
    Switch mswitch;
    FirebaseAuth.AuthStateListener myAuthListener;
    private SharedPreferences mpreferences;
    private SharedPreferences.Editor meditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFirebaseAuth = FirebaseAuth.getInstance();

        email =(EditText) findViewById(R.id.email_login);
        pwd = (EditText)findViewById(R.id.pwd_login);
        register = findViewById(R.id.button_login_register);
        studentSignIn = findViewById(R.id.button_login_signIn);
        professorSignIn = findViewById(R.id.button_login__professor);
        mswitch = findViewById(R.id.switch1);
        mpreferences =PreferenceManager.getDefaultSharedPreferences(this);
        meditor = mpreferences.edit();

        checksharedprefernces();




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

             if (mswitch.isChecked()){
                 meditor.putString(getString(R.string.switch1),"True");
                 meditor.commit();

                 String name = email.getText().toString();
                 meditor.putString(getString(R.string.email),name);
                 meditor.commit();

                 String password = pwd.getText().toString();
                 meditor.putString(getString(R.string.password),password);
                 meditor.commit();
             }else{
                 meditor.putString(getString(R.string.switch1),"False");
                 meditor.commit();

                 String name = email.getText().toString();
                 meditor.putString(getString(R.string.email),"");
                 meditor.commit();

                 String password = pwd.getText().toString();
                 meditor.putString(getString(R.string.password),"");
                 meditor.commit();

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
    private  void checksharedprefernces(){
        String switch1  = mpreferences.getString(getString(R.string.switch1),"False");
        String name = mpreferences.getString(getString(R.string.email),"");
        String password = mpreferences.getString(getString(R.string.password),"");

        email.setText(name);
        pwd.setText(password);

        if (switch1.equals("True")){
            mswitch.setChecked(true);
        }else{
            mswitch.setChecked(false);
        }


    }


}

