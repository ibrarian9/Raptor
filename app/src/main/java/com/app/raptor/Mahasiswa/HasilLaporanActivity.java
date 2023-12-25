package com.app.raptor.Mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

public class HasilLaporanActivity extends AppCompatActivity {

    BottomNavigationView botNavbar;
    ExtendedFloatingActionButton fab;
    TextView judulkp, nama, nim, tgl, dospem, pembimbing;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;

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

        //  Get Current User Data
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();

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

                    nama.setText(sNama);
                    nim.setText(sNim);
                    judulkp.setText(sJudul);
                    dospem.setText(sDospem);
                    pembimbing.setText(sPembimbing);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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