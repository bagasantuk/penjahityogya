package com.example.penjahityogya.activities;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.penjahityogya.Prevalent.Prevalent;
import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.CartViewHolder;
import com.example.penjahityogya.models.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements Serializable {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference cartListRef;
    private int overTotalPrice = 0;
    private SessionManager sessionManager;
    DatabaseReference reference;
    String latitude, longitude, alamat, telp, nama,idMitra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        sessionManager = new SessionManager(CartActivity.this, SessionManager.SESSION_USER); //deklarasi sessionManager NB: SESSION_USER sesi untuk user, SESSION_JASA sesi untuk jasa

        // cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(currentUser.getUid()).child("Products");


    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent in = getIntent();
        idMitra = in.getStringExtra("idMitra");

        HashMap<String, String> usersDetails = sessionManager.getidMitraFromSession(); //deklarasi HashMap ambil data dari sessionManager
        idMitra = usersDetails.get(SessionManager.KEY_IDMITRA); // contoh ambil data dari HashMap

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(currentUser.getUid())
                                .child("Products").child(idMitra), Cart.class)
                        .build();
//         FirebaseRecyclerOptions<Cart> options =
//                 new FirebaseRecyclerOptions.Builder<Cart>()
//                 .setQuery(cartListRef, Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            int oneTypeProductPrice;

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int posisition, @NonNull Cart model) {
                int hargabahan = (Integer.valueOf(model.getQuantity()) * Integer.valueOf(model.getHargabahan()));
                holder.txtProductQuantity.setText("= " + model.getQuantity());
                holder.txtProductPrice.setText("Harga = Rp. " + model.getPrice() + "+" + model.getHargabahan());
                holder.txtProductName.setText(model.getPname());
//menampilkan total price

                if (model.getPrice() != null)
                    oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice + hargabahan;
                txtTotalAmount.setText("Total = Rp. " + String.valueOf(overTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("idMitra", model.getMitraId());
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("category", model.getCategory());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    cartListRef.child("User View")
                                            .child(currentUser.getUid())
                                            .child("Products")
                                            .child(model.getMitraId())
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item Removed Successfully...", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, Home.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                }

                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference().child("Data").child(currentUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        latitude = dataSnapshot.child("latitude").getValue().toString();
                        longitude = dataSnapshot.child("longitude").getValue().toString();
                        alamat = dataSnapshot.child("alamat").getValue().toString();

                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                telp = dataSnapshot.child("phoneNo").getValue().toString();
                                nama = dataSnapshot.child("name").getValue().toString();

                                Intent intent = new Intent(CartActivity.this, Pemesanan.class);
                                intent.putExtra("latitude", latitude);
                                intent.putExtra("longitude", longitude);
                                intent.putExtra("alamat", alamat);
                                intent.putExtra("nama", nama);
                                intent.putExtra("telp", telp);
                                intent.putExtra("idMitra", idMitra);
                                overTotalPrice = 0;
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //startActivity(new Intent(PenjahitDetail.this, BuatBaruActivity.class));
            }
        });


    }


}
