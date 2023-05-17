package com.example.onlinemusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinemusicplayer.AdminCode.AdminMainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    EditText dremail, drpassword2;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[0-9]{10}";
    Button drlogin;
    // User Login Attributes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        // User Login Attributes
        dremail = findViewById(R.id.dremail);
        drpassword2 = findViewById(R.id.drpassword);
        drlogin = findViewById(R.id.drlogin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        // User Login Attributes


        // User Login Attributes
        drlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = dremail.getText().toString();
                String password = drpassword2.getText().toString();

                if (!email.equals("aj@gmail.com")) {
                    dremail.setError("Enter correct Email");
                } else if (password.isEmpty() || password.length() < 8) {
                    drpassword2.setError("Enter proper password");
                } else {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setTitle("Login");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminLoginActivity.this, AdminMainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AdminLoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

        });
        // User Login Attributes
    }

    // User Login Attributes
}