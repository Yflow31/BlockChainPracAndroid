package com.example.blockchainprac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.VoteUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseFirestore db;

    EditText rgemail,rgpassword;

    Button registerbtn,regtologin;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Register.this, Welcome.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        auth = FirebaseAuth.getInstance();

        rgemail = findViewById(R.id.rgemail);
        rgpassword = findViewById(R.id.rgpassword);

        registerbtn = findViewById(R.id.registerbtn);
        regtologin = findViewById(R.id.regtologin);

        db = FirebaseFirestore.getInstance();

        regtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personemail = ""+String.valueOf(rgemail.getText());
                String personpassword = ""+String.valueOf(rgpassword.getText());

                if (!TextUtils.isEmpty(personemail) && !TextUtils.isEmpty(personpassword)) {
                    auth.createUserWithEmailAndPassword(personemail, personpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createUserInFireStore(personemail);
                            } else {
                                Toast.makeText(Register.this, "Something went wrong in register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Register.this, "Please enter email ID and Password. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUserInFireStore(String email) {
        // For Adding User in Fire Store
        if (auth.getCurrentUser()!= null){
            String ID =  auth.getCurrentUser().getUid();
            VoteUser voteUser = new VoteUser();
            voteUser.setID(ID);
            voteUser.setEmail(email);
            voteUser.setVoted(false);
            voteUser.setVotingTimestamp("");

            db.collection(AppConstants.COLLECTION).document(ID).set(voteUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                       if (task.isSuccessful()){
                           Toast.makeText(this, "We have sent you a verification link via email. Please verify your account.", Toast.LENGTH_SHORT).show();
                       }
                    });
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(this, "Something went wrong "+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}