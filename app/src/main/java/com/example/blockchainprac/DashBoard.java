package com.example.blockchainprac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.blockchainprac.result.ResultAdapter;
import com.example.blockchainprac.result.ResultCount;
import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.HashAdapter;
import com.example.blockchainprac.utils.Loader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashBoard extends AppCompatActivity {
    Loader loadingInsuranceDialogueFragment;
    boolean LOADER_SHOWING = false;
    RecyclerView hashlist;

    TextView dashcount;

    DatabaseReference hashref;

    Button gobacktowelcome, gotologs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        gotologs = findViewById(R.id.gotologs);

        hashlist = findViewById(R.id.hashlist);

        dashcount = findViewById(R.id.dashcount);

        gobacktowelcome = findViewById(R.id.gobacktowelcome);

        gotologs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, Logs.class);
                startActivity(intent);
            }
        });

        hashref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DETAILS);

        gobacktowelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                finish();
            }
        });

        ArrayList<lastData> list = new ArrayList<>();
        ArrayList<ResultCount> resultCounts = new ArrayList<>();
        ResultAdapter adapter = new ResultAdapter(resultCounts);
        hashlist.setAdapter(adapter);

        // Create a HashMap to store name counts
        HashMap<String, Integer> nameCounts = new HashMap<>();

        showLoadingMain();
        hashref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                nameCounts.clear();
                resultCounts.clear();


                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    lastData showData = snapshot1.getValue(lastData.class);
                    String name = showData.getData();
                    String txt = showData.getData() + ":" + showData.getHash();
                    Log.d("Checkingdata", "onDataChange: " + txt);
                    // Update the count for the current name
                    if (nameCounts.containsKey(name)) {
                        int count = nameCounts.get(name);
                        nameCounts.put(name, count + 1);
                    } else {
                        nameCounts.put(name, 1);
                    }
                    list.add(showData);
                }
                for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
                    Log.d("NameCount", entry.getKey() + ": " + entry.getValue());
                    resultCounts.add(new ResultCount(entry.getKey(), entry.getValue()));
                }
                adapter.notifyDataSetChanged();
                hideLoadingMain();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideLoadingMain();

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