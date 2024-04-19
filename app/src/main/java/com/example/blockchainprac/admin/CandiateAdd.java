package com.example.blockchainprac.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blockchainprac.R;
import com.example.blockchainprac.databinding.ActivityAdminBinding;
import com.example.blockchainprac.databinding.ActivityCandiateAddBinding;
import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.Loader;
import com.google.firebase.firestore.FirebaseFirestore;

public class CandiateAdd extends AppCompatActivity {

    Loader loadingInsuranceDialogueFragment;
    boolean LOADER_SHOWING = false;
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
        showLoadingMain();
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
            hideLoadingMain();
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