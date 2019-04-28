package com.example.asuss.turkcell_firebase;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MusterilerActivity extends AppCompatActivity {

    EditText musteri;
    Button musteriButton;
    ListView musteriListView;
    ArrayList<String> musteriler;
    ArrayAdapter<String> arrayAdapter;
    String myMusteri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteriler);

        musteri = (EditText)findViewById(R.id.editText3);
        musteriButton = (Button)findViewById(R.id.button3);
        musteriListView = (ListView)findViewById(R.id.listView);

        musteriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musteriEkle();
            }
        });

        musteriler = musterileriGetir();

        musteriListView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1, musteriler);
        musteriListView.setAdapter(arrayAdapter);

        musteriListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final AlertDialog.Builder diyalogMusteri = new AlertDialog.Builder(MusterilerActivity.this);
                diyalogMusteri.setMessage("Silinsin mi?.").setCancelable(false).setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        musteriyiSil(musteriler.get(pos));
                        Log.e("Adımız: ", musteriler.get(pos));
                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                diyalogMusteri.create().show();
            }
        });
    }

    private void musteriyiSil(String ad) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Musteriler").orderByChild("ad_soyad").equalTo(ad);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "onCancelled", databaseError.toException());
            }
        });
    }

    private ArrayList<String> musterileriGetir() {

        final ArrayList<String> musterilerim = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myReference = firebaseDatabase.getReference().child("Musteriler");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musterilerim.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.child("ad_soyad").getValue() != null){
                        myMusteri = ds.child("ad_soyad").getValue().toString();
                        musterilerim.add(myMusteri);
                    }
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return musterilerim;
    }

    private void musteriEkle() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference().child("Musteriler");
        String musteriID = myReference.push().getKey();
        String yeniMusteri = musteri.getText().toString();

        if(yeniMusteri.length() > 0){
            myReference.child(musteriID).child("ad_soyad").setValue(yeniMusteri);
            showDialog("Başarılı","Müşteri kaydedildi");
        }
        else{
            showDialog("Başarısız","Müşteri alanı boş bırakılmaz.");
        }

        musteri.setText("");
    }

    private void showDialog(String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MusterilerActivity.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("TAMAM",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
