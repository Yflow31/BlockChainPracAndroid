package com.example.blockchainprac.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blockchainprac.R;
import com.example.blockchainprac.databinding.ActivityAdminBinding;
import com.example.blockchainprac.databinding.LoginBinding;
import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.Loader;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Admin extends AppCompatActivity implements CandidateHelper{


    Loader loadingInsuranceDialogueFragment;
    boolean LOADER_SHOWING = false;

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
        showLoadingMain();
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
                hideLoadingMain();
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

    private void showLoadingMain() {
        if (!LOADER_SHOWING) {
            LOADER_SHOWING = true;

            FragmentManager fragmentManager = getSupportFragmentManager();
            Loader fragment = (Loader) fragmentManager.findFragmentByTag("loadingDialog");

            if (fragment == null) {
                // No existing fragment found, create and show a new instance
                loadingInsuranceDialogueFragment = Loader.newInstance("Loading, Please wait...");
                loadingInsuranceDialogueFragment.setCancelable(false);
                loadingInsuranceDialogueFragment.show(fragmentManager, "loadingDialog");
            } else if (!fragment.isAdded()) {
                // If the fragment exists but hasn't been added, show it again.
                // This scenario is rare due to the lifecycle of DialogFragment.
                loadingInsuranceDialogueFragment.show(fragmentManager, "loadingDialog");
            }
            // If the fragment is already added, it should be visible and nothing needs to be done.
        }

    }

    private void hideLoadingMain() {
        try {
            LOADER_SHOWING = false;

            FragmentManager fragmentManager = getSupportFragmentManager();
            Loader fragment = (Loader) fragmentManager.findFragmentByTag("loadingDialog");
            if (fragment != null && fragment.isAdded()) {
                fragment.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}