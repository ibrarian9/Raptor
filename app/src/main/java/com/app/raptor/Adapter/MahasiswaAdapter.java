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

import com.app.raptor.Dospem.LaporanDetailDospemActivity;
import com.app.raptor.Models.UserDetails;
import com.app.raptor.R;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<UserDetails> list;

    public MahasiswaAdapter(Context context, ArrayList<UserDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MahasiswaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_mahasiswa, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.MyViewHolder holder, int position) {
        UserDetails data = list.get(position);

        holder.nama.setText(data.getNama());
        holder.itemView.setOnClickListener( v -> {
            Intent i = new Intent(context, LaporanDetailDospemActivity.class);
            i.putExtra("nama", data.getNama());
            i.putExtra("nim", data.getNim());
            i.putExtra("uid", data.getUid());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        public MyViewHolder(@NonNull View v) {
            super(v);

            nama = v.findViewById(R.id.nama);
        }
    }
}
