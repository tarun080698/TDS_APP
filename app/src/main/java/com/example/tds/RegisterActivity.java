package com.example.tds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btnsignin, btnsignup;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email_register);
        password = findViewById(R.id.pass_register);
//        btnsignin = findViewById(R.id.signin);
        btnsignup = findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();

        Context context;
        mDialog = new ProgressDialog(this);
//        btnsignin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mpass = password.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Required Field.");
                    return;
                }
                if (TextUtils.isEmpty(mpass)) {
                    password.setError("Required Field.");
                }
                mDialog.setMessage("Please wait...");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail, mpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registration Successful." +
                                                    "\nPlease Log-in...",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}