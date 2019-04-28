package com.example.asuss.turkcell_firebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteActivity extends AppCompatActivity {

    EditText not;
    Button button;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        not = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Burada notlarımızı yazıyoruz, eklediğimiz notu veri tabanımıza gönderiyoruz.
                addNote();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notesIntent = new Intent(AddNoteActivity.this, MyNotes.class);
                startActivity(notesIntent);
            }
        });
    }

    private void addNote() {
        // Database'den bir instance çıkarıyoruz.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // Burada veri tabanına bir child oluşuturyoruz. Bunun altına gelecek bizim yazdığımız notlar.
        // Oluşturmamışsa oluşturuyor, eğer varsa onun üzerine pushluyoruz.
        DatabaseReference myRef = firebaseDatabase.getReference().child("GezdigimYerler");

        //Bir tane ID tanımlıyoruz. Bu ID'yi Gezdigim Yerlere ekliyoruz. Eklediğimiz notların ID'si.
        String notesID = myRef.push().getKey();
        String receivedUserNote = not.getText().toString();

        // Girilen verinin uzunluğunu kontrol ediyoruz.
        if(receivedUserNote.length() > 0){
            myRef.child(notesID).child("sehirAdi").setValue(receivedUserNote);
            showDialog("Başarılı","Notunuz kaydedildi");
        }

        // Eğer girilen veri yoksa yine uyarı ekranı gelir.
        else{
            showDialog("Başarısız","Not alanı boş bırakılmaz.");
        }

        not.setText("");

    }

    private void showDialog(String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);

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
