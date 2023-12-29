package com.app.raptor.Mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Models.UserDetails;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView botNavbar;
    TextView nama, nim, edNama, edEmail, edJudulkp, semester, tanggal, dospem, pembimbing, btnEdit, logout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid, sNim, sNama, sEmail, sJudul, sDospem, sPembimbing, sTgl, sSem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nama = findViewById(R.id.tvNama);
        nim = findViewById(R.id.tvNim);
        botNavbar = findViewById(R.id.bottomNavigationView);

        edNama = findViewById(R.id.nama);
        edEmail = findViewById(R.id.email);
        edJudulkp = findViewById(R.id.judulkp);
        semester = findViewById(R.id.semester);
        tanggal = findViewById(R.id.tglmulai);
        dospem = findViewById(R.id.dospem);
        pembimbing = findViewById(R.id.instansi);
        btnEdit = findViewById(R.id.edit);
        logout = findViewById(R.id.logout);

        //  Get Current User Data
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        uid = user.getUid();


        Dialog dialog = new Dialog(this);
        logout.setOnClickListener(v -> {
            dialog.setContentView(R.layout.logout_dialog);
            dialog.setCancelable(false);
            dialogFull(dialog);

            TextView no = dialog.findViewById(R.id.no);
            no.setOnClickListener( v1 -> dialog.dismiss());

            TextView yes = dialog.findViewById(R.id.yes);
            yes.setOnClickListener( v2 -> {
                mAuth.signOut();
                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(i);
                finish();
                dialog.dismiss();
            });
            dialog.show();
        });

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                    sNama = uDetail.getNama();
                    sNim = uDetail.getNim();
                    sEmail = uDetail.getEmail();
                    sJudul = uDetail.getJudulkp();
                    sDospem = uDetail.getDospem();
                    sPembimbing = uDetail.getPembimbing();
                    sTgl = uDetail.getTglMulai();
                    sSem = uDetail.getSemester();

                    nama.setText(sNama);
                    nim.setText(sNim);

                    //  Show Data Profile
                    edNama.setText(sNama);
                    edEmail.setText(sEmail);
                    edJudulkp.setText(sJudul);
                    semester.setText(sSem);
                    tanggal.setText(sTgl);
                    dospem.setText(sDospem);
                    pembimbing.setText(sPembimbing);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnEdit.setOnClickListener( v -> {
            Dialog d = new Dialog(this);
            d.setCancelable(false);
            d.setContentView(R.layout.dialog_profile);
            dialogFull(d);
            //  Set EditText
            EditText nama, email, judulkp, sem, tglkp, dospem, instansi;

            nama = d.findViewById(R.id.nama);
            email = d.findViewById(R.id.email);
            judulkp = d.findViewById(R.id.judulkp);
            sem = d.findViewById(R.id.semester);
            tglkp = d.findViewById(R.id.tglmulai);
            dospem = d.findViewById(R.id.dospem);
            instansi = d.findViewById(R.id.instansi);

            //  Set Data from History Account
            nama.setText(sNama);
            email.setText(sEmail);
            judulkp.setText(sJudul);
            tglkp.setText(sTgl);
            sem.setText(sSem);
            dospem.setText(sDospem);
            instansi.setText(sPembimbing);

            TextView batal = d.findViewById(R.id.batal);
            batal.setOnClickListener( v1 -> d.dismiss());

            TextView simpan = d.findViewById(R.id.simpan);
            simpan.setOnClickListener( v1 -> {
                String dataNama = nama.getText().toString();
                String dataEmail = email.getText().toString();
                String dataJudul = judulkp.getText().toString();
                String dataSem = sem.getText().toString();
                String dataTgl = tglkp.getText().toString();
                String dataDospem = dospem.getText().toString();
                String dataInstansi = instansi.getText().toString();

                if (TextUtils.isEmpty(dataNama)){
                    Toast.makeText(this, "Nama Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dataEmail)){
                    Toast.makeText(this, "Email Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dataJudul)){
                    Toast.makeText(this, "Judul Kp Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dataSem)){
                    Toast.makeText(this, "Semester Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dataTgl)){
                    Toast.makeText(this, "Tanggal Mulai Kp Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dataDospem)){
                    Toast.makeText(this, "Dosen Pembimbing Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dataInstansi)){
                    Toast.makeText(this, "Pembimbing Instansi Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    UserDetails userData = new UserDetails(dataNama, sNim, dataJudul, dataSem, dataDospem, dataInstansi, dataEmail, dataTgl, uid);
                    ref.setValue(userData).addOnCompleteListener( t -> {
                       if (t.isSuccessful()){
                           Toast.makeText(this, "Data Berhasil di Update...", Toast.LENGTH_SHORT).show();
                           d.dismiss();
                       } else {
                           Toast.makeText(this, "Data Gagal di Update...", Toast.LENGTH_SHORT).show();
                           d.dismiss();
                       }
                    });
                    d.dismiss();
                }
            });
            d.show();
        });

        botNavbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, ListLaporanActivity.class));
                return true;
            } else if (id == R.id.agenda) {
                startActivity(new Intent(this, HasilLaporanActivity.class));
                return true;
            } else return id == R.id.profil;
        });
    }

    private void dialogFull(Dialog d) {
        if (d.getWindow() != null){
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}