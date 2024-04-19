package com.example.blockchainprac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blockchainprac.databinding.LoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    LoginBinding binding;

    View view;
    FirebaseAuth auth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this, Welcome.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();



        binding.logintoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personemail = ""+ binding.lgemail.getText().toString();
                String personpassword = ""+ binding.lgpassword.getText().toString();

                if (!binding.lgemail.getText().toString().isEmpty() && !binding.lgpassword.getText().toString().isEmpty()) {
                    auth.signInWithEmailAndPassword(personemail, personpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Login Complete", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, Welcome.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(Login.this, "One of the column is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}