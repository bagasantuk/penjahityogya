package com.example.penjahityogya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.StatusViewHolder;
import com.example.penjahityogya.models.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ListPemesananPelanggan extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference OrderRef, reference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String idMitra;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pemesanan_pelanggan);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Intent in = getIntent();
        idMitra = in.getStringExtra("idMitra");
        sessionManager = new SessionManager(ListPemesananPelanggan.this, SessionManager.SESSION_USER);
        HashMap<String, String> usersDetails = sessionManager.getidMitraFromSession(); //deklarasi HashMap ambil data dari sessionManager

        idMitra = usersDetails.get(SessionManager.KEY_IDMITRA); // contoh ambil data dari HashMap
//
//    sessionManager.logoutUserFromSession(); //Hapus data yang ada di sessionManager (untuk Logout)

        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orderan").child("User View").child(currentUser.getUid()).child(idMitra);

        //tampilan daftar orderan
        recyclerView = findViewById(R.id.recycle_menu_list_pemesanan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(OrderRef, Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, StatusViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, StatusViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StatusViewHolder holder, int posisition, @NonNull Cart cart) {
                        holder.txtBarang.setText("Nama Barang : " + cart.getPname());//
                        holder.txtTotal.setText("Total : " + cart.getTotal());
                        holder.txtStatus.setText("Status : " + cart.getStatus());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ListPemesananPelanggan.this, StatusPemesanan.class);
                                intent.putExtra("status", cart.getStatus());
                                intent.putExtra("pid", cart.getPid());
                                intent.putExtra("idMitra", idMitra);
//                            intent.putExtra("total",cart.getTotal());
                                startActivity(intent);
                            }
                        });
                    }

                    int oneTypeProductPrice;

                    @NonNull
                    @Override
                    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pemesanan_menu_pelanggan, parent, false);
                        StatusViewHolder holder = new StatusViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
