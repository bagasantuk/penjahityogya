package com.example.penjahityogya.ViewHolder;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penjahityogya.Interface.ItemClickListener;
import com.example.penjahityogya.R;

public class MitraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtNamaMitra, txtJam, txtAlamat, txtJarak, txtNotelp;
    public ImageView imageView;
    public ItemClickListener listener;

    public MitraViewHolder(@NonNull View itemView)
    {
        super(itemView);

//        imageView = (ImageView) itemView.findViewById(R.id.detailMitraimageView);
        txtNamaMitra = (TextView) itemView.findViewById(R.id.list_penjahit);
        txtNotelp = (TextView) itemView.findViewById(R.id.list_notelp);
        txtJam = (TextView) itemView.findViewById(R.id.list_jam);
        txtAlamat = (TextView) itemView.findViewById(R.id.list_alamat);
        txtJarak = (TextView) itemView.findViewById(R.id.list_jarak);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
