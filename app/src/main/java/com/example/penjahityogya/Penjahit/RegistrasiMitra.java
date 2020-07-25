package com.example.penjahityogya.Penjahit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.penjahityogya.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RegistrasiMitra extends AppCompatActivity {

    ImageView ImgMitraPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri;

    private EditText mitraEmail,mitraPassword,mitraPAssword2,mitraUsaha,mitraTelp,mitraAlamat;
    private ProgressBar loadingProgress;
    private Button regMitraBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_mitra);

        //ini views
        mitraEmail = findViewById(R.id.regMitraMail);
        mitraPassword = findViewById(R.id.regMItraPassword);
        mitraPAssword2 = findViewById(R.id.regMitraPassword2);
        mitraUsaha = findViewById(R.id.regMitraUsaha);
        mitraTelp = findViewById(R.id.regMitraTelp);
        mitraAlamat = findViewById(R.id.regMitraAlamat);
        loadingProgress = findViewById(R.id.regProgressBar);
        regMitraBtn = findViewById(R.id.regMitraBtn);

        loadingProgress.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        regMitraBtn.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(RegistrasiMitra.this, LoginMitra.class);
            @Override
            public void onClick(View view) {
                regMitraBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = mitraEmail.getText().toString();
                final String password = mitraPassword.getText().toString();
                final String password2 = mitraPAssword2.getText().toString();
                final String usaha = mitraUsaha.getText().toString();
                final String Telp = mitraTelp.getText().toString();
                final String Alamat = mitraAlamat.getText().toString();

                if( email.isEmpty() || usaha.isEmpty() || password.isEmpty() || Telp.isEmpty() || Alamat.isEmpty() || !password.equals(password2)) {


                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields") ;
                    regMitraBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);


                }
                else {
                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid


                    CreateMitraAccount(email,usaha,password);
                }

            }
        });

        ImgMitraPhoto = findViewById(R.id.regMitraPhoto) ;

        ImgMitraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();

                }
                else
                {
                    openGallery();
                }

            }
        });
    }

    private void CreateMitraAccount(String email, final String usaha, String password) {
        // this method create user account with specific email and password

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // user account created successfully
                            showMessage("Account created");
                            // after we created user account we need to update his profile picture and name
                            updateMitraInfo( usaha ,pickedImgUri,mAuth.getCurrentUser());
                            onAuthSuccess(task.getResult().getUser());




                        }
                        else
                        {

                            // account creation failed
                            showMessage("account creation failed" + task.getException().getMessage());
                            regMitraBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }
    private void onAuthSuccess(FirebaseUser mitra) {
        String mitrausaha = mitraUsaha.getText().toString();

        // membuat User admin baru
        writeNewmitra(mitra.getUid(), mitrausaha, mitra.getEmail(), mitraAlamat.getText().toString(),mitraTelp.getText().toString(),mitraPassword.getText().toString());

        // Go to MainActivity
        startActivity(new Intent(RegistrasiMitra.this, LoginMitra.class));
        finish();
    }

    private void updateMitraInfo(final String usaha, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("mitra_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(usaha)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("Register Complete");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });





            }
        });
    }
    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), HomeMitra.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegistrasiMitra.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrasiMitra.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegistrasiMitra.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(RegistrasiMitra.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgMitraPhoto.setImageURI(pickedImgUri);


        }


    }
    private void writeNewmitra(String userId, String usaha, String email, String Telp, String password, String Alamat) {
        MItraHelperClass user= new MItraHelperClass(userId, usaha, email, Telp, password, Alamat);
        mDatabase.child("mitra").child(userId).setValue(user);
    }}
