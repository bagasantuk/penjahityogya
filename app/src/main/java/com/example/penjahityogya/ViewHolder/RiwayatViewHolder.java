package com.example.penjahityogya.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penjahityogya.Interface.ItemClickListener;
import com.example.penjahityogya.R;

public class RiwayatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtBarang, txtTotal, txtStatus,txtCategory,txtTanggal,txtJumlah;
    private ItemClickListener itemClickListener;
    public RiwayatViewHolder(@NonNull View itemView) {
        super(itemView);
        txtBarang = itemView.findViewById(R.id.riwayat_barang);
        txtTotal = itemView.findViewById(R.id.riwayat_total);
        txtStatus = itemView.findViewById(R.id.riwayat_status);
        txtCategory = itemView.findViewById(R.id.riwayat_category);
        txtTanggal = itemView.findViewById(R.id.riwayat_tanggal);
        txtJumlah = itemView.findViewById(R.id.riwayat_quantity);
    }

    @Override
    public void onClick(View view) {

    }
}
