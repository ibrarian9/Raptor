package com.app.raptor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.raptor.Dospem.LaporanDospemActivity;
import com.app.raptor.Models.Laporan;
import com.app.raptor.R;

import java.util.ArrayList;

public class LaporanDosenAdapter extends RecyclerView.Adapter<LaporanDosenAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<Laporan> list;

    public LaporanDosenAdapter(Context context, ArrayList<Laporan> list) {
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
    public LaporanDosenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_dosen, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanDosenAdapter.MyViewHolder holder, int pos) {
        Laporan laporan = list.get(pos);

        holder.tvTgl.setText(laporan.getTanggal());
        holder.tvAgenda.setText(laporan.getAgenda());
        holder.itemView.setOnClickListener( v -> {
            Intent i = new Intent(context, LaporanDospemActivity.class);
            i.putExtra("agenda", laporan.getAgenda());
            i.putExtra("waktu", laporan.getWaktu());
            i.putExtra("lokasi", laporan.getLokasi());
            i.putExtra("narasumber", laporan.getNarasumber());
            i.putExtra("kegiatan", laporan.getKegiatan());
            i.putExtra("tanggal", laporan.getTanggal());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Laporan> data){
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
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
