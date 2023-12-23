package com.app.raptor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.raptor.Models.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView botNavbar;
    TextView nama, nim, edNama, edEmail, edJudulkp, semester, tanggal, dospem, pembimbing;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nama = findViewById(R.id.tvNama);
        nim = findViewById(R.id.tvNim);
        botNavbar = findViewById(R.id.bottomNavigationView);

        edNama = findViewById(R.id.nama);
        edEmail = findViewById(R.id.email);
        edJudulkp = findViewById(R.id.judulkp);
        semester = findViewById(R.id.semester);
        tanggal = findViewById(R.id.tglmulai);
        dospem = findViewById(R.id.dospem);
        pembimbing = findViewById(R.id.instansi);

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
                    String sEmail = uDetail.getEmail();
                    String sJudul = uDetail.getJudulkp();
                    String sDospem = uDetail.getDospem();
                    String sPembimbing = uDetail.getPembimbing();

                    nama.setText(sNama);
                    nim.setText(sNim);

                    //  Show Data Profile
                    edNama.setText(sNama);
                    edEmail.setText(sEmail);
                    edJudulkp.setText(sJudul);
                    dospem.setText(sDospem);
                    pembimbing.setText(sPembimbing);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botNavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, ListLaporanActivity.class));
                return true;
            } else if (id == R.id.agenda) {
                startActivity(new Intent(this, HasilLaporanActivity.class));
                return true;
            } else return id == R.id.profil;
        });
    }
}