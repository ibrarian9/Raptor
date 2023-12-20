package com.app.raptor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView botNavbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        botNavbar = findViewById(R.id.bottomNavigationView);

        botNavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, ListLaporanActivity.class));
                return true;
            } else if (id == R.id.agenda) {
                startActivity(new Intent(this, HasilLaporanActivity.class));
                return true;
            } else if (id == R.id.profil) {
                return true;
            }
            return false;
        });
    }
}