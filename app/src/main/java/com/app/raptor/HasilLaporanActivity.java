package com.app.raptor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class HasilLaporanActivity extends AppCompatActivity {

    BottomNavigationView botNavbar;
    ExtendedFloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_laporan);

        fab = findViewById(R.id.extendFab);

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