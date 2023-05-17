package com.example.onlinemusicplayer.UserCode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.onlinemusicplayer.R;
import com.example.onlinemusicplayer.UserCode.Adapter.RecyclerViewAdapter;
import com.example.onlinemusicplayer.UserCode.ClassJavaUser.UserUpload;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_activity);

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
    }
}