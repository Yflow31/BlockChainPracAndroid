package com.example.blockchainprac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blockchainprac.admin.Admin;
import com.example.blockchainprac.vote.VotingMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity {
    FirebaseAuth auth;

    FirebaseUser user;

    Button signout,gotovote,dashboard,adminpanel;

    TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        signout = findViewById(R.id.signout);
        detail=findViewById(R.id.detail);
        gotovote=findViewById(R.id.gotovote);
        dashboard=findViewById(R.id.dashboard);
        adminpanel=findViewById(R.id.adminpanel);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        adminpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Admin.class);
                startActivity(intent);
            }
        });

        //Checking user
        if (user == null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        else {
            detail.setText(user.getEmail());
        }


        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(intent);

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        gotovote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.reload().addOnCompleteListener(task ->{
                    if (task.isSuccessful()){
                        user = auth.getCurrentUser();
                    }
                });
                //Checking if Verified
                boolean IsVerified = user.isEmailVerified();
                Log.d("firebase_user", "onClick: "+ user.toString());
                if (!IsVerified){
                    user.sendEmailVerification().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Welcome.this, "We have sent a verification link on your email please verify before voting", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Welcome.this, "Email not sent "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Intent intent = new Intent(getApplicationContext(), VotingMain.class);
                    startActivity(intent);
                }
            }
        });






    }
}