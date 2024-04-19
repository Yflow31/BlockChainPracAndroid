package com.example.blockchainprac.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blockchainprac.R;
import com.example.blockchainprac.databinding.ActivityAdminBinding;
import com.example.blockchainprac.databinding.ActivityCandiateAddBinding;
import com.example.blockchainprac.utils.AppConstants;
import com.google.firebase.firestore.FirebaseFirestore;

public class CandiateAdd extends AppCompatActivity {

    View view;

    ActivityCandiateAddBinding binding;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCandiateAddBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();

        binding.btnAddCandidate.setOnClickListener(view1 -> {
            addCandidateToFirebase();
        });

    }

    private void addCandidateToFirebase() {
        String name = ""+ binding.name.getText().toString();
        String email = ""+ binding.email.getText().toString();
        String age = ""+ binding.age.getText().toString();
        String phone = ""+ binding.phone.getText().toString();
        if (validatetextinput()){
            String documentId = db.collection(AppConstants.CANDIDATES).document().getId();
            Candidate candidate = new Candidate(documentId,name,email,age,phone);
            db.collection(AppConstants.CANDIDATES).document(documentId).set(candidate).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    getOnBackPressedDispatcher().onBackPressed();
                }
            });


        }else {
            Toast.makeText(this, "Please add all details", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatetextinput() {
        String name = ""+ binding.name.getText().toString();
        String email = ""+ binding.email.getText().toString();
        String age = ""+ binding.age.getText().toString();
        String phone = ""+ binding.phone.getText().toString();
        if (name.isEmpty() || email.isEmpty() || age.isEmpty() || phone.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}