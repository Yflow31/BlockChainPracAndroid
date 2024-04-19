package com.example.blockchainprac.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blockchainprac.databinding.ItemCandiateBinding;
import com.example.blockchainprac.databinding.ItemDashboardBinding;
import com.example.blockchainprac.lastData;
import com.example.blockchainprac.utils.HashAdapter;

import java.util.ArrayList;
import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder> {

    List<Candidate> datalist = new ArrayList<>();

    CandidateHelper candidateHelper;

    public CandidateAdapter(List<Candidate> datalist, CandidateHelper candidateHelper) {
        this.datalist = datalist;
        this.candidateHelper = candidateHelper;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCandiateBinding binding = ItemCandiateBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CandidateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Candidate candidate = datalist.get(position);
        holder.binding.itemCandiateName.setText(candidate.getName());
        holder.binding.itemCandiateAge.setText(candidate.getAge());
        holder.binding.itemCandiateEmail.setText(candidate.getEmail());
        holder.binding.itemCandiatePhone.setText(candidate.getPhone());
        holder.binding.itemCandiateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                candidateHelper.deleteCandidate(candidate.getId());
            }
        });

    }

    @Override
    public int getItemCount() { // Mathematical logic
        return datalist != null ? datalist.size() : 0;
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder{
        ItemCandiateBinding binding;
        public CandidateViewHolder(@NonNull  ItemCandiateBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
