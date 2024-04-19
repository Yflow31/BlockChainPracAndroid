package com.example.blockchainprac.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blockchainprac.databinding.ItemDashboardBinding;
import com.example.blockchainprac.lastData;

import java.util.ArrayList;
import java.util.List;

public class HashAdapter extends RecyclerView.Adapter <HashAdapter.AdapterViewHolder> {

    List<lastData> datalist = new ArrayList<>();

    public HashAdapter(List<lastData> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Supports each item
        ItemDashboardBinding binding = ItemDashboardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AdapterViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) { // Logic to represent the data
        lastData data = datalist.get(position);
        holder.binding.itemHash.setText(data.getData() + "\n\n"+ data.getHash() + "\n\n"+ data.getTimestamp());

    }

    @Override
    public int getItemCount() { // Mathematical logic
        return datalist != null ? datalist.size() : 0;
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{  // UI level logic

        ItemDashboardBinding binding;

        public AdapterViewHolder(@NonNull ItemDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}
