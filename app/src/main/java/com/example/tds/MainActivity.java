package com.example.tds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btnsignin, btnsignup;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.pass_login);
        btnsignin = findViewById(R.id.signin);
        btnsignup = findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }
        mDialog = new ProgressDialog(this);



        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String mEmail = email.getText().toString().trim();
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

                mAuth.signInWithEmailAndPassword(mEmail, mpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mDialog.show();
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this,
                                            "Redirecting to Homepage",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,
                                            HomeActivity.class));
                                    mDialog.dismiss();
                                } else {
                                    mDialog.dismiss();
                                    Snackbar.make(view,
                                            "Log-in failed. Try again...",
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Exit the application.")
                .setMessage("Are you sure?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface agr0, int arg1) {
                        finish();
                    }
                }).show();
    }
}