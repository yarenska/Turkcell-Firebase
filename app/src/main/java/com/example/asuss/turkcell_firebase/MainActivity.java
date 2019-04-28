package com.example.asuss.turkcell_firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Real time database den bi tane database oluşturduk.

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button photoButton = (Button)findViewById(R.id.photoButton);
        Button noteButton = (Button)findViewById(R.id.noteButton);
        Button musteriButton = (Button)findViewById(R.id.musteriButton);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoInt = new Intent(MainActivity.this, AddPhotoActivity.class);
                startActivity(photoInt);
            }
        });

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteInt = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(noteInt);
            }
        });

        musteriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent musteriInt = new Intent(MainActivity.this, MusterilerActivity.class);
                startActivity(musteriInt);
            }
        });

    }
}
