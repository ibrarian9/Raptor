package com.app.raptor.Mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.raptor.Adapter.HasilLaporanAdapter;
import com.app.raptor.Models.Laporan;
import com.app.raptor.Models.UserDetails;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HasilLaporanActivity extends AppCompatActivity {

    BottomNavigationView botNavbar;
    ExtendedFloatingActionButton fab;
    RecyclerView rv;
    TextView judulkp, nama, nim, tgl, dospem, pembimbing, print;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    HasilLaporanAdapter adapter;
    ArrayList<Laporan> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_laporan);

        fab = findViewById(R.id.extendFab);
        judulkp = findViewById(R.id.judul);
        nama = findViewById(R.id.nama);
        nim = findViewById(R.id.nim);
        tgl = findViewById(R.id.tglmulai);
        dospem = findViewById(R.id.dospem);
        pembimbing = findViewById(R.id.pembimbing);
        print = findViewById(R.id.print);
        rv = findViewById(R.id.rv);

        //  Get Current User Data
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();

        //   Set Adapter
        adapter = new HasilLaporanAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                    String sNama = uDetail.getNama();
                    String sNim = uDetail.getNim();
                    String sJudul = uDetail.getJudulkp();
                    String sDospem = uDetail.getDospem();
                    String sPembimbing = uDetail.getPembimbing();
                    String sTglMulai = uDetail.getTglMulai();

                    nama.setText(sNama);
                    nim.setText(sNim);
                    judulkp.setText(sJudul);
                    dospem.setText(sDospem);
                    pembimbing.setText(sPembimbing);
                    tgl.setText(sTglMulai);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Laporan").child(uid);
        data.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();

                for (DataSnapshot snap: snapshot.getChildren()){
                    Laporan lapor = snap.getValue(Laporan.class);
                    list.add(lapor);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        print.setOnClickListener( v -> startActivity(new Intent(this, LaporanFile.class)));

        botNavbar = findViewById(R.id.botNavbar);
        botNavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, ListLaporanActivity.class));
                return true;
            } else if (id == R.id.agenda) {
                return true;
            } else if (id == R.id.profil) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

}