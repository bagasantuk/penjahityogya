package com.example.penjahityogya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.penjahityogya.Penjahit.LoginMitra;
import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.ProductViewHolder;
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
import com.squareup.picasso.Picasso;

public class PenjahitDetail extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    String jam;
    String alamat, idMitra, namaMitra;
    ImageView photo;
    Button buat_baru, vermak;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjahit_detail);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        updateProfile();
//
//        //tampilan produk
//        recyclerView = findViewById(R.id.recycle_menu);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

        buat_baru = findViewById(R.id.buatbaru);
        vermak = findViewById(R.id.vermak);


    }


    private void updateProfile() {
        TextView profilnama = findViewById(R.id.detailMitraNama);
        TextView Jam = findViewById(R.id.detailMitraJam);
        final TextView Alamat = findViewById(R.id.detailMitraAlamat);
//        ImageView photo = findViewById(R.id.detailMitraimageView);

        Intent intent = getIntent();
        idMitra = intent.getStringExtra("UserId");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(idMitra);

        reference = FirebaseDatabase.getInstance().getReference("mitra").child(idMitra);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("jam").getValue() != null
                       && dataSnapshot.child("alamat").getValue() != null) {
                    jam = dataSnapshot.child("jam").getValue().toString();
                    Jam.setText(jam);
                    alamat = dataSnapshot.child("alamat").getValue().toString();
                    Alamat.setText(alamat);
                    namaMitra = dataSnapshot.child("usaha").getValue().toString();
                    profilnama.setText(namaMitra);

//                    String image = dataSnapshot.child("image").getValue().toString();
//                    Glide.with(PenjahitDetail.this).load(image).into(photo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        profilnama.setText(currentUser.getDisplayName());

        // now we will use Glide to load user image
        // first we need to import the library

//        Glide.with(this).load(currentUser.getPhotoUrl()).into(photo);
//        Toast.makeText(this, currentUser.getPhotoUrl()+"", Toast.LENGTH_SHORT).show();

//        Picasso.get().load(model.getImage()).into(holder.imageView);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int posisition, @NonNull Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Harga = " + "Rp." + model.getPrice());
                        if (namaMitra.equals(model.getUsaha()))
                            Picasso.get().load(model.getImage()).into(photo);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PenjahitDetail.this, ProductDetailActivity.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("idMitra", idMitra);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
//            recyclerView.setAdapter(adapter);
//            adapter.startListening();

        buat_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PenjahitDetail.this, BuatBaruActivity.class);
                //intent.putExtra("pid", model.getPid());
                intent.putExtra("idMitra", idMitra);
                intent.putExtra("category", "Buat Baru");
                startActivity(intent);
                //startActivity(new Intent(PenjahitDetail.this, BuatBaruActivity.class));
            }
        });

        vermak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PenjahitDetail.this, BuatBaruActivity.class);
                //intent.putExtra("pid", model.getPid());
                intent.putExtra("idMitra", idMitra);
                intent.putExtra("category", "Vermak");
                startActivity(intent);
                //startActivity(new Intent(PenjahitDetail.this, BuatBaruActivity.class));
            }
        });

    }


}

