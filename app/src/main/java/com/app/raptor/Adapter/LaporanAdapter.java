package com.app.raptor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.raptor.Models.Laporan;
import com.app.raptor.R;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<Laporan> list;

    public LaporanAdapter(Context context, ArrayList<Laporan> list) {
        this.context = context;
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LaporanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanAdapter.MyViewHolder holder, int pos) {
        Laporan laporan = list.get(pos);

        holder.tvTgl.setText(laporan.getTanggal());
        holder.tvAgenda.setText(laporan.getAgenda());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTgl, tvAgenda;
        public MyViewHolder(@NonNull View v) {
            super(v);

            tvTgl = v.findViewById(R.id.tvTgl);
            tvAgenda = v.findViewById(R.id.tvAgenda);
        }
    }
}
