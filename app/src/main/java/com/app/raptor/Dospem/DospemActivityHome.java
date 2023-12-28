package com.app.raptor.Dospem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Mahasiswa.HasilLaporanActivity;
import com.app.raptor.Mahasiswa.ProfileActivity;
import com.app.raptor.Models.Dosen;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DospemActivityHome extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fUser;
    TextView tvNama, tvNim;
    String uid;
    BottomNavigationView botnavbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dospem_home);

        //   Get Id
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        assert fUser != null;
        uid = fUser.getUid();

        //   Define Id
        tvNama = findViewById(R.id.nama);
        tvNim = findViewById(R.id.nim);
        botnavbar = findViewById(R.id.botNavbar);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Dosen dosen = snapshot.getValue(Dosen.class);
                    if (dosen != null){
                        tvNama.setText(dosen.getNama());
                        if (!dosen.getNip().isEmpty()){
                            tvNim.setText(dosen.getNip());
                        } else {
                            tvNim.setText("NIP belum Diisi...");
                        }
                    }
                } else {
                    Toast.makeText(DospemActivityHome.this, "Data user tidak ditemukan...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botnavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                return true;
            } else if (id == R.id.notif) {
                return true;
            } else if (id == R.id.profil) {
                startActivity(new Intent(this, ProfileDospemActivity.class));
                return true;
            }
            return false;
        });

    }
}