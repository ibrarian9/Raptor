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

public class HasilLaporanAdapter extends RecyclerView.Adapter<HasilLaporanAdapter.MyViewHolder> {

    Context context;
    private ArrayList<Laporan> list;

    public HasilLaporanAdapter(Context context, ArrayList<Laporan> list) {
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
    public HasilLaporanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_hasil, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HasilLaporanAdapter.MyViewHolder holder, int position) {
        Laporan laporan = list.get(position);

        holder.no.setText(String.valueOf(position + 1));
        holder.agenda.setText(laporan.getAgenda());
        holder.tanggal.setText(laporan.getTanggal());
        holder.waktu.setText(laporan.getWaktu());
        holder.lokasi.setText(laporan.getLokasi());
        holder.narasumber.setText(laporan.getNarasumber());
        holder.kegiatan.setText(laporan.getKegiatan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView agenda, tanggal, waktu, lokasi, narasumber, kegiatan, no;

        public MyViewHolder(@NonNull View it) {
            super(it);

            agenda = it.findViewById(R.id.agenda);
            tanggal = it.findViewById(R.id.tanggal);
            waktu = it.findViewById(R.id.waktu);
            lokasi = it.findViewById(R.id.lokasi);
            narasumber = it.findViewById(R.id.narasumber);
            kegiatan = it.findViewById(R.id.kegiatan);
            no = it.findViewById(R.id.no);
        }
    }
}
