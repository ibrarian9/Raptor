package com.app.raptor.Mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Models.Laporan;
import com.app.raptor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormLaporanActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fUser;
    String uid, agenda, waktu, lokasi, narasumber, kegiatan, dataTanggal;
    TextView tanggal, tvSubmit;
    EditText edAgenda, edWaktu, edLokasi, edNarasumber, edKegiatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_laporan);

        tanggal = findViewById(R.id.tvTanggal);
        edAgenda = findViewById(R.id.edAgenda);
        edWaktu = findViewById(R.id.edWaktu);
        edLokasi = findViewById(R.id.edLokasi);
        edNarasumber = findViewById(R.id.edNarasumber);
        edKegiatan = findViewById(R.id.edKegiatan);
        tvSubmit = findViewById(R.id.tvSimpan);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        assert fUser != null;
        uid = fUser.getUid();
        dataTanggal = getIntent().getStringExtra("date");
        assert dataTanggal != null;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Laporan").child(uid).child(dataTanggal);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Laporan laporan = snapshot.getValue(Laporan.class);
                if (laporan != null){
                    String tgl = laporan.getTanggal();
                    tanggal.setText(tgl);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvSubmit.setOnClickListener( v -> {
            agenda = edAgenda.getText().toString();
            waktu = edWaktu.getText().toString();
            lokasi = edLokasi.getText().toString();
            narasumber = edNarasumber.getText().toString();
            kegiatan = edKegiatan.getText().toString();

            if (TextUtils.isEmpty(agenda)){
                Toast.makeText(this, "Agenda Belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(waktu)){
                Toast.makeText(this, "Waktu Belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(lokasi)){
                Toast.makeText(this, "Lokasi Belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(narasumber)){
                Toast.makeText(this, "Narasumber Belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(kegiatan)){
                Toast.makeText(this, "Kegiatan Belum diisi...", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Laporan").child(uid).child(dataTanggal);
                dRef.child("agenda").setValue(agenda);
                dRef.child("waktu").setValue(waktu);
                dRef.child("lokasi").setValue(lokasi);
                dRef.child("narasumber").setValue(narasumber);
                dRef.child("kegiatan").setValue(kegiatan);
                Intent i = new Intent(this, ListLaporanActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(i);
                finish();
            }
        });


    }
}