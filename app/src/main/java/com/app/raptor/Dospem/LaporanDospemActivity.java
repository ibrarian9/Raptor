package com.app.raptor.Dospem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.raptor.R;
import com.squareup.picasso.Picasso;

public class LaporanDospemActivity extends AppCompatActivity {

    EditText agenda, waktu;
    ImageView back, poto;
    TextView tvTanggal, edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_dospem);

        agenda = findViewById(R.id.edAgenda);
        waktu = findViewById(R.id.edWaktu);
        tvTanggal = findViewById(R.id.tvTanggal);
        back = findViewById(R.id.back);
        poto = findViewById(R.id.ivPoto);
        edit = findViewById(R.id.edit);

        String dataAgenda = getIntent().getStringExtra("agenda");
        String dataWaktu = getIntent().getStringExtra("waktu");
        String dataTanggal = getIntent().getStringExtra("tanggal");
        String dataPoto = getIntent().getStringExtra("foto");
        String dataLok = getIntent().getStringExtra("lokasi");
        String dataKegiatan = getIntent().getStringExtra("kegiatan");
        String dataNara = getIntent().getStringExtra("narasumber");

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_saran);
        d.setCancelable(false);
        dialogFull(d);
        edit.setOnClickListener(v -> {
            TextView batal = d.findViewById(R.id.batal);
            batal.setOnClickListener(v1 -> d.dismiss());
            d.show();
        });

        back.setOnClickListener( v -> getOnBackPressedDispatcher().onBackPressed());
        tvTanggal.setText(dataTanggal);
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