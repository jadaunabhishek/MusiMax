package com.example.onlinemusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class UserPhoneLoginActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_phn_login);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        phoneInput = findViewById(R.id.phoneinput);
        sendOtp = findViewById(R.id.sendotp);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOtp.setOnClickListener((v)->{
            if(!countryCodePicker.isValidFullNumber()){
                phoneInput.setError("Enter valid phone number");
            }
            Intent intent = new Intent(UserPhoneLoginActivity.this, UserOTPLoginActivity.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);

        });


    }
}