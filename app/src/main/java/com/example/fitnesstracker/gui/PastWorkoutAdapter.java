package com.example.fitnesstracker.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;

import java.util.ArrayList;

public class PastWorkoutAdapter extends RecyclerView.Adapter<PastWorkoutAdapter.PastWorkoutViewHolder>{
    private static ArrayList<String> TimeDateList;
    private static ArrayList<String> VolumeList;

    public static class PastWorkoutViewHolder extends RecyclerView.ViewHolder{

        public TextView TimeDate;
        public TextView Volume;


        public PastWorkoutViewHolder(View itemView) {
            super(itemView);

            TimeDate = itemView.findViewById(R.id.timedateTextView);
            Volume = itemView.findViewById(R.id.volumeTextView);

        }
    }

    public PastWorkoutAdapter(ArrayList<String> TimeDateList, ArrayList<String> VolumeList){

        this.TimeDateList = TimeDateList;
        this.VolumeList = VolumeList;

    }



    @Override
    public PastWorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewpast, parent, false);
        PastWorkoutViewHolder svh = new PastWorkoutViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(PastWorkoutAdapter.PastWorkoutViewHolder holder, int position) {


        String currTimeDate = TimeDateList.get(position);
        String currVolume = VolumeList.get(position);

        holder.TimeDate.setText(TimeDateList.get(position));
        holder.Volume.setText(VolumeList.get(position));

    }

    @Override
    public int getItemCount() {
        return TimeDateList.size();
    }
}
