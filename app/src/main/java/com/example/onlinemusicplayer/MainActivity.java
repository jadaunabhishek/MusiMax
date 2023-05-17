package com.example.onlinemusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinemusicplayer.AdminCode.AdminMainScreen;
import com.example.onlinemusicplayer.UserCode.UserMainScreen;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button getstarted;

    TextView logindoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.spinneruser);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

                getstarted = findViewById(R.id.buttonstarted);
                getstarted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position == 1){
                            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                        }

                        else if(position == 0){
                            startActivity(new Intent(MainActivity.this, UserLoginActivity.class));
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("User");
        arrayList.add("Admin");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);




        // as i changed to text view for doctor and patient login therefore this program
//        logindoctor = findViewById(R.id.doctorlogin);
//
//        logindoctor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, doctorlogin.class));
//            }
//        });

    }
}