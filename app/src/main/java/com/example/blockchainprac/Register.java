package com.example.blockchainprac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.Loader;
import com.example.blockchainprac.utils.RandomPhotoUrlGenerator;
import com.example.blockchainprac.utils.VoteUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    Loader loadingInsuranceDialogueFragment;
    boolean LOADER_SHOWING = false;

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
                showLoadingMain();
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
                                hideLoadingMain();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Register.this, "Please enter email ID and Password. ", Toast.LENGTH_SHORT).show();
                    hideLoadingMain();
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

            FirebaseUser firebaseUser = auth.getCurrentUser();

            String photoUrl = "https://api.dicebear.com/7.x/notionists/png?seed=" + (new RandomPhotoUrlGenerator().randomUsernameExtension());
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("")
                    .setPhotoUri(Uri.parse(photoUrl))
                    .build();



            db.collection(AppConstants.COLLECTION).document(ID).set(voteUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                       if (task.isSuccessful()){
                           Toast.makeText(this, "We have sent you a verification link via email. Please verify your account.", Toast.LENGTH_SHORT).show();
                       }
                    });
                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(taskUpdate -> {
                        if (taskUpdate.isSuccessful()) {
                            Log.d("PROFILE", "User profile updated.");
                            Intent intent = new Intent(getApplicationContext(), Welcome.class);
                            startActivity(intent);
                            finish();
                        }
                    });


                }else {
                    Toast.makeText(this, "Something went wrong "+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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