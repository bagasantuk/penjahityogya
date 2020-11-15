package com.example.penjahityogya.Penjahit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.penjahityogya.PlacePickerActivity;
import com.example.penjahityogya.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.penjahityogya.UserHelperMap;
import com.example.penjahityogya.activities.Home;
import com.example.penjahityogya.helpers.Gxon;
import com.example.penjahityogya.helpers.LocationTrack;
import com.example.penjahityogya.models.AgendaModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.penjahityogya.PlacePickerActivity.REQUEST_PLACE_PICKER;


public class RegistrasiMitra extends AppCompatActivity implements OnMapReadyCallback {

    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    private long backPressedTime;

    FirebaseDatabase database;
    DatabaseReference reff;
    int maxid = 0;

    FirebaseUser currentUser;
    UserHelperMap member;


    //spinner
    private ConstraintLayout constraintLayout;
    private AgendaModel detailAgenda;
    private GoogleMap googleMap;
    private LatLng currentLocation;
    private LocationTrack gps;
    private boolean valEdit;
    private AgendaModel resAlamat = new AgendaModel();
    //private Object View2;

    ImageView ImgMitraPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri;

    private EditText mitraEmail,mitraPassword,mitraPAssword2,mitraUsaha,mitraTelp,mitraAlamat,mitraJam;
    private ProgressBar loadingProgress;
    private Button regMitraBtn, btnPickLokasi;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_mitra);

        gps = new LocationTrack(this);
        currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_mitra);
        mapFragment.getMapAsync(this);
        //getLastID();
        if (getIntent().hasExtra("data") && getIntent().getStringExtra("data") != null) {
                detailAgenda = Gxon.from(getIntent().getStringExtra("data"), AgendaModel.class);
                tampilData(true);
                valEdit = true;
        }

        //ini views
        mitraEmail = findViewById(R.id.regMitraMail);
        mitraPassword = findViewById(R.id.regMItraPassword);
        mitraPAssword2 = findViewById(R.id.regMitraPassword2);
        mitraUsaha = findViewById(R.id.regMitraUsaha);
        mitraTelp = findViewById(R.id.regMitraTelp);
        mitraAlamat = findViewById(R.id.regMitraAlamat);
        mitraJam = findViewById(R.id.regMitraJam);
        loadingProgress = findViewById(R.id.regProgressBar);
        regMitraBtn = findViewById(R.id.regMitraBtn);

        /*Hooks*/
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        btnPickLokasi = findViewById(R.id.btnPilihLokasi);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        member = new UserHelperMap();

        loadingProgress.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        /*Tool Bar*/
        setSupportActionBar(toolbar);
        btnPickLokasi.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent i = new Intent();
                i.setClass(RegistrasiMitra.this, PlacePickerActivity.class);
                startActivityForResult(i, REQUEST_PLACE_PICKER);
            }
        });


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
                final String Jam = mitraAlamat.getText().toString();

                if( email.isEmpty() || usaha.isEmpty() || password.isEmpty() || Telp.isEmpty() || Alamat.isEmpty() || Jam.isEmpty() || !password.equals(password2)) {


                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields") ;
                    regMitraBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);


                }
                CreateMitraAccount(email,usaha,password);

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

    private void tampilData(boolean valEdit)
    {
        mitraAlamat.setText(detailAgenda.getAlamat());
        resAlamat.setLatitude(detailAgenda.getLatitude());
        resAlamat.setLongitude(detailAgenda.getLongitude());
        addMarker(detailAgenda);

        mitraAlamat.setEnabled(valEdit);
        //btnPickLokasi.setVisibility(valEdit ? View.VISIBLE:View.GONE);
        this.valEdit = valEdit;
    }
//    private double distance(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        return (dist);
//    }private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }

    private void addMarker (AgendaModel data){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())));
        markerOptions.title(data.getAlamat());
        googleMap.clear();
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())), 12));
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
        writeNewmitra(mitra.getUid(), mitraUsaha.getText().toString(), mitraEmail.getText().toString(),mitraTelp.getText().toString(),mitraPassword.getText().toString(), mitraAlamat.getText().toString(), mitraJam.getText().toString(), resAlamat.getLatitude(), resAlamat.getLongitude() );

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
        Intent homeActivity = new Intent(getApplicationContext(), LoginMitra.class);
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
        if (requestCode == REQUEST_PLACE_PICKER && resultCode == RESULT_OK) {
            resAlamat = Gxon.from(data.getStringExtra("resAlamat"), AgendaModel.class);
            mitraAlamat.setText(resAlamat.getAlamat());
//                Location locationA = new Location("Location A");
//
//                locationA.setLatitude(-7.792789540441245);
//
//                locationA.setLongitude(110.40796615183355);
//
//                Location locationB = new Location("Location B");
//
//                locationB.setLatitude(-7.803148829355984);
//
//                locationB.setLongitude(110.4178574681282);
//
//                tvAlamat.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB)) + "m");

            addMarker(resAlamat);
        }


    }
    private void writeNewmitra(String userId, String usaha, String email, String Telp, String password, String Alamat, String Jam, String latitude, String longitude) {
        MItraHelperClass user= new MItraHelperClass(userId, usaha, email, Telp, password, Alamat, Jam, longitude, latitude);
        mDatabase.child("mitra").child(userId).setValue(user);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
        if (detailAgenda != null) addMarker(detailAgenda);

    }
}
