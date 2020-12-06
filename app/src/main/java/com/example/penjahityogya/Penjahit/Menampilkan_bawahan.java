package com.example.penjahityogya.Penjahit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.penjahityogya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menampilkan_bawahan extends AppCompatActivity {
    EditText pinggang, pinggul, paha, selangkangan, lutut, pergelangan, panjang;
    String idUser,productID, _pinggang, _pinggul, _paha, _selangkangan, _lutut, _pergelangan, _panjang;
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menampilkan_bawahan);

        pinggang = findViewById(R.id.edtpinggang);
        pinggul = findViewById(R.id.edtpinggul);
        paha = findViewById(R.id.edtpaha);
        selangkangan = findViewById(R.id.edtselangkangan);
        lutut = findViewById(R.id.edtlutut);
        pergelangan = findViewById(R.id.edtpergelangan);
        panjang = findViewById(R.id.edtpanjang);

        Intent in = getIntent();
        idUser = in.getStringExtra("idUser");
        productID = in.getStringExtra("pid");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ambilData();
    }

    private void ambilData() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Ukuran")
                .child(currentUser.getUid())
                .child(idUser)
                .child("Bawahan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("pinggang").getValue() != null
                        && dataSnapshot.child("pinggul").getValue() != null
                        && dataSnapshot.child("paha").getValue() != null
                        && dataSnapshot.child("selangkangan").getValue() != null
                        && dataSnapshot.child("lutut").getValue() != null
                        && dataSnapshot.child("pergelangan").getValue() != null
                        && dataSnapshot.child("panjang").getValue() != null
                ) {
                    pinggang.setText(dataSnapshot.child("pinggang").getValue().toString());
                    paha.setText(dataSnapshot.child("paha").getValue().toString());
                    selangkangan.setText(dataSnapshot.child("selangkangan").getValue().toString());
                    lutut.setText(dataSnapshot.child("lutut").getValue().toString());
                    pinggul.setText(dataSnapshot.child("pinggul").getValue().toString());
                    pergelangan.setText(dataSnapshot.child("pergelangan").getValue().toString());
                    panjang.setText(dataSnapshot.child("panjang").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
