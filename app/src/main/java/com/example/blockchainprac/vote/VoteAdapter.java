package com.example.blockchainprac.vote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blockchainprac.admin.Candidate;
import com.example.blockchainprac.admin.CandidateHelper;
import com.example.blockchainprac.databinding.ItemCandiateBinding;
import com.example.blockchainprac.databinding.ItemDashboardBinding;
import com.example.blockchainprac.databinding.ItemVoteBinding;
import com.example.blockchainprac.lastData;
import com.example.blockchainprac.utils.HashAdapter;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {

    List<Candidate> datalist = new ArrayList<>();
    VoteHelper voteHelper;
    int selectedPosition = RecyclerView.NO_POSITION; // Initially, no item is selected

    public VoteAdapter(List<Candidate> datalist, VoteHelper voteHelper) {
        this.datalist = datalist;
        this.voteHelper = voteHelper;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVoteBinding binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        Candidate candidate = datalist.get(position);
        holder.binding.itemCandiateName.setText(candidate.getName());
        holder.binding.itemCandiateAge.setText(candidate.getAge());
        holder.binding.itemCandiateEmail.setText(candidate.getEmail());
        holder.binding.itemCandiatePhone.setText(candidate.getPhone());

        // Check if this item is currently selected
        holder.binding.itemVotecard.setChecked(position == selectedPosition);

        holder.binding.itemVotecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the selected position
                int oldSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();

                // Notify adapter about item selection changes
                notifyItemChanged(oldSelectedPosition);
                notifyItemChanged(selectedPosition);

                // Inform the helper about the selected candidate
                voteHelper.voteSelected(candidate);
            }
        });
    }

    @Override
    public int getItemCount() { // Mathematical logic
        return datalist != null ? datalist.size() : 0;
    }

    public static class VoteViewHolder extends RecyclerView.ViewHolder {
        ItemVoteBinding binding;

        public VoteViewHolder(@NonNull ItemVoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
