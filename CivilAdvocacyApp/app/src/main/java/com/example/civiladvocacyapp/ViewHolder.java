package com.example.civiladvocacyapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView position;
    TextView name_party;

    ViewHolder(View view) {
        super(view);
        name_party = view.findViewById(R.id.name_party);
        position = view.findViewById(R.id.position);
    }

}


