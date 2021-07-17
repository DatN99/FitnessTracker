package com.example.fitnesstracker.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fitnesstracker.R;


public class StartWorkoutFragment extends Fragment {

    View view;

    private Button startWorkout;

    private MainActivityListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_workout, container, false);

        startWorkout = view.findViewById(R.id.startWorkoutButton);

        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updateWorkoutState();
                startWorkout.setText("Continue Workout");
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MainActivityListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement MainActivityListener");
        }
    }
}