package com.app.raptor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ListLaporanActivity extends AppCompatActivity {

    FloatingActionButton plusBtn;
    BottomNavigationView botNavbar;
    TextView okay;
    ImageView close;
    CalendarView calender;
    String hari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laporan);

        plusBtn = findViewById(R.id.fabAdd);
        botNavbar = findViewById(R.id.bottomNavigationView);

        Dialog dialog = new Dialog(this);
        plusBtn.setOnClickListener(v -> {
            dialog.setContentView(R.layout.dialog_calender);
            dialog.setCancelable(false);

            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(v1 -> {
                dialog.dismiss();
            } );

            calender = dialog.findViewById(R.id.cv);
            calender.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int days = calendar.get(Calendar.DAY_OF_WEEK);
                if (days == 1){
                    hari = "Minggu";
                } else if (days == 2) {
                    hari = "Senin";
                } else if (days == 3) {
                    hari = "Selasa";
                } else if (days == 4) {
                    hari = "Rabu";
                } else if (days == 5) {
                    hari = "Kamis";
                } else if (days == 6) {
                    hari = "Jum'at";
                } else if (days == 7) {
                    hari = "Sabtu";
                }
                System.out.println(hari);
            });

            okay = dialog.findViewById(R.id.ok);
            okay.setOnClickListener(vi -> {
                dialog.dismiss();
            });
            dialog.show();
        });

        botNavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                return true;
            } else if (id == R.id.cari) {
                return true;
            } else if (id == R.id.agenda) {
                startActivity(new Intent(this, HasilLaporanActivity.class));
                return true;
            } else if (id == R.id.profil) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}