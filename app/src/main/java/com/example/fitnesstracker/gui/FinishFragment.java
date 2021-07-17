package com.example.fitnesstracker.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.example.fitnesstracker.workout.Workout;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FinishFragment extends Fragment {

    View view;

    //Widget variables

    //Workout/Sets variables
    Workout workout;
    ArrayList<Sets> SetsList;

    //File Workout is saved to
    private static final String ALL_WORKOUTS = "workouts.txt";
    private String workoutStr;

    private MainActivityListener listener;



    public FinishFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.nav_finish, container, false);

        boolean workoutStarted = listener.getWorkoutState();


        if (!workoutStarted){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new StartWorkoutFragment())
                    .addToBackStack(null)
                    .commit();
        }

        else {

            listener.updateWorkoutState();


            if (getArguments() != null) {
                SetsList = getArguments().getParcelableArrayList("Sets List");
            }


            if (SetsList.size() > 0) {
                workout = new Workout();
                workout.saveWorkout(SetsList);

                setWorkoutStr();

                saveWorkout();
            }

            clearSets();
        }





        return view;
    }

    public void setWorkoutStr(){
        workoutStr = workout.getTimeStr() + workout.getDateStr() + workout.getVolumeStr();

        String setinfo = "";

        for (Sets currSet:SetsList){
            setinfo += "|"+currSet.getString();
        }

        workoutStr += setinfo +"\n";


    }

    public void saveWorkout(){
        FileOutputStream fos = null;

        try {

            //file saved to data/data/com.example.fitnesstracker/files/workouts.txt
            fos = getActivity().openFileOutput(ALL_WORKOUTS, getActivity().MODE_APPEND);
            fos.write(workoutStr.getBytes());
            Toast.makeText(getActivity(), "Workout saved to " + getActivity().getFilesDir() + "/" + ALL_WORKOUTS , Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public void clearSets(){
        SharedPreferences data = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        Gson gson = new Gson();
        SetsList.clear();
        String json = gson.toJson(SetsList);
        editor.putString("All Sets", json);
        editor.apply();
    }




}