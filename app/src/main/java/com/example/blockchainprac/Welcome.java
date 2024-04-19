package com.example.blockchainprac;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.blockchainprac.admin.Admin;
import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.Loader;
import com.example.blockchainprac.vote.VotingMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity {

    Loader loadingInsuranceDialogueFragment;
    boolean LOADER_SHOWING = false;
    FirebaseAuth auth;

    FirebaseUser user;

    Button signout, gotovote, dashboard, adminpanel;

    TextView detail;

    ImageView userphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        signout = findViewById(R.id.signout);
        detail = findViewById(R.id.detail);
        gotovote = findViewById(R.id.gotovote);
        dashboard = findViewById(R.id.dashboard);
        adminpanel = findViewById(R.id.adminpanel);
        userphoto = findViewById(R.id.userphoto);

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
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            detail.setText(user.getEmail());
        }

        checkadminlogin();

        Log.d("profileimg", "Here: "+user.getPhotoUrl());
        Glide.with(this)
                .load(user.getPhotoUrl())
                .transform(new FitCenter(), new RoundedCorners(28))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(userphoto);

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
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        gotovote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingMain();
                user.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        if (user != null) {
                            //Checking if Verified
                            boolean IsVerified = user.isEmailVerified();
                            Log.d("firebase_user", "onClick: " + user.toString());
                            if (!IsVerified) {
                                user.sendEmailVerification().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(Welcome.this, "We have sent a verification link on your email please verify before voting", Toast.LENGTH_SHORT).show();
                                        hideLoadingMain();
                                    } else {
                                        Toast.makeText(Welcome.this, "Email not sent " + task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        hideLoadingMain();
                                    }
                                });
                            } else {
                                Intent intent = new Intent(getApplicationContext(), VotingMain.class);
                                startActivity(intent);
                            }
                        }
                    }
                });

            }
        });
    }

    private void checkadminlogin() {

        if (user.getEmail().equalsIgnoreCase(AppConstants.ADMINEMAIL)){
            adminpanel.setVisibility(View.VISIBLE);
        }else {
            adminpanel.setVisibility(View.GONE);
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