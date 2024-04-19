package com.example.blockchainprac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.HashAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Logs extends AppCompatActivity {

    RecyclerView hashlist;

    DatabaseReference hashref;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        hashlist = findViewById(R.id.hashlist);
        hashref = FirebaseDatabase.getInstance().getReference().child(AppConstants.DETAILS);

        ArrayList<lastData> list = new ArrayList<>();
        HashAdapter adapter = new HashAdapter(list);
        hashlist.setAdapter(adapter);

        hashref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                int count = (int) snapshot.getChildrenCount();

                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    lastData showData = snapshot1.getValue(lastData.class);
                    String txt = showData.getData()+":"+showData.getHash();
                    Log.d("Checkingdata", "onDataChange: "+ txt);
                    list.add(showData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}