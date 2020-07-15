package com.example.penjahityogya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.penjahityogya.R;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    DatabaseReference reference;
    private Button profil_btn;
    String noTelp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profil_btn = findViewById(R.id.profil_btn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        updateProfile();

    }
        private void updateProfile() {


        EditText profilnama = findViewById(R.id.profil_nama);
        final EditText profiltelp = findViewById(R.id.profil_telp);
        EditText profilemail = findViewById(R.id.profil_email);
        ImageView prophoto = findViewById(R.id.pro_photo);


        profilnama.setText(currentUser.getDisplayName());
        Intent intent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noTelp=dataSnapshot.child("phoneNo").getValue().toString();
                profiltelp.setText(noTelp);
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

        profil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });


    }

}
