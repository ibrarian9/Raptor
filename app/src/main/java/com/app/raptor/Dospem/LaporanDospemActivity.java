package com.app.raptor.Dospem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.raptor.R;

public class LaporanDospemActivity extends AppCompatActivity {

    EditText agenda, waktu;
    ImageView back;
    TextView tvTanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_dospem);

        agenda = findViewById(R.id.edAgenda);
        waktu = findViewById(R.id.edWaktu);
        tvTanggal = findViewById(R.id.tvTanggal);
        back = findViewById(R.id.back);

        String dataAgenda = getIntent().getStringExtra("agenda");
        String dataWaktu = getIntent().getStringExtra("waktu");
        String dataTanggal = getIntent().getStringExtra("tanggal");

        back.setOnClickListener( v -> getOnBackPressedDispatcher().onBackPressed());
        tvTanggal.setText(dataTanggal);
        agenda.setText(dataAgenda);
        agenda.setEnabled(false);
        waktu.setText(dataWaktu);
        waktu.setEnabled(false);
    }
}