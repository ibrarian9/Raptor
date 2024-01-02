package com.app.raptor.Koordinator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class KoordinatorActivityHome extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fUser;
    String uid;
    TextView nDosen, nNip;
    BottomNavigationView botnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koordinator_home);

        //   Get Id
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        assert fUser != null;
        uid = fUser.getUid();

        nDosen = findViewById(R.id.nama);
        nNip = findViewById(R.id.nip);
        botnav = findViewById(R.id.botNavbar);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nama = snapshot.child("nama").getValue(String.class);
                    String nip = snapshot.child("nip").getValue(String.class);
                    nDosen.setText(nama);
                    nNip.setText(nip);
                } else {
                    Toast.makeText(getApplicationContext(), "Data user tidak ditemukan...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botnav.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                return true;
            } else if (id == R.id.notif) {
                return true;
            } else if (id == R.id.profil){
                startActivity(new Intent(this, ProfileKoordinatorActivity.class));
                return true;
            }
            return false;
        });
    }
}