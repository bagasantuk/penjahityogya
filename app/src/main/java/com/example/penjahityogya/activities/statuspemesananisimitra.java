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
import android.widget.TextView;

import com.example.penjahityogya.FirebaseDatabaseHelper;
import com.example.penjahityogya.Pesan;
import com.example.penjahityogya.R;
import com.example.penjahityogya.RecyclerViewConfig;
import com.example.penjahityogya.ViewHolder.MitraViewHolder;
import com.example.penjahityogya.ViewHolder.StatusViewHolder;
import com.example.penjahityogya.models.Cart;
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

import java.util.List;

public class statuspemesananisimitra extends AppCompatActivity{

    FirebaseAuth mAuth;
    FirebaseUser currentUser ;

    private DatabaseReference OrderRef,reference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String idMitra, nama;
    TextView Nama;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuspemesananisimitra);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Intent in = getIntent();
//        idMitra = in.getStringExtra("idMitra");

//        OrderRef = FirebaseDatabase.getInstance().getReference().child("Orderan").child("User View").child(currentUser.getUid()).child(idMitra);

        //tampilan daftar orderan
        recyclerView = findViewById(R.id.recycle_isimitra);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Nama= findViewById(R.id.list_isimitra);

    }
    @Override
    protected void onStart()
    {
        super.onStart();

//        FirebaseRecyclerOptions<Cart> options =
//                new FirebaseRecyclerOptions.Builder<Cart>()
//                        .setQuery(OrderRef, Cart.class)
//                        .build();
//
//
//        FirebaseRecyclerAdapter<Cart, MitraViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Cart, MitraViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull MitraViewHolder holder, int posisition, @NonNull Cart cart) {
//                        ambilNama(get(posisition));
//                        holder.txtNamaMitra.setText(nama);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                Intent intent = new Intent(statuspemesananisimitra.this, ListPemesananPelanggan.class);
//                                intent.putExtra("status",cart.getStatus());
//                                intent.putExtra("pid",cart.getPid());
////                            intent.putExtra("total",cart.getTotal());
//                                startActivity(intent);
//                            }
//                        });
//                    }
//
//                    int oneTypeProductPrice;
//
//                    @NonNull
//                    @Override
//                    public MitraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_isi_statusmitra, parent, false);
//                        MitraViewHolder holder = new MitraViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
        new FirebaseDatabaseHelper().readBooks(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Pesan> books, List<String> keys) {
                new RecyclerViewConfig().setConfig(recyclerView, statuspemesananisimitra.this,
                        books, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdate() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        return false;
//    }

//    void ambilNama(String id){
//        reference = FirebaseDatabase.getInstance().getReference().child("mitra").child(id);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child("usaha").getValue()!=null ) {
//                    nama = dataSnapshot.child("usaha").getValue(String.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
