package com.app.raptor.Mahasiswa;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.raptor.Adapter.HasilLaporanAdapter;
import com.app.raptor.Models.Laporan;
import com.app.raptor.Models.UserDetails;
import com.app.raptor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class LaporanFile extends AppCompatActivity {

    RecyclerView rv;
    TextView judulkp, nama, nim, tgl, dospem, pembimbing, print;
    FirebaseAuth mAuth;
    ConstraintLayout constraintLayout;
    FirebaseUser user;
    String uid, sNama;
    HasilLaporanAdapter adapter;
    ArrayList<Laporan> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_laporan);

        judulkp = findViewById(R.id.judul);
        nama = findViewById(R.id.nama);
        nim = findViewById(R.id.nim);
        tgl = findViewById(R.id.tglmulai);
        dospem = findViewById(R.id.dospem);
        pembimbing = findViewById(R.id.pembimbing);
        rv = findViewById(R.id.rv);
        constraintLayout = findViewById(R.id.dataPrint);
        print = findViewById(R.id.print);

        //  Get Current User Data
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();

        //   Set Adapter
        adapter = new HasilLaporanAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                    sNama = uDetail.getNama();
                    String sNim = uDetail.getNim();
                    String sJudul = uDetail.getJudulkp();
                    String sDospem = uDetail.getDospem();
                    String sPembimbing = uDetail.getPembimbing();
                    String sTglMulai = uDetail.getTglMulai();

                    nama.setText(sNama);
                    nim.setText(sNim);
                    judulkp.setText(sJudul);
                    dospem.setText(sDospem);
                    pembimbing.setText(sPembimbing);
                    tgl.setText(sTglMulai);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Laporan").child(uid);
        data.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();

                for (DataSnapshot snap: snapshot.getChildren()){
                    Laporan lapor = snap.getValue(Laporan.class);
                    list.add(lapor);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        print.setOnClickListener( v -> printData() );
    }

    private void printData() {
        View v = constraintLayout;
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (getDisplay() != null){
                this.getDisplay().getRealMetrics(displayMetrics);
            }
        } else {
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        v.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));


        PdfDocument document = new PdfDocument();

        int viewWidth = v.getMeasuredWidth();
        int viewHeight = 10000;
        v.layout(0, 0 , displayMetrics.widthPixels, displayMetrics.heightPixels);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Canvas
        Canvas canvas = page.getCanvas();
        v.draw(canvas);

        // Finishing
        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Date date = new Date();
        String tgl = String.valueOf(date.getTime());
        String fName = "_" + tgl + "_" + sNama + ".pdf";
        File file = new File(downloadsDir, fName);
        String toast = "File " + fName + " berhasil disimpan di folder downloads...";
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
