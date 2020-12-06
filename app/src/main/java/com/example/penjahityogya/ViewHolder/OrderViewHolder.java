package com.example.penjahityogya.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penjahityogya.Interface.ItemClickListener;
import com.example.penjahityogya.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtNamaPemesan, txtQuantity, txtTotal, txtnama;
    public ItemClickListener listener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtNamaPemesan = (TextView) itemView.findViewById(R.id.order_nama);
        txtnama = (TextView) itemView.findViewById(R.id.ukuran_nama);
        txtTotal = (TextView) itemView.findViewById(R.id.order_total);
        txtQuantity  = (TextView) itemView.findViewById(R.id.order_quantity);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

    }
}
