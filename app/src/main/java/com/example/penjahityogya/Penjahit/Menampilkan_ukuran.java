package com.example.penjahityogya.Penjahit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.penjahityogya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Menampilkan_ukuran extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    Button atasan, bawahan;
    String idUser,productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menampilkan_ukuran);
        atasan = findViewById(R.id.btn_atasan);
        bawahan = findViewById(R.id.btn_bawahan);
        Intent in = getIntent();
        idUser = in.getStringExtra("idUser");
        productID = in.getStringExtra("pid");
    }
    @Override
    protected void onStart() {

        super.onStart();

        atasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menampilkan_ukuran.this, Menampilkan_atasan.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("pid",productID);
                startActivity(intent);
            }
        });

        bawahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menampilkan_ukuran.this, Menampilkan_bawahan.class);
                intent.putExtra("idUser",idUser);
                startActivity(intent);
            }
        });
    }
}
