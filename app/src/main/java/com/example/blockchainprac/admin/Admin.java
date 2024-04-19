package com.example.blockchainprac.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blockchainprac.R;
import com.example.blockchainprac.databinding.ActivityAdminBinding;
import com.example.blockchainprac.databinding.LoginBinding;
import com.example.blockchainprac.utils.AppConstants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Admin extends AppCompatActivity implements CandidateHelper{

    FirebaseFirestore db;

    View view;

    ActivityAdminBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

       getCandidateList();

        binding.fab.setOnClickListener(view1 -> {
            // Candidate addition activity
            Intent intent = new Intent(Admin.this, CandiateAdd.class);
            startActivity(intent);

        });

    }

    private void getCandidateList() {
        db = FirebaseFirestore.getInstance();

        ArrayList<Candidate> candidates = new ArrayList<>();
        CandidateAdapter adapter = new CandidateAdapter(candidates,this::deleteCandidate);
        binding.candiateList.setAdapter(adapter);

        db.collection(AppConstants.CANDIDATES).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    Candidate candidatesnap = documentSnapshot.toObject(Candidate.class);
                    candidates.add(candidatesnap);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCandidateList();
    }

    @Override
    public void deleteCandidate(String ID) {
        db = FirebaseFirestore.getInstance();
        db.collection(AppConstants.CANDIDATES).document(ID).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                getCandidateList();
            }else {
                Toast.makeText(this, "Unable to Delete Test", Toast.LENGTH_SHORT).show();
            }
        });
    }
}