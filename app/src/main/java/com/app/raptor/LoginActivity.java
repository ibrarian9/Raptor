package com.app.raptor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Dospem.DospemActivityHome;
import com.app.raptor.Koordinator.KoordinatorActivityHome;
import com.app.raptor.Mahasiswa.ListLaporanActivity;
import com.app.raptor.Mahasiswa.RegisterActivity;
import com.app.raptor.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView login;
    FirebaseAuth mAuth;
    EditText edEmail, edPass;
    String email, pass, uid;
    ProgressBar pb;

    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        //  Check if user Signed
        FirebaseUser current = mAuth.getCurrentUser();
        if (current != null){
            uid = current.getUid();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
            db.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String getRole = snapshot.child("role").getValue(String.class);
                        if (getRole == null){
                            UserDetails userDet = snapshot.getValue(UserDetails.class);
                            assert userDet != null;
                            String nama = userDet.getNama();
                            String nim = userDet.getNim();
                            String judul = userDet.getJudulkp();
                            if (!nama.equals("") || !nim.equals("") || !judul.equals("")){
                                startActivity(new Intent(LoginActivity.this, ListLaporanActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                finish();
                            }
                        } else if (getRole.equals("dospem")){
                            startActivity(new Intent(LoginActivity.this, DospemActivityHome.class));
                            finish();
                        } else if (getRole.equals("koordinator")) {
                            startActivity(new Intent(LoginActivity.this, KoordinatorActivityHome.class));
                            finish();
                        }
                    } else {
                        email = current.getEmail();
                        UserDetails userDet = new UserDetails("","","","","","", email,"", uid);
                        db.child(uid).setValue(userDet).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);

        pb = findViewById(R.id.progressBar);

        login = findViewById(R.id.login);
        login.setOnClickListener( v -> {
            email = String.valueOf(edEmail.getText());
            pass = String.valueOf(edPass.getText());

            if (TextUtils.isEmpty(email)){
                Toast.makeText(LoginActivity.this, "Enter Email...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(pass)) {
                Toast.makeText(LoginActivity.this, "Enter Password...", Toast.LENGTH_SHORT).show();
            } else {
                if (!pb.isAnimating()){
                    pb.setVisibility(View.VISIBLE);
                }

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(t -> {
                           if (t.isSuccessful()){

                               //   Get UserName
                               mAuth = FirebaseAuth.getInstance();
                               FirebaseUser user = mAuth.getCurrentUser();
                               assert user != null;
                               uid = user.getUid();

                               DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                               db.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if (snapshot.exists()){
                                           String getRole = (String) snapshot.child("role").getValue();
                                           if (getRole == null){
                                               UserDetails userDet = snapshot.getValue(UserDetails.class);
                                               assert userDet != null;
                                               String nama = userDet.getNama();
                                               String nim = userDet.getNim();
                                               String judul = userDet.getJudulkp();
                                               if (!nama.equals("") || !nim.equals("") || !judul.equals("")){
                                                   if (pb.isAnimating()){
                                                       pb.setVisibility(View.GONE);
                                                   }
                                                   startActivity(new Intent(LoginActivity.this, ListLaporanActivity.class));
                                                   finish();
                                               } else {
                                                   if (pb.isAnimating()){
                                                       pb.setVisibility(View.GONE);
                                                   }
                                                   startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                                   finish();
                                               }
                                           } else if (getRole.equals("dospem")){
                                               if (pb.isAnimating()){
                                                   pb.setVisibility(View.GONE);
                                               }
                                               startActivity(new Intent(LoginActivity.this, DospemActivityHome.class));
                                               finish();
                                           } else if (getRole.equals("koordinator")) {
                                               if (pb.isAnimating()){
                                                   pb.setVisibility(View.GONE);
                                               }
                                               startActivity(new Intent(LoginActivity.this, KoordinatorActivityHome.class));
                                               finish();
                                           }
                                       } else {
                                           UserDetails userDet = new UserDetails("","","","","","",email,"", uid);
                                           db.child(uid).setValue(userDet).addOnCompleteListener(task -> {
                                               if (task.isSuccessful()){
                                                   if (pb.isAnimating()){
                                                       pb.setVisibility(View.GONE);
                                                   }
                                                   startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                                   finish();
                                               }
                                           });
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });
                           } else {
                               Toast.makeText(LoginActivity.this, "Email atau Password Salah...", Toast.LENGTH_SHORT).show();
                           }
                        });
            }
        });
    }
}