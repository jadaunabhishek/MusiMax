package com.example.onlinemusicplayer.AdminCode;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinemusicplayer.AdminCode.ClassJavaAdmin.UploadSong;
import com.example.onlinemusicplayer.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AdminMainScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textViewImage;
    ProgressBar progressBar;
    Uri audioUri ;
    StorageReference mStorageref;
    StorageTask mUploadsTask ;
    DatabaseReference referenceSongs ;
    String songsCategory ;
    MediaMetadataRetriever metadataRetriever;
    byte [] art ;
    String title1, artist1, album_art1 = "", durations1;
    TextView title, artist,album, durations,dataa;
    ImageView album_art ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_activity);

        textViewImage = findViewById(R.id.textViewSongsFilesSelected);
        progressBar = findViewById(R.id.progressbar);
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        durations = findViewById(R.id.duration);
        album = findViewById(R.id.album);
        dataa = findViewById(R.id.dataa);
        album_art = findViewById(R.id.imageview);

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageref = FirebaseStorage.getInstance().getReference().child("songs");

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);


        List<String> categories = new ArrayList<>();

        categories.add("Love Songs");
        categories.add("Sad Songs");
        categories.add("Party Songs");
        categories.add("Birthday Songs");
        categories.add("God Songs");

        ArrayAdapter<String> dataAdpter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, categories);

        dataAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdpter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        songsCategory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "Selected: "+songsCategory, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  void openAudioFiles (View v ){

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i,101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode  == RESULT_OK && data.getData() != null){

            audioUri = data.getData();
            String fileNames = getFileName(audioUri);
            textViewImage.setText(fileNames);
            metadataRetriever.setDataSource(this,audioUri);

            art = metadataRetriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
            album_art.setImageBitmap(bitmap);
            album.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            dataa.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            durations.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            title.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

            artist1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            durations1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);


        }


    }

    private  String getFileName(Uri uri){

        String result = null;
        if(uri.getScheme().equals("content")){

            Cursor cursor = getContentResolver().query(uri, null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()) {

                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }
            finally {
                cursor.close();
            }
        }

        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut +1);

            }
        }
        return  result;
    }

    public  void  uploadFileTofirebase (View v ){
        if(textViewImage.equals("No file Selected")){
            Toast.makeText(this, "please selected an image!", Toast.LENGTH_SHORT).show();

        }
        else{
            if(mUploadsTask != null && mUploadsTask.isInProgress()){
                Toast.makeText(this, "Song upload already in progress!", Toast.LENGTH_SHORT).show();

            }else {
                uploadFiles();
            }
        }

    }

    private void uploadFiles() {

        if(audioUri != null){
            Toast.makeText(this, "Uploading please wait!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final  StorageReference storageReference = mStorageref.child(System.currentTimeMillis()+"."+getfileextension(audioUri));
            mUploadsTask = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            UploadSong uploadSong = new UploadSong(songsCategory,title1,artist1,album_art1,durations1,uri.toString());
                            String uploadId = referenceSongs.push().getKey();
                            referenceSongs.child(uploadId).setValue(uploadSong);
                            Toast.makeText(AdminMainScreen.this, "Song Successfully Uploaded!", Toast.LENGTH_SHORT).show();



                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progess = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progess);

                }
            });

        }else {
            Toast.makeText(this, "No file Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private  String getfileextension(Uri audioUri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));

    }

    public  void  openAlbumUploadsActivty(View v){
        Intent intent = new Intent( AdminMainScreen.this, UploadAlbumActivity.class);
        startActivity(intent);
    }
}