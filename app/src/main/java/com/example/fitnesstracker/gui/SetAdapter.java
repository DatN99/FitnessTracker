package com.example.fitnesstracker.gui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;

import java.util.ArrayList;


public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {

    private static ArrayList<Sets> SetsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddItem(int position);
        void onDeleteItem(int position);
        void onEditItem(int position);
    }


    public static class SetViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView repsText;
        public TextView weightText;
        public Button addCopyButton;
        public Button removeButton;
        public Button editButton;


        public SetViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameTextView);
            repsText = itemView.findViewById(R.id.setsrepsTextView);
            weightText = itemView.findViewById(R.id.weightTextView);
            addCopyButton = itemView.findViewById(R.id.addCopyButton);
            removeButton = itemView.findViewById(R.id.removeButton);
            editButton = itemView.findViewById(R.id.editSetButton);


            addCopyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddItem(position);
                        }
                    }
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteItem(position);
                        }
                    }
                }
            });


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onEditItem(position);
                        }
                    }
                }
            });

        }
    }

    public SetAdapter(ArrayList<Sets> temp) {
        this.SetsList = temp;
    }


    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewsets, parent, false);

        SetViewHolder svh = new SetViewHolder(v, mListener);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



}




