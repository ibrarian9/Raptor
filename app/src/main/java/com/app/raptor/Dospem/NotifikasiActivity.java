package com.app.raptor.Dospem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotifikasiActivity extends AppCompatActivity {

    BottomNavigationView botnav;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        botnav = findViewById(R.id.botNavbar);
        back = findViewById(R.id.back);

        back.setOnClickListener( v -> getOnBackPressedDispatcher().onBackPressed());

        botnav.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, DospemActivityHome.class));
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