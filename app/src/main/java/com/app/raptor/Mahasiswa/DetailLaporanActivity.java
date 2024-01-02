package com.app.raptor.Mahasiswa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Models.Laporan;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DetailLaporanActivity extends AppCompatActivity {

    TextView agenda, waktu, lokasi, narasumber, kegiatan, edit, saran;
    String dataAgenda, dataWaktu, dataLokasi, dataNarasumber, dataKegiatan, dataTanggal, uid, dataFoto, dataSaran;
    ExtendedFloatingActionButton back;
    BottomNavigationView botnav;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private int jam, menit;
    Dialog d;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null ){
            uid = mUser.getUid();
        }

        //  Define Id
        agenda = findViewById(R.id.agenda);
        waktu = findViewById(R.id.waktu);
        lokasi = findViewById(R.id.lokasi);
        narasumber = findViewById(R.id.narasumber);
        kegiatan = findViewById(R.id.kegiatan);
        back = findViewById(R.id.extendFab);
        edit = findViewById(R.id.tvEdit);
        saran = findViewById(R.id.edSaran);
        botnav = findViewById(R.id.botNavbar);

        //  Get Data From Intent
        dataAgenda = getIntent().getStringExtra("agenda");
        dataWaktu = getIntent().getStringExtra("waktu");
        dataLokasi = getIntent().getStringExtra("lokasi");
        dataNarasumber = getIntent().getStringExtra("narasumber");
        dataKegiatan = getIntent().getStringExtra("kegiatan");
        dataTanggal = getIntent().getStringExtra("tanggal");
        dataFoto = getIntent().getStringExtra("foto");
        dataSaran = getIntent().getStringExtra("saran");

        agenda.setText(dataAgenda);
        waktu.setText(dataWaktu);
        lokasi.setText(dataLokasi);
        narasumber.setText(dataNarasumber);
        kegiatan.setText(dataKegiatan);
        if (!dataSaran.isEmpty()){
            saran.setText(dataSaran);
        } else {
            saran.setText("Kotak saran masih kosong...");
        }


        d = new Dialog(DetailLaporanActivity.this);
        d.setContentView(R.layout.dialog_laporan);
        d.setCancelable(false);
        dialogFull(d);

        edit.setOnClickListener( v -> {

            TextView batal, okay, edWaktu;
            EditText edAgenda, edLokasi, edNara, edKegiatan;

            // Define Id
            edAgenda = d.findViewById(R.id.agenda);
            edWaktu = d.findViewById(R.id.waktu);
            edLokasi = d.findViewById(R.id.lokasi);
            edNara = d.findViewById(R.id.narasumber);
            edKegiatan = d.findViewById(R.id.kegiatan);

            // Set Current Data
            edAgenda.setText(dataAgenda);
            edWaktu.setText(dataWaktu);
            edLokasi.setText(dataLokasi);
            edNara.setText(dataNarasumber);
            edKegiatan.setText(dataKegiatan);

            edWaktu.setOnClickListener(v3 -> {
                final Calendar calendar = Calendar.getInstance();
                jam = calendar.get( Calendar.HOUR );
                menit = calendar.get( Calendar.MINUTE );

                @SuppressLint("SetTextI18n")
                TimePickerDialog timePick = new TimePickerDialog(this, (view, hourOfDay, minute)
                        -> edWaktu.setText( hourOfDay + ":" + minute ), jam, menit, false);
                timePick.show();
                timePick.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.blue_900));
                timePick.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.blue_900));
            });

            // Button Handle
            batal = d.findViewById(R.id.batal);
            batal.setOnClickListener( v1 -> d.dismiss());

            okay = d.findViewById(R.id.simpan);
            okay.setOnClickListener( v2 -> {
                String sAgenda = edAgenda.getText().toString();
                String sWaktu = edWaktu.getText().toString();
                String sLokasi = edLokasi.getText().toString();
                String sNara = edNara.getText().toString();
                String sKegiatan = edKegiatan.getText().toString();

                if (TextUtils.isEmpty(sAgenda)){
                    Toast.makeText(this, "Agenda masih kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sWaktu)){
                    Toast.makeText(this, "Waktu masih kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sLokasi)){
                    Toast.makeText(this, "Lokasi masih kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sNara)){
                    Toast.makeText(this, "Narasumber masih kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sKegiatan)){
                    Toast.makeText(this, "Kegiatan masih kosong...", Toast.LENGTH_SHORT).show();
                } else {
                    Laporan laporan = new Laporan(dataTanggal, sAgenda, sWaktu, sLokasi, sNara, sKegiatan, dataFoto, dataSaran);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Laporan").child(uid).child(dataTanggal);
                    ref.setValue(laporan).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, "Laporan Telah di Update...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, ListLaporanActivity.class));
                            finish();
                            d.dismiss();
                        } else {
                            Toast.makeText(this, "Laporan Gagal dalam mengupdate...", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        }
                    });
                    d.dismiss();
                }
            });
            d.show();
        });
        back.setOnClickListener( v -> getOnBackPressedDispatcher().onBackPressed());

        botnav.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, ListLaporanActivity.class));
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

    private void dialogFull(Dialog d) {
        if (d.getWindow() != null){
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}