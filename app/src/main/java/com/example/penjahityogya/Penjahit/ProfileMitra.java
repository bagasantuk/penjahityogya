package com.example.penjahityogya.Penjahit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.penjahityogya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileMitra extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    DatabaseReference reference;
    String noTelp;
    String alamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_mitra);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        updateProfile();

    }

    private void updateProfile() {
        EditText profilnama = findViewById(R.id.pro_mitraname);
        final EditText profiltelp = findViewById(R.id.pro_mitratelp);
        EditText profilemail = findViewById(R.id.pro_mitraemail);
        final EditText profilalamat = findViewById(R.id.pro_mitraalamat);
        ImageView prophoto = findViewById(R.id.pro_mitraphoto);


        profilnama.setText(currentUser.getDisplayName());
        Intent intent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("mitra").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noTelp=dataSnapshot.child("telp").getValue().toString();
                profiltelp.setText(noTelp);
                alamat=dataSnapshot.child("alamat").getValue().toString();
                profilalamat.setText(alamat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //profiltelp.setText("78");
        //profiltelp.setText(noTelp);
        //profiltelp.setText(currentUser.getPhoneNumber());
        profilemail.setText(currentUser.getEmail());

        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(prophoto);
    }
}
