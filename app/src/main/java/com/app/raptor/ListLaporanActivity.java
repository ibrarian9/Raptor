package com.app.raptor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListLaporanActivity extends AppCompatActivity {

    FloatingActionButton plusBtn;
    CalendarView calender;
    BottomNavigationView botNavbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laporan);

        plusBtn = findViewById(R.id.fabAdd);
        calender = findViewById(R.id.cv);
        botNavbar = findViewById(R.id.bottomNavigationView);
        calender.setVisibility(View.GONE);
        System.out.println(calender.getVisibility());

        if (calender.getVisibility() == View.VISIBLE){
            plusBtn.setOnClickListener( v -> calender.setVisibility(View.GONE));
        } else if (calender.getVisibility() == View.GONE){
            plusBtn.setOnClickListener( v -> calender.setVisibility(View.VISIBLE));
        }

        System.out.println(calender.getVisibility());

        botNavbar.setOnItemSelectedListener( i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                return true;
            } else if (id == R.id.cari) {
                startActivity(new Intent(this, ListLaporanActivity.class));
                return true;
            } else if (id == R.id.agenda) {
                return true;
            } else if (id == R.id.profil) {
                return true;
            }
            return false;
        });

    }
}