package com.app.raptor.Dospem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Models.Laporan;
import com.app.raptor.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class LaporanDospemActivity extends AppCompatActivity {

    EditText agenda, waktu;
    String uid;
    ImageView back, poto;
    TextView tvTanggal, edit, saran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_dospem);

        //  Define id
        agenda = findViewById(R.id.edAgenda);
        waktu = findViewById(R.id.edWaktu);
        tvTanggal = findViewById(R.id.tvTanggal);
        back = findViewById(R.id.back);
        poto = findViewById(R.id.ivPoto);
        edit = findViewById(R.id.edit);
        saran = findViewById(R.id.edSaran);

        //  Get Data From Intent
        String dataAgenda = getIntent().getStringExtra("agenda");
        String dataWaktu = getIntent().getStringExtra("waktu");
        String dataTanggal = getIntent().getStringExtra("tanggal");
        String dataPoto = getIntent().getStringExtra("foto");
        String dataLok = getIntent().getStringExtra("lokasi");
        String dataKegiatan = getIntent().getStringExtra("kegiatan");
        String dataNara = getIntent().getStringExtra("narasumber");
        String dataSaran = getIntent().getStringExtra("saran");
        uid = getIntent().getStringExtra("uid");

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_saran);
        d.setCancelable(false);
        dialogFull(d);
        edit.setOnClickListener(v -> {
            TextView batal = d.findViewById(R.id.batal);
            batal.setOnClickListener(v1 -> d.dismiss());

            TextView simpan = d.findViewById(R.id.simpan);
            simpan.setOnClickListener(v2 -> {
                EditText edsaran = d.findViewById(R.id.edSaran);
                String saran = edsaran.getText().toString();

                if (saran.isEmpty()){
                    Toast.makeText(this, "Saran Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else {
                    Laporan laporan = new Laporan(dataTanggal, dataAgenda, dataWaktu, dataLok, dataNara, dataKegiatan, dataPoto, saran);

                    assert dataTanggal != null;
                    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Laporan").child(uid).child(dataTanggal);
                    dRef.setValue(laporan).addOnCompleteListener(task -> {

                        Toast.makeText(this, "Saran Berhasil Dikirim...", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, DospemActivityHome.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(i);
                        finish();
                    });
                }
            });
            d.show();
        });

        back.setOnClickListener( v -> getOnBackPressedDispatcher().onBackPressed());
        tvTanggal.setText(dataTanggal);
        saran.setText(dataSaran);
        agenda.setText(dataAgenda);
        agenda.setEnabled(false);
        waktu.setText(dataWaktu);
        waktu.setEnabled(false);
        if (dataPoto != null){
            if (!dataPoto.isEmpty()){
                Picasso.get().load(dataPoto).fit().into(poto);
            } else {
                poto.setImageResource(R.drawable.baseline_no_photo_24);
            }
        } else {
            poto.setImageResource(R.drawable.baseline_no_photo_24);
        }
    }

    private void dialogFull(Dialog d) {
        if (d.getWindow() != null){
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}