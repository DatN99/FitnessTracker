package com.example.fitnesstracker.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {

    private static ArrayList<Sets> SetsList;

    public static class SetViewHolder extends RecyclerView.ViewHolder{

        public TextView nameText;
        public TextView repsText;
        public TextView weightText;

        public SetViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameTextView);
            repsText = itemView.findViewById(R.id.setsrepsTextView);
            weightText = itemView.findViewById(R.id.weightTextView);

        }
    }

    public SetAdapter(ArrayList<Sets> temp){
        this.SetsList = temp;
    }



    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewsets, parent, false);
        SetViewHolder svh = new SetViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SetAdapter.SetViewHolder holder, int position) {
        Sets currentSet = SetsList.get(position);

        holder.nameText.setText(currentSet.getName());
        holder.repsText.setText(currentSet.getReps());
        holder.weightText.setText(currentSet.getWeight());
    }

    @Override
    public int getItemCount() {
        return SetsList.size();
    }
}
