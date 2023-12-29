package com.app.raptor.Dospem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.raptor.Adapter.LaporanDosenAdapter;
import com.app.raptor.Models.Laporan;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaporanDetailDospemActivity extends AppCompatActivity {

    RecyclerView rv;
    TextView nama, nim;
    DatabaseReference ref;
    BottomNavigationView botnav;
    LaporanDosenAdapter adapter;
    ArrayList<Laporan> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail_dospem);

        nama = findViewById(R.id.nama);
        nim = findViewById(R.id.nim);
        botnav = findViewById(R.id.botNavbar);
        adapter = new LaporanDosenAdapter(this, list);
        rv = findViewById(R.id.rv);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        String dataNama = getIntent().getStringExtra("nama");
        String dataNim = getIntent().getStringExtra("nim");
        String dataUid = getIntent().getStringExtra("uid");
        if (dataUid != null){
            ref = FirebaseDatabase.getInstance().getReference("Laporan").child(dataUid);
        }

        //  set Nama and nim
        nama.setText(dataNama);
        nim.setText(dataNim);
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Laporan lapor = snapshot1.getValue(Laporan.class);
                    list.add(lapor);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botnav.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, DospemActivityHome.class));
                return true;
            } else if (id == R.id.notif) {
                startActivity(new Intent(this, NotifikasiActivity.class));
                return true;
            } else if (id == R.id.profil) {
                startActivity(new Intent(this, ProfileDospemActivity.class));
                return true;
            }
            return false;
        });
    }
}