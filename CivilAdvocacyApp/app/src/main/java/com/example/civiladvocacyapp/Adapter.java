package com.example.civiladvocacyapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "EmployeesAdapter";
    private final List<Official> officialList;
    private final MainActivity mainAct;

    Adapter(List<Official> officialList, MainActivity ma) {
        this.officialList = officialList;
        this.mainAct = ma;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gov_list, parent, false);

        itemView.setOnClickListener(mainAct);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note " + position);

        Official official = officialList.get(position);
        holder.position.setText(official.getPosition());
        String text = official.getName() + " (" + official.getParty() +")";
        holder.name_party.setText(text);
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }

}

