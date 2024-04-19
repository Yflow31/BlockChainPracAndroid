package com.example.blockchainprac.vote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blockchainprac.Block;
import com.example.blockchainprac.Blockchain;
import com.example.blockchainprac.Login;
import com.example.blockchainprac.R;
import com.example.blockchainprac.Welcome;
import com.example.blockchainprac.admin.Candidate;
import com.example.blockchainprac.admin.CandidateAdapter;
import com.example.blockchainprac.lastData;
import com.example.blockchainprac.utils.AppConstants;
import com.example.blockchainprac.utils.VoteUser;
import com.example.blockchainprac.vote.VoteHelper;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VotingMain extends AppCompatActivity implements VoteHelper {

    String CandidateId = "";
    FirebaseFirestore db;

    FirebaseAuth auth;

    FirebaseUser user;
    String lasthash;
    int lastindex;
    Blockchain blockchain;

    Button blockadd,back;

    RecyclerView recyclerView;

    DatabaseReference votingDbref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting_main);

        db = FirebaseFirestore.getInstance();

        recyclerView=findViewById(R.id.candiate_list);

        votingDbref = FirebaseDatabase.getInstance().getReference().child("Details");


        blockadd = findViewById(R.id.blockadd);

        back = findViewById(R.id.back);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        getCandidateList();
        checkUserVote();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                finish();
            }
        });

        votingDbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                lastindex = count;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    lasthash = snapshot1.child("hash").getValue(String.class);
                    Log.d("Last Hash ", "Data: "+lasthash);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



        blockchain = new Blockchain(4);
        blockadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int newIndex;
                String previousHash;

                if (lastindex == 0) {
                    newIndex = blockchain.chain.size(); // Calculate the index of the new block
                } else {
                    newIndex = lastindex+1;
                }

                String newData = CandidateId; // New data to be added
                if (lasthash == null) {
                    previousHash = blockchain.chain.get(blockchain.chain.size() - 1).getHash(); // Calculate the index of the new block
                } else {
                    previousHash = lasthash;
                }

                // Create a new block and add it to the blockchain
                Block newBlock = new Block(newIndex, System.currentTimeMillis(), newData, previousHash);
                blockchain.addBlock(newBlock);

                // Update the UI to display the newly added block
                Log.d("blockshow", "Block added: Index " + newIndex + "\n Data: " + newData + "\n Hash: " + newBlock.getHash());
                appendFirebase(newBlock.getHash().toString(),newData,newIndex, String.valueOf(newBlock.getTimestamp()));
            }
        });
        
    }

    private void checkUserVote() {
        String ID = user.getUid();
        db.collection(AppConstants.COLLECTION).document(ID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()){
                    VoteUser voteUser = snapshot.toObject(VoteUser.class);
                    if (voteUser.getVoted()){
                        blockadd.setEnabled(false);
                    }
                }
            }
        });
    }

    private void appendFirebase(String hash,String data,int index,String timestamp) {
        lastData showData = new lastData(hash,data,index,timestamp);
        String ID = user.getUid();
        votingDbref.child(String.valueOf(index)).setValue(showData).addOnCompleteListener(task -> {
            db.collection(AppConstants.COLLECTION).document(ID).update("voted",true);
            db.collection(AppConstants.COLLECTION).document(ID).update("votingTimestamp",String.valueOf(System.currentTimeMillis()));
            blockadd.setEnabled(false);
        });
    }

    @Override
    public void voteSelected(Candidate candidate) {
        CandidateId = candidate.getName();
    }

    private void getCandidateList() {
        db = FirebaseFirestore.getInstance();

        ArrayList<Candidate> candidates = new ArrayList<>();
        VoteAdapter adapter = new VoteAdapter(candidates,this::voteSelected);
        recyclerView.setAdapter(adapter);

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
}