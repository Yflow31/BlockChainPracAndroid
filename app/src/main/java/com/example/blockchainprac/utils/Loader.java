package com.example.blockchainprac.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.blockchainprac.databinding.LoadingLayoutBinding;

public class Loader extends DialogFragment {

    private static final String ARG_LOADING_TEXT = "LOADING_TEXT";
    private LoadingLayoutBinding binding;

    public static Loader newInstance(String loadingText) {
        Loader fragment = new Loader();
        Bundle args = new Bundle();
        args.putString(ARG_LOADING_TEXT, loadingText);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Creating the dialog with the context of the parent activity
        Dialog dialog = new Dialog(requireActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Inflate the layout for this fragment
        binding = LoadingLayoutBinding.inflate(LayoutInflater.from(getContext()), null, false);
        dialog.setContentView(binding.getRoot());

        // Set loading text from arguments
        String loadingText = getArguments() != null ? getArguments().getString(ARG_LOADING_TEXT, "Loading, Please wait...") : "Loading, Please wait...";
        binding.loadingText.setText(loadingText);

        // Adjust the size of the dialog if necessary
        // For example, setting the width to a percentage of the screen width
        // This part is optional and can be adjusted based on your design requirements

        return dialog;
    }


}