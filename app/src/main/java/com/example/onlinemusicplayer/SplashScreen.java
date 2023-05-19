package com.example.onlinemusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.onlinemusicplayer.UserCode.UserMainScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {

                if (mUser!=null) {
                        startActivity(new Intent(SplashScreen.this, UserMainScreen.class));
                }

                else {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
            }
        }, 1500);
    }
}