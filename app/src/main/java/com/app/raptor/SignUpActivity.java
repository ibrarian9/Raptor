package com.app.raptor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.raptor.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText edEmail, edPass;
    String email, pass;
    FirebaseAuth mAuth;
    TextView btnReg, signIn;

    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        //  Check if user Signed
        FirebaseUser current = mAuth.getCurrentUser();
        if (current != null){
            String userId = current.getUid();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
            db.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserDetails userDet = snapshot.getValue(UserDetails.class);
                    assert userDet != null;
                    String nama = userDet.getNama();
                    if (!nama.equals("")){
                        startActivity(new Intent(getApplicationContext(), ListLaporanActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        finish();
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
        setContentView(R.layout.activity_sign_up);

        edEmail = findViewById(R.id.edemail);
        edPass = findViewById(R.id.edpass);
        btnReg = findViewById(R.id.register);
        signIn = findViewById(R.id.signIn);
        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener( v -> startActivity(new Intent(this, LoginActivity.class)));

        btnReg.setOnClickListener( v -> {
            email = edEmail.getText().toString();
            pass = edPass.getText().toString();

            if (TextUtils.isEmpty(email)){
                Toast.makeText(this, "Email Kosong...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Password Kosong...", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        //  get uid
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        String uid = user.getUid();

                        UserDetails newUser = new UserDetails("","","","","", email, "");
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                        db.child(uid).setValue(newUser).addOnCompleteListener(t -> {
                           if (t.isSuccessful()){
                               Toast.makeText(this, "Akun Berhasil dibuat...", Toast.LENGTH_SHORT).show();
                               Intent i = new Intent(this, RegisterActivity.class);
                               i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(i);
                               finish();
                           } else {
                               Toast.makeText(this, "Account Registered Failed", Toast.LENGTH_SHORT).show();
                           }
                        });
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            edEmail.setError("Email is Already registered");
                        } catch (Exception e) {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}