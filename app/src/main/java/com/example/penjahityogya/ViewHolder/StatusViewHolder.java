package com.example.penjahityogya.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penjahityogya.Interface.ItemClickListener;
import com.example.penjahityogya.R;

public class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtBarang, txtTotal, txtStatus;
    private ItemClickListener itemClickListener;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);

        txtBarang = itemView.findViewById(R.id.Pem_barang);
        txtTotal = itemView.findViewById(R.id.Pem_total);
        txtStatus = itemView.findViewById(R.id.Pem_status);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
