package com.app.raptor.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.raptor.Mahasiswa.DetailLaporanActivity;
import com.app.raptor.Models.Laporan;
import com.app.raptor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.MyViewHolder> {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String uid;
    Context context;
    private ArrayList<Laporan> list;

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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if ( mUser != null){
            uid = mUser.getUid();
        }

        holder.tvTgl.setText(laporan.getTanggal());
        holder.tvAgenda.setText(laporan.getAgenda());
        holder.del.setOnClickListener( v -> {
            Dialog d = new Dialog(v.getContext());
            d.setContentView(R.layout.dialog_hapus);
            d.setCancelable(false);
            dialogFull(d);

            TextView no = d.findViewById(R.id.no);
            no.setOnClickListener( v1 -> d.dismiss());

            TextView yes = d.findViewById(R.id.yes);
            yes.setOnClickListener( v2 -> {
                DatabaseReference m = FirebaseDatabase.getInstance().getReference("Laporan").child(uid);
                m.child(laporan.getTanggal()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(context, "Laporan Berhasil dihapus...", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });
                d.dismiss();
            });
            d.show();
        });
        holder.ll.setOnClickListener( v -> {
            Intent i = new Intent(context, DetailLaporanActivity.class);
            i.putExtra("agenda", laporan.getAgenda());
            i.putExtra("waktu", laporan.getWaktu());
            i.putExtra("lokasi", laporan.getLokasi());
            i.putExtra("narasumber", laporan.getNarasumber());
            i.putExtra("kegiatan", laporan.getKegiatan());
            i.putExtra("tanggal", laporan.getTanggal());
            i.putExtra("foto", laporan.getFoto());
            i.putExtra("saran", laporan.getSaran());
            context.startActivity(i);
        });
    }

    private void dialogFull(Dialog d) {
        if (d.getWindow() != null){
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Laporan> data) {
        list = data;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Laporan> data){
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTgl, tvAgenda;
        ImageView del;
        LinearLayout ll;
        public MyViewHolder(@NonNull View v) {
            super(v);

            tvTgl = v.findViewById(R.id.tvTgl);
            tvAgenda = v.findViewById(R.id.tvAgenda);
            del = v.findViewById(R.id.delete);
            ll = v.findViewById(R.id.ll);
        }
    }
}
