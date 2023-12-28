package com.app.raptor.Mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView simpan;
    EditText edNama, edNim, edJudulKp;
    Spinner edDospen, edInstansi;
    String nama, nim, judulKp, dospem, pembimbing;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(Color.BLACK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edNama = findViewById(R.id.nama);
        edNim = findViewById(R.id.nim);
        edJudulKp = findViewById(R.id.judul);
        edDospen = findViewById(R.id.dospem);
        edInstansi = findViewById(R.id.pembimbing);
        simpan = findViewById(R.id.simpan);

        //  Get Data Dosen
        String[] data = getResources().getStringArray(R.array.dosen);
        List<String> list = Arrays.asList(data);
        ArrayList<String> dataList = new ArrayList<>(list);
        edDospen.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataList);
        dataAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        edDospen.setAdapter(dataAdapter);
        System.out.println(edDospen.getSelectedItem().toString());

        //  Get Data Instansi
        String[] data1 = getResources().getStringArray(R.array.instansi);
        List<String> list1 = Arrays.asList(data1);
        ArrayList<String> dataList1 = new ArrayList<>(list1);
        edInstansi.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataList1);
        dataAdapter1.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        edInstansi.setAdapter(dataAdapter1);

        simpan.setOnClickListener( v -> {
            nama = edNama.getText().toString();
            nim = edNim.getText().toString();
            judulKp = edJudulKp.getText().toString();
            dospem = edDospen.getSelectedItem().toString();
            pembimbing = edInstansi.getSelectedItem().toString();

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