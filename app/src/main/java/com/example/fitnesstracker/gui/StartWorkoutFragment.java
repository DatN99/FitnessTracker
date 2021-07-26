package com.example.fitnesstracker.gui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fitnesstracker.R;

/**
 * This fragment holds the opening screen for the user
 * To start a workout, the user simply presses the "Start Workout" Button
 * Following the button press, the "MainActivityListener" will change "mWorkoutStarted" in MainActivity
 */

public class StartWorkoutFragment extends Fragment {

    View view;

    //"Start Workout" Button
    private Button mStartWorkout;

    //listener for MainActivity
    private MainActivityListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_workout, container, false);

        //Setup "Start Workout" Button
        mStartWorkout = view.findViewById(R.id.startWorkoutButton);

        mStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change workout status and open WorkoutFragment
                mListener.updateWorkoutState();

            }
        });


        return view;
    }

    //this method is automatically called before onCreate and initializes "mlistener"
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (MainActivityListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement MainActivityListener");
        }
    }
}