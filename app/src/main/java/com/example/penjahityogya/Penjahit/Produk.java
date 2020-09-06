package com.example.penjahityogya.Penjahit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.penjahityogya.R;

public class Produk extends AppCompatActivity {

    ListView listView;
    String mTitle[]= {"CELANA", "KEMEJA", "BATIK", "BLAZER"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);

        listView = findViewById(R.id.listview);

        MyAdapter adapter = new MyAdapter( this, mTitle);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(Produk.this, TambahProdukMitra.class);
                    intent.putExtra("category", "CELANA");
                    startActivity(intent);
                }
                if (position == 1){
                    Intent intent = new Intent(Produk.this, TambahProdukMitra.class);
                    intent.putExtra("category", "KEMEJA");
                    startActivity(intent);
                }
                if (position == 2){
                    Intent intent = new Intent(Produk.this, TambahProdukMitra.class);
                    intent.putExtra("category", "BATIK");
                    startActivity(intent);
                }
                if (position == 3){
                    Intent intent = new Intent(Produk.this, TambahProdukMitra.class);
                    intent.putExtra("category", "BLAZER");
                    startActivity(intent);
                }
            }
        });
    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String[] rTitle;

        MyAdapter (Context c, String title[]){
            super(c, R.layout.row, R.id.title, title);
            this.context = c;
            this.rTitle = title;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle = row.findViewById(R.id.title);

            myTitle.setText(rTitle[position]);

            return row;
        }
    }
}
