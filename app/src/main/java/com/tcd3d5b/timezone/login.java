package com.tcd3d5b.timezone;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.text.TextUtils.isEmpty;
public class login extends AppCompatActivity{
EditText l_email,l_pwd;
RadioButton rem;
TextView frgtpwd;
Button sign,login;
FirebaseAuth myFirebaseAuth;
FirebaseAuth.AuthStateListener myAuthListener;
private SharedPreferences mpreferences;
static final String myprefs = "myprefs";
private SharedPreferences.Editor meditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_login);
        l_email=findViewById(R.id.mail);
        l_pwd=findViewById(R.id.pswd);
        rem=findViewById(R.id.rembme);
        frgtpwd = findViewById(R.id.frgtpwd);
        sign=findViewById(R.id.sgnup);
        login=findViewById(R.id.lgn);
        myFirebaseAuth = FirebaseAuth.getInstance();
        mpreferences =getSharedPreferences("myprefs",MODE_PRIVATE);
        meditor = mpreferences.edit();
        checksharedprefernces();
        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser myFirebaseUser = myFirebaseAuth.getCurrentUser();
                if(myFirebaseUser != null) {
                    startActivity(new Intent(login.this,HomeActivity.class));
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String email_s = l_email.getText().toString();
                String pwd_s = l_pwd.getText().toString();
                int index = email_s.indexOf("@");
                final String id= email_s.substring(0,index);

                if (email_s.isEmpty()) {
                    Toast.makeText(login.this,"Please input email",Toast.LENGTH_LONG).show();
                    l_email.requestFocus();
                } else if (pwd_s.isEmpty()) {
                    Toast.makeText(login.this,"Please input password",Toast.LENGTH_LONG).show();
                    l_pwd.requestFocus();
                } else {
                    myFirebaseAuth.signInWithEmailAndPassword(email_s, pwd_s).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(login.this, HomeActivity.class));
                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                intent.putExtra("userid",id);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(login.this,"Check your Email or password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (rem.isChecked()){
                    meditor.putString(getString(R.string.radio),"True");
                    meditor.commit();

                    String email = l_email.getText().toString();
                    meditor.putString(getString(R.string.email),email);
                    meditor.commit();

                    String password = l_pwd.getText().toString();
                    meditor.putString(getString(R.string.password),password);
                    meditor.commit();
                }else{
                    meditor.putString(getString(R.string.radio),"False");
                    meditor.commit();

                    meditor.putString(getString(R.string.email),"");
                    meditor.commit();

                    meditor.putString(getString(R.string.password),"");
                    meditor.commit();

                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,sign_up.class);
                startActivity(intent);

            }
        });

        frgtpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(l_email.getText().toString())
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
    private  void checksharedprefernces(){
        String switch1  = mpreferences.getString(getString(R.string.radio),"False");
        String email = mpreferences.getString(getString(R.string.email),"");
        String password = mpreferences.getString(getString(R.string.password),"");

        l_email.setText(email);
        l_pwd.setText(password);

        if (switch1.equals("True")){
            rem.setChecked(true);
        }else{
            rem.setChecked(false);
        }
    }



}
