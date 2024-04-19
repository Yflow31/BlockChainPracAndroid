package com.example.blockchainprac.result;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blockchainprac.admin.Candidate;
import com.example.blockchainprac.databinding.ItemResultBinding;
import com.example.blockchainprac.databinding.ItemVoteBinding;
import com.example.blockchainprac.vote.VoteAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultCountViewHolder> {
    List<ResultCount> datalist = new ArrayList<>();

    public ResultAdapter(List<ResultCount> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ResultCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemResultBinding binding = ItemResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultCountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultCountViewHolder holder, int position) {
        ResultCount candidate = datalist.get(position);
        holder.binding.itemCandiateName.setText(candidate.getName());
        int postion_index = position+1;
        holder.binding.itemIndex.setText(""+postion_index);
        holder.binding.voteCount.setText(""+candidate.getCount());
    }

    @Override
    public int getItemCount() { // Mathematical logic
        return datalist != null ? datalist.size() : 0;
    }

    public static class ResultCountViewHolder extends RecyclerView.ViewHolder{
        ItemResultBinding binding;

        public ResultCountViewHolder(@NonNull ItemResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
