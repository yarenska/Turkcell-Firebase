package com.example.asuss.turkcell_firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyNotes extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<String>();
    String myPlace;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        Button button = (Button)findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyNotes.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        arrayList = getMyNotes();

        listView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    private ArrayList<String> getMyNotes() {
        // Fonkimizi çağırıyoruz.
        showProgressDialog();
        final ArrayList<String> myNotes = new ArrayList<>();
        // Instance yarattık.
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // "GezdigimYerler" collection'ını alıyoruz.
        final DatabaseReference myRef = database.getReference().child("GezdigimYerler");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                // Her bir child'ı alıyoruz ve ArrayList'imize aktarıyoruz.
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.child("sehirAdi").getValue()!=null) {
                        myPlace = ds.child("sehirAdi").getValue().toString();
                        myNotes.add(myPlace);
                    }
                }

                // ArrayAdapter'ı da haberdar ediyoruz. Database'de benden başkası bir değişiklik yaptıysa dinliyor.
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myNotes;
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(MyNotes.this);
        progressDialog.setMessage("Yükleniyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}


