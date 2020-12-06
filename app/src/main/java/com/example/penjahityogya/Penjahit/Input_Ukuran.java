package com.example.penjahityogya.Penjahit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.penjahityogya.R;
import com.example.penjahityogya.activities.BuatBaruActivity;
import com.example.penjahityogya.activities.PenjahitDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Input_Ukuran extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    Button atasan, bawahan;
    String idUser,productID,nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input__ukuran);

        atasan = findViewById(R.id.btn_atasan);
        bawahan = findViewById(R.id.btn_bawahan);
        Intent in = getIntent();
        idUser = in.getStringExtra("idUser");
        productID = in.getStringExtra("pid");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }
    @Override
    protected void onStart() {

        super.onStart();
        reference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(idUser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").getValue() != null
                ) {
                    nama = dataSnapshot.child("name").getValue().toString();
                    HelperUkuran data =new HelperUkuran(idUser,nama);
                    DatabaseReference add2 = FirebaseDatabase.getInstance().getReference().child("tmpUkuran").child(currentUser.getUid()).child(idUser);
                    add2.setValue(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        atasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Input_Ukuran.this, Atasan.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("pid",productID);
                startActivity(intent);
            }
        });

        bawahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input_Ukuran.this, Bawahan.class);
                intent.putExtra("idUser",idUser);
                startActivity(intent);
            }
        });
    }
}
