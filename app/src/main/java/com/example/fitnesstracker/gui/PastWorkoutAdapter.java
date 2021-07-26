package com.example.fitnesstracker.gui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;

import java.util.ArrayList;
import java.util.Set;

public class PastWorkoutAdapter extends RecyclerView.Adapter<PastWorkoutAdapter.PastWorkoutViewHolder>{

    private static ArrayList<String> TimeDateList;
    private static ArrayList<String> VolumeList;
    private static ArrayList<String> SetsList;


    class PastWorkoutViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "PastWorkoutViewHolder";

        public TextView TimeDate;
        public TextView Volume;
        public TextView SetInfo;

        Button expandButton;
        ConstraintLayout expandableLayout;


        public PastWorkoutViewHolder(View itemView) {
            super(itemView);

            TimeDate = itemView.findViewById(R.id.dateTextView);
            Volume = itemView.findViewById(R.id.volumeTextView);
            SetInfo = itemView.findViewById(R.id.SetInfoTextView);

            expandButton = itemView.findViewById(R.id.expandButton);
            expandableLayout = itemView.findViewById(R.id.expandableConstraintLayout);
            expandableLayout.setVisibility(View.GONE);



            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (expandableLayout.getVisibility() == View.VISIBLE){
                        expandableLayout.setVisibility(View.GONE);

                    }

                    else if (expandableLayout.getVisibility() == View.GONE){
                        expandableLayout.setVisibility(View.VISIBLE);

                    }

                }
            });

        }
    }


    public PastWorkoutAdapter(ArrayList<String> TimeDateList, ArrayList<String> VolumeList, ArrayList<String> SetsList){

        this.TimeDateList = TimeDateList;
        this.VolumeList = VolumeList;
        this.SetsList = SetsList;


    }


    @Override
    public PastWorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewpast, parent, false);
        PastWorkoutViewHolder svh = new PastWorkoutViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(PastWorkoutAdapter.PastWorkoutViewHolder holder, int position) {




            holder.TimeDate.setText(TimeDateList.get(position));
            holder.Volume.setText(VolumeList.get(position));
            holder.SetInfo.setText(SetsList.get(position));







    }

    @Override
    public int getItemCount() {
        return TimeDateList.size();
    }







}
