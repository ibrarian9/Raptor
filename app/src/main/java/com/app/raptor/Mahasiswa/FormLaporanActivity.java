package com.app.raptor.Mahasiswa;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class FormLaporanActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseStorage storage;
    ImageView potoLaporan, camera, gallery;
    private int jam, menit;
    Uri download;
    String uid, agenda, waktu, lokasi, narasumber, kegiatan, dataTanggal;
    TextView tanggal, tvSubmit, edWaktu;
    EditText edAgenda, edLokasi, edNarasumber, edKegiatan;
    ProgressBar progressBar;
    ActivityResultLauncher<Intent> resultLauncherCamera, resultLauncherGallery;

    @Override
    public void onBackPressed() {
        //    Disable Back Button
        Toast.makeText(this, "Tombol kembali di larang...", Toast.LENGTH_SHORT).show();
    }

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
        potoLaporan = findViewById(R.id.ivPoto);
        tvSubmit = findViewById(R.id.tvSimpan);
        progressBar = findViewById(R.id.progressBar);
        storage = FirebaseStorage.getInstance();

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

        edWaktu.setOnClickListener(v -> {
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


        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_poto);
        d.setCancelable(false);
        dialogFull(d);
        potoLaporan.setOnClickListener( v -> {

            camera = d.findViewById(R.id.camera);
            gallery = d.findViewById(R.id.gallery);

            camera.setOnClickListener(v1 -> {
                Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultLauncherCamera.launch(takePic);
                d.dismiss();
            });

            gallery.setOnClickListener(v2 -> {
                Intent openGallery = new Intent(Intent.ACTION_PICK);
                openGallery.setType("image/*");
                resultLauncherGallery.launch(openGallery);
                d.dismiss();
            });
            d.show();
        });

        resultLauncherCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle ex = data.getExtras();
                            if (ex != null){
                                Bitmap bitmap = (Bitmap) ex.get("data");
                                assert bitmap != null;
                                uploadToFirebase(bitmap);
                                potoLaporan.setImageBitmap(bitmap);
                            }
                        } else {
                            Toast.makeText(this, "Data tidak ada...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        resultLauncherGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            upToFirebase(imageUri);
                            Picasso.get().load(imageUri).fit().into(potoLaporan);
                        } else {
                            Toast.makeText(this, "Data tidak ada...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        tvSubmit.setOnClickListener( v -> {
            if (!progressBar.isAnimating()){
                progressBar.setVisibility(View.VISIBLE);
            }

            agenda = edAgenda.getText().toString();
            waktu = edWaktu.getText().toString();
            lokasi = edLokasi.getText().toString();
            narasumber = edNarasumber.getText().toString();
            kegiatan = edKegiatan.getText().toString();

            if (download == null){
                Toast.makeText(this, "Foto Masih Kosong...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(agenda)){
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
                Laporan dataLaporan = new Laporan(dataTanggal, agenda, waktu, lokasi, narasumber, kegiatan, download.toString(), "");

                DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Laporan").child(uid).child(dataTanggal);
                dRef.setValue(dataLaporan).addOnCompleteListener(task -> {

                    if (progressBar.isAnimating()){
                        progressBar.setVisibility(View.GONE);
                    }

                    Toast.makeText(this, "Laporan Berhasil Disimpan...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, ListLaporanActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(i);
                    finish();
                });

                if (progressBar.isAnimating()){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void upToFirebase(Uri imageUri) {

        if (!progressBar.isAnimating()){
            progressBar.setVisibility(View.VISIBLE);
        }

        StorageReference fileRef = storage.getReference().child("laporan").child(uid).child(dataTanggal);
        UploadTask uploadImg = fileRef.putFile(imageUri);

        uploadImg.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (progressBar.isAnimating()){
                    progressBar.setVisibility(View.GONE);
                }
                download = task.getResult();
                Toast.makeText(this, "Foto Berhasil Di Pilih...", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void uploadToFirebase(Bitmap bitmap) {

        StorageReference fileRef = storage.getReference().child("laporan").child(uid).child(dataTanggal);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()){
                throw Objects.requireNonNull(task.getException());
            }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                if (progressBar.isAnimating()){
                    progressBar.setVisibility(View.GONE);
                }
                download = task1.getResult();
                Toast.makeText(this, "Foto Berhasil Di Pilih...", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void dialogFull(Dialog dialog) {
        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}