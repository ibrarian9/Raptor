package com.app.raptor.Mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Adapter.LaporanAdapter;
import com.app.raptor.Models.Laporan;
import com.app.raptor.Models.UserDetails;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ListLaporanActivity extends AppCompatActivity {

    FloatingActionButton plusBtn;
    BottomNavigationView botNavbar;
    TextView okay, nama, nim;
    RecyclerView rv;
    ImageView close;
    CalendarView calender;
    FirebaseAuth mAuth;
    FirebaseUser user;
    LaporanAdapter laporanAdapter;
    String hari, bulan, tgl, tahun, uid, checkTgl;
    ArrayList<Laporan> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laporan);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();

        nama = findViewById(R.id.nama);
        nim = findViewById(R.id.nim);
        plusBtn = findViewById(R.id.fabAdd);
        botNavbar = findViewById(R.id.bottomNavigationView);

        laporanAdapter = new LaporanAdapter(this, list);
        rv = findViewById(R.id.rv);
        rv.setAdapter(laporanAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                    String sNama = uDetail.getNama();
                    String sNim = uDetail.getNim();
                    nama.setText(sNama);
                    nim.setText(sNim);
                    checkTgl = uDetail.getTglMulai();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query listLaporan = FirebaseDatabase.getInstance().getReference("Laporan").child(uid);
        listLaporan.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                laporanAdapter.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Laporan lapor = snapshot1.getValue(Laporan.class);
                    list.add(lapor);
                }

                laporanAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Dialog dialog = new Dialog(this);
        plusBtn.setOnClickListener(v -> {
            dialog.setContentView(R.layout.dialog_calender);
            dialog.setCancelable(false);

            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(v1 -> dialog.dismiss());

            calender = dialog.findViewById(R.id.cv);
            calender.setOnDateChangeListener((view, year, month, day) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                int bul = month + 1;

                tgl = String.valueOf(day);
                tahun = String.valueOf(year);

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

                //  Define Month
                if (bul == 1){
                    bulan = "Januari";
                } else if (bul == 2) {
                    bulan = "Februari";
                } else if (bul == 3) {
                    bulan = "Maret";
                } else if (bul == 4) {
                    bulan = "April";
                } else if (bul == 5) {
                    bulan = "Mei";
                } else if (bul == 6) {
                    bulan = "Juni";
                } else if (bul == 7) {
                    bulan = "Juli";
                } else if (bul == 8) {
                    bulan = "Agustus";
                } else if (bul == 9) {
                    bulan = "September";
                } else if (bul == 10) {
                    bulan = "Oktober";
                } else if (bul == 11) {
                    bulan = "November";
                } else if (bul == 12) {
                    bulan = "Desember";
                }

                System.out.println(bulan);
                System.out.println(hari);
            });

            okay = dialog.findViewById(R.id.ok);
            okay.setOnClickListener(vi -> {
                String tanggal = hari + ", " + tgl + " " + bulan + " " + tahun;
                if (tanggal.contains("null")){
                    Toast.makeText(this, "Tanggal Belum di Isi...", Toast.LENGTH_SHORT).show();
                } else {

                    if (checkTgl == null || checkTgl.isEmpty()){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                        reference.child("tglMulai").setValue(tanggal);
                    }

                    Laporan laporan = new Laporan(tanggal,"","","","","","");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Laporan").child(uid);
                    databaseReference.child(tanggal).setValue(laporan).addOnCompleteListener( v1 -> {
                        Intent i = new Intent(this, FormLaporanActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        i.putExtra("date", tanggal);
                        startActivity(i);
                        finish();
                    });
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        botNavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
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