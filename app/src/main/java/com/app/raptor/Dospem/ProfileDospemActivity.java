package com.app.raptor.Dospem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.app.raptor.Mahasiswa.LoginActivity;
import com.app.raptor.Models.Dosen;
import com.app.raptor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileDospemActivity extends AppCompatActivity {

    BottomNavigationView botnav;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    String uid, sNama, sNip, sNohp, sEmail;
    EditText nama, nip, noHp, email;
    TextView tvNama, tvNip, edit, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dospem);

        //   Get Id
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        assert fUser != null;
        uid = fUser.getUid();

        botnav = findViewById(R.id.botNavbar);
        nama = findViewById(R.id.edNama);
        nip = findViewById(R.id.edNip);
        noHp = findViewById(R.id.edNohp);
        email = findViewById(R.id.edEmail);
        tvNama = findViewById(R.id.tvNama);
        tvNip = findViewById(R.id.tvNim);
        edit = findViewById(R.id.edit);
        logout = findViewById(R.id.logout);

        nama.setEnabled(false);
        nip.setEnabled(false);
        noHp.setEnabled(false);
        email.setEnabled(false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Dosen dosen = snapshot.getValue(Dosen.class);
                    if (dosen != null){
                        //  GetData
                        sNama = dosen.getNama();
                        sNip = dosen.getNip();
                        sNohp = dosen.getNoHp();
                        sEmail = dosen.getEmail();

                        tvNama.setText(dosen.getNama());
                        nama.setText(dosen.getNama());
                        nip.setText(dosen.getNip());
                        noHp.setText(dosen.getNoHp());
                        email.setText(dosen.getEmail());
                        if (!dosen.getNip().isEmpty()){
                            tvNip.setText(dosen.getNip());
                        } else {
                            tvNip.setText("NIP belum Diisi...");
                        }
                    }
                } else {
                    Toast.makeText(ProfileDospemActivity.this, "Data user tidak ditemukan...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Dialog d = new Dialog(this);
        edit.setOnClickListener( v -> {
            d.setContentView(R.layout.dialog_profile_dospem);
            d.setCancelable(false);
            dialogFull(d);

            EditText edNama = d.findViewById(R.id.edNama);
            EditText edNip = d.findViewById(R.id.edNip);
            EditText edNohp = d.findViewById(R.id.edNohp);
            EditText edEmail = d.findViewById(R.id.edEmail);

            edNama.setText(sNama);
            edNama.setEnabled(false);
            edNip.setText(sNip);
            edNohp.setText(sNohp);
            edEmail.setText(sEmail);
            edEmail.setEnabled(false);

            TextView batal = d.findViewById(R.id.batal);
            batal.setOnClickListener( v1 -> d.dismiss());

            TextView okay = d.findViewById(R.id.simpan);
            okay.setOnClickListener( v2 -> {
                String nip = edNip.getText().toString();
                String noHp = edNohp.getText().toString();

                if (TextUtils.isEmpty(nip)){
                    Toast.makeText(this, "NIP Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(noHp)) {
                    Toast.makeText(this, "Nomor Hape Masih Kosong...", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    Dosen dosen = new Dosen(sEmail, sNama, nip, noHp, "dospem");
                    dref.setValue(dosen).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, "Profile Berhasil di Ubah...", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        } else {
                            Toast.makeText(this, "Profile gagal di Ubah...", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        }
                    });
                    d.dismiss();
                }
            });
            d.show();
        });

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

        botnav.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.beranda){
                startActivity(new Intent(this, DospemActivityHome.class));
                return true;
            } else if (id == R.id.notif) {
                startActivity(new Intent(this, NotifikasiActivity.class));
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