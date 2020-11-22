package com.example.penjahityogya.Penjahit;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.MitraViewHolder;
import com.example.penjahityogya.ViewHolder.OrderViewHolder;
import com.example.penjahityogya.activities.Home;
import com.example.penjahityogya.activities.LoginActivity;
import com.example.penjahityogya.activities.Pemesanan;
import com.example.penjahityogya.activities.PenjahitDetail;
import com.example.penjahityogya.activities.Profile;
import com.example.penjahityogya.activities.SessionManager;
import com.example.penjahityogya.models.Cart;
import com.example.penjahityogya.models.Mitra;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Home_mitra extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser ;private DatabaseReference OrderRef,reference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String idPelanggan;
    private int overTotalPrice = 0;
    private TextView txtTotalAmount;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_mitra2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        sessionManager = new SessionManager(Home_mitra.this, SessionManager.SESSION_JASA);

        OrderRef = FirebaseDatabase.getInstance().getReference().child("ambilId").child(currentUser.getUid());
        txtTotalAmount = (TextView) findViewById(R.id.order_total);

        //ini


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        //tampilan daftar orderan
        recyclerView = findViewById(R.id.recycle_menu_mitra);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(OrderRef, Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, OrderViewHolder>(options) {
                    int oneTypeProductPrice;
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int posisition, @NonNull Cart cart) {
                        holder.txtNamaPemesan.setText("Nama Pemesan : "+cart.getNamaUser());
                        holder.txtQuantity.setText("Quantity : "+cart.getQuantity());
                        holder.txtTotal.setText("Total Pembayaran : "+cart.getTotal());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(Home_mitra.this, DetailPemesanan.class);
                                intent.putExtra("idUser",cart.getIdUser());
                                intent.putExtra("nama",cart.getNamaUser());
                                intent.putExtra("total",cart.getTotal());
                                startActivity(intent);


                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_pemesanan, parent, false);
                        OrderViewHolder holder = new OrderViewHolder(view);
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
        getMenuInflater().inflate(R.menu.home_mitra, menu);
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
            Intent intent = new Intent(Home_mitra.this, Home_mitra.class);
            startActivity(intent);
        } else if (id == R.id.nav_mitraprofil) {
            Intent intent = new Intent(Home_mitra.this, ProfileMitra.class);
            startActivity(intent);
        } else if (id == R.id.nav_mitraproduk) {
            Intent intent = new Intent(Home_mitra.this, Produk.class);
            startActivity(intent);
        } else if (id == R.id.nav_mitraabout) {

        } else if (id == R.id.nav_mitralogout) {
            FirebaseAuth.getInstance().signOut();
            sessionManager.logoutJasaFromSession();
            Intent loginActivity = new Intent(getApplicationContext(), LoginMitra.class);
            startActivity(loginActivity);
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateNavHeader(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navMitname = headerView.findViewById(R.id.nav_mitrausername);
        TextView navMitMail = headerView.findViewById(R.id.nav_mitemail);
        ImageView navMitPhoto = headerView.findViewById(R.id.nav_mitphoto);

        navMitMail.setText (currentUser.getEmail());
        navMitname.setText(currentUser.getDisplayName());

        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navMitPhoto);

    }

}
