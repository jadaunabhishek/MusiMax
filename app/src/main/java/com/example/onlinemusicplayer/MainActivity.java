package com.example.onlinemusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinemusicplayer.UserCode.UserMainScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button phnlogin, signup;
    Button googleLogin;
    GoogleSignInOptions gsc;
    GoogleSignInClient googleSignInUser;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // google login
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        gsc = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInUser = GoogleSignIn.getClient(MainActivity.this, gsc);



        // spinner for user or admin
        Spinner spinner = findViewById(R.id.spinneruser);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                phnlogin = findViewById(R.id.phonelogin);
                signup = findViewById(R.id.signupemail);
                login = findViewById(R.id.loginemail);
                googleLogin = findViewById(R.id.googlelogin);

                // phone authentication login
                phnlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position == 1){
                            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                        }

                        else if(position == 0){
                            startActivity(new Intent(MainActivity.this, UserPhoneLoginActivity.class));
                        }
                    }
                });

                // new user sign up

                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position == 1){
                            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                        }

                        else if(position == 0){
                            startActivity(new Intent(MainActivity.this, UserRegisterEmailActivity.class));

                        }
                    }
                });

                //old user login

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position == 1){
                            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                        }

                        else if(position == 0){
                            startActivity(new Intent(MainActivity.this, UserLoginEmailActivity.class));

                        }
                    }
                });


                googleLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position == 1){
                            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                        }

                        else if(position == 0){
                            signIn();

                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // spinner items
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("User");
        arrayList.add("Admin");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

    }


    // google sign in
    private void signIn() {
        Intent intent = googleSignInUser.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Store in firebase
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("username", account.getDisplayName());
                            map.put("email", account.getEmail());
                            map.put("profile", String.valueOf(account.getPhotoUrl()));
                            map.put("uid", firebaseUser.getUid());
                            map.put("Search", account.getDisplayName().toLowerCase());

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Google Users");
                            reference.child(firebaseUser.getUid()).setValue(map);
                            startActivity(new Intent(MainActivity.this, UserMainScreen.class));
                        } else {
//                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}