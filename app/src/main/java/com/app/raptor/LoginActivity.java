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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView login, signUp;
    FirebaseAuth mAuth;
    EditText edEmail, edPass;
    String email, pass;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        //  Check if user Signed
        FirebaseUser current = mAuth.getCurrentUser();
        if (current != null){
            Intent i = new Intent(this, ListLaporanActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener( v -> startActivity(new Intent(this, SignUpActivity.class)));

        login = findViewById(R.id.login);
        login.setOnClickListener( v -> {
            email = String.valueOf(edEmail.getText());
            pass = String.valueOf(edPass.getText());

            if (TextUtils.isEmpty(email)){
                Toast.makeText(LoginActivity.this, "Enter Email...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(pass)) {
                Toast.makeText(LoginActivity.this, "Enter Password...", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(t -> {
                           if (t.isSuccessful()){

                               //   Get UserName
                               mAuth = FirebaseAuth.getInstance();
                               FirebaseUser user = mAuth.getCurrentUser();
                               assert user != null;
                               String userId = user.getUid();

                               DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                               db.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       UserDetails userDet = snapshot.getValue(UserDetails.class);
                                       assert userDet != null;
                                       String nama = userDet.getNama();
                                       Intent i;
                                       if (nama != null){
                                           i = new Intent(getApplicationContext(), ListLaporanActivity.class);
                                       } else {
                                           i = new Intent(getApplicationContext(), RegisterActivity.class);
                                       }
                                       startActivity(i);
                                       finish();
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });
                           } else {
                               Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                           }
                        });
            }
        });
    }
}