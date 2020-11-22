package com.example.penjahityogya.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.ProductViewHolder;
import com.example.penjahityogya.ViewHolder.MitraViewHolder;
import com.example.penjahityogya.models.Mitra;
import com.example.penjahityogya.models.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    private DatabaseReference MitraRef,reference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String idMitra,jarak;
    double toKM, latitude,longitude;
    SessionManager sessionManager;
    Location locationA = new Location("Location A");
    Location locationB = new Location("Location B");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        sessionManager = new SessionManager(Home.this, SessionManager.SESSION_USER);

        //untuk database produk
        MitraRef = FirebaseDatabase.getInstance().getReference().child("mitra");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Button b=findViewById(R.id.map_c);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Home.this, MapsActivity.class));
//                finish();
//            }
//        });
        // ini
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Home.this, CartActivity.class);
                intent.putExtra("idMitra",idMitra);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        //tampilan produk
        recyclerView = findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        reference = FirebaseDatabase.getInstance().getReference().child("Data").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("latitude").getValue()!=null && dataSnapshot.child("longitude").getValue()!=null) {
                    latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Mitra> options =
                new FirebaseRecyclerOptions.Builder<Mitra>()
                .setQuery(MitraRef, Mitra.class)
                .build();


        FirebaseRecyclerAdapter<Mitra, MitraViewHolder> adapter =
                new FirebaseRecyclerAdapter<Mitra, MitraViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MitraViewHolder holder, int posisition, @NonNull Mitra mitra) {
                        holder.txtNamaMitra.setText(mitra.getUsaha());
                        holder.txtJam.setText(mitra.getJam());
                        holder.txtAlamat.setText(mitra.getAlamat());

                        if(mitra.getLatitude()!=null && mitra.getLongitude()!=null ) {
                            locationA.setLatitude(latitude);
                            locationA.setLongitude(longitude);

                            locationB.setLatitude(Double.parseDouble(mitra.getLatitude()));
                            locationB.setLongitude(Double.parseDouble(mitra.getLongitude()));

                            toKM=locationA.distanceTo(locationB)/1000;
                            jarak =  new DecimalFormat("##.##").format(toKM);
                            holder.txtJarak.setText("Jarak : "+jarak+ " KM");
                        }
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(Home.this, PenjahitDetail.class);
                                idMitra = mitra.getUserId();
                                intent.putExtra("UserId", mitra.getUserId());
                                if(toKM< 3.0) {
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(Home.this, "Area diluar jangkauan",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }



                    @NonNull
                    @Override
                    public MitraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_penjahit_layout, parent, false);
                        MitraViewHolder holder = new MitraViewHolder(view);
                        return holder;
                    }
                };
            recyclerView.setAdapter(adapter);
            adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Home.this, Home.class);
            startActivity(intent);
        }
        else if (id== R.id.nav_cart){
            Intent intent = new Intent(Home.this, CartActivity.class);
            intent.putExtra("idMitra",idMitra);
            startActivity(intent);
        }
        else if (id== R.id.nav_pemesanan){
            Intent intent = new Intent(Home.this, statuspemesananisimitra.class);
            startActivity(intent);}
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(Home.this, Riwayat.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            sessionManager.logoutUserFromSession();
            Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivity);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;



    }
    //TODO: MENAMPILKAN DATA PRODUK PADA HOME
    public void updateNavHeader(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_mitra_mail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_mitra_photo);

        navUserMail.setText (currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);


    }

}
