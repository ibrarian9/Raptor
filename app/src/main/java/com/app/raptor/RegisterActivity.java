package com.app.raptor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextView simpan;
    EditText edNama, edNim, edJudulKp, edDospen, edPembimbing;
    String nama, nim, judulKp, dospem, pembimbing;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edNama = findViewById(R.id.nama);
        edNim = findViewById(R.id.nim);
        edJudulKp = findViewById(R.id.judul);
        edDospen = findViewById(R.id.dospem);
        edPembimbing = findViewById(R.id.pembimbing);
        simpan = findViewById(R.id.simpan);

        simpan.setOnClickListener( v -> {
            nama = edNama.getText().toString();
            nim = edNim.getText().toString();
            judulKp = edJudulKp.getText().toString();
            dospem = edDospen.getText().toString();
            pembimbing = edPembimbing.getText().toString();

            if (TextUtils.isEmpty(nama)){
                Toast.makeText(this, "Nama belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(nim)){
                Toast.makeText(this, "Nim belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(judulKp)) {
                Toast.makeText(this, "Judul Kp belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(dospem)) {
                Toast.makeText(this, "Dosen Pembimbing belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(pembimbing)) {
                Toast.makeText(this, "Pembimbing Instansi belum diisi...", Toast.LENGTH_SHORT).show();
            } else {
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                assert user != null;
                String uid = user.getUid();

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                db.child("nama").setValue(nama);
                db.child("nim").setValue(nim);
                db.child("judulkp").setValue(judulKp);
                db.child("dospem").setValue(dospem);
                db.child("pembimbing").setValue(pembimbing);
                Intent i = new Intent(this, ListLaporanActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}