package com.example.penjahityogya.Penjahit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.penjahityogya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menampilkan_atasan extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    EditText bahu, dada, leher, ketiak, perut, pinggul, pergelangan, panjangtangan, panjang;
    String idUser,productID, _bahu, _dada, _leher, _ketiak, _perut, _pinggul, _pergelangan, _panjangtangan, _panjang;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menampilkan_atasan);

        bahu = findViewById(R.id.edtbahu);
        dada = findViewById(R.id.edtdada);
        leher = findViewById(R.id.edtleher);
        ketiak = findViewById(R.id.edtketiak);
        perut = findViewById(R.id.edtperut);
        pinggul = findViewById(R.id.edtatspinggul);
        pergelangan = findViewById(R.id.edtpergelangantngn);
        panjangtangan = findViewById(R.id.edtpanjangtngn);
        panjang = findViewById(R.id.edtpanjangbaju);

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
                    .child("Atasan");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("bahu").getValue() != null
                            && dataSnapshot.child("dada").getValue() != null
                            && dataSnapshot.child("leher").getValue() != null
                            && dataSnapshot.child("ketiak").getValue() != null
                            && dataSnapshot.child("perut").getValue() != null
                            && dataSnapshot.child("pinggul").getValue() != null
                            && dataSnapshot.child("pergelangan").getValue() != null
                            && dataSnapshot.child("panjangtangan").getValue() != null
                            && dataSnapshot.child("panjang").getValue() != null
                    ) {
                        bahu.setText(dataSnapshot.child("bahu").getValue().toString());
                        dada.setText(dataSnapshot.child("dada").getValue().toString());
                        leher.setText(dataSnapshot.child("leher").getValue().toString());
                        ketiak.setText(dataSnapshot.child("ketiak").getValue().toString());
                        perut.setText(dataSnapshot.child("perut").getValue().toString());
                        pinggul.setText(dataSnapshot.child("pinggul").getValue().toString());
                        pergelangan.setText(dataSnapshot.child("pergelangan").getValue().toString());
                        panjangtangan.setText(dataSnapshot.child("panjangtangan").getValue().toString());
                        panjang.setText(dataSnapshot.child("panjang").getValue().toString());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
