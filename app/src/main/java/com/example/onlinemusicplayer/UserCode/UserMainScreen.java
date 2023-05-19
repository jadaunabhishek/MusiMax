package com.example.onlinemusicplayer.UserCode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlinemusicplayer.MainActivity;
import com.example.onlinemusicplayer.R;
import com.example.onlinemusicplayer.SplashScreen;
import com.example.onlinemusicplayer.UserCode.Adapter.RecyclerViewAdapter;
import com.example.onlinemusicplayer.UserCode.ClassJavaUser.UserUpload;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMainScreen extends AppCompatActivity {

    RecyclerView recyclerView ;
    RecyclerViewAdapter adapter ;
    DatabaseReference mDatabase ;
    ProgressDialog progressDialog ;
    private List<UserUpload> userupload;
    Button logOut;
    GoogleSignInOptions gsc;
    GoogleSignInClient googleSignInUser;
    public static final String SHARED_PREFS = "rememberMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_activity);

        logOut = findViewById(R.id.logout);

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        progressDialog = new ProgressDialog(this);
        userupload = new ArrayList<>() ;
        progressDialog.setMessage("please wait ...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                for(DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                    UserUpload upload = postsnapshot.getValue(UserUpload.class);
                    userupload.add(upload);


                }
                adapter = new RecyclerViewAdapter( getApplicationContext(), userupload);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });


        gsc = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleSignInUser = GoogleSignIn.getClient(UserMainScreen.this, gsc);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // random email logOut
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "");
                editor.putString("google", "");
                editor.apply();

                startActivity(new Intent(UserMainScreen.this, SplashScreen.class));
                finish();

                // google logOut
                googleSignInUser.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", "");
                        editor.apply();
                        finish();
                        startActivity(new Intent(UserMainScreen.this, SplashScreen.class));
                    }
                });

                // phone logOut
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}