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

/**
 * This class is responsible for the cardviews in "WorkoutLogFragment"'s RecyclerView
 *
 * Each cardview displays the exercise name, reps, and weight (lbs)
 * On each cardview, there is also an option to add a duplicate set and edit that target set
 *
 * Note: a new Set will appear under the most recently created Set so that oldest->newest is top->bottom
 */

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {

    //SetsList variable
    private static ArrayList<Sets> mSetsList;

    //Listener variable
    private OnItemClickListener mListener;

    //Interface used by "SetViewHolder" to notify "WorkoutLogFragment" that an item needs to be changed or copied
    public interface OnItemClickListener {
        void onAddItem(int position);
        void onEditItem(int position);
    }

    //holds variables for cardview display
    public static class SetViewHolder extends RecyclerView.ViewHolder {

        //initialize widgets
        public TextView nameText;
        public TextView repsText;
        public TextView weightText;
        public Button addCopyButton;
        public Button editButton;


        public SetViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //assign widgets
            nameText = itemView.findViewById(R.id.nameTextView);
            repsText = itemView.findViewById(R.id.setsrepsTextView);
            weightText = itemView.findViewById(R.id.weightTextView);
            addCopyButton = itemView.findViewById(R.id.addCopyButton);
            editButton = itemView.findViewById(R.id.editSetButton);

            //adds copy of Set
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

            //edits Set
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

    //assigns mSetsList
    public SetAdapter(ArrayList<Sets> temp) {
        this.mSetsList = temp;
    }


    //inflate cardview layout
    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewsets, parent, false);

        SetViewHolder svh = new SetViewHolder(v, mListener);

        return svh;
    }


    //displays values for cardviews
    @Override
    public void onBindViewHolder(SetAdapter.SetViewHolder holder, int position) {

        Sets currentSet = mSetsList.get(position);

        holder.nameText.setText(currentSet.getName());
        holder.repsText.setText(currentSet.getReps() + " reps");
        holder.weightText.setText(currentSet.getWeight() + " lbs");

    }

    @Override
    public int getItemCount() {
        return mSetsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}




