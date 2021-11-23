package com.example.fitnesstracker.gui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.example.fitnesstracker.workout.Workout;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main purpose of this class is to save all aspects of the workout, and tag it with a time and date, in the user's internal storage
 * This fragment displays a finishing screen that enters from the top of the screen through an animation
 * The user can click the X at the top right corner to close this screen
 */
public class FinishFragment extends Fragment {

    View view;

    //Widget variables
    private TextView finish;
    private TextView exit;


    //Workout/Sets variables
    Workout workout;
    ArrayList<Sets> SetsList;

    //File Workout is saved to
    private String workoutStr;
    private static final String ALL_WORKOUTS = "workouts.txt";

    //listener variable
    private MainActivityListener listener;


    public FinishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_finish, container, false);

        //get rid of "FINISH" in app header
        finish = getActivity().findViewById(R.id.finishText);
        finish.setVisibility(View.GONE);

        //give functionality to exit TextView
        exit = view.findViewById(R.id.exitButton);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartWorkoutFragment();
            }
        });


        //get current status of MainActivity's workoutStarted
        boolean workoutStarted = listener.getWorkoutState();

        //open "StartWorkoutFragment" if user happens to land on this fragment without a workout being started
        if (!workoutStarted){
            openStartWorkoutFragment();
        }

        else {

            //update MainActivity's workoutStarted
            listener.updateWorkoutState();

            //get SetsList from "WorkoutLogActivity"
            if (getArguments() != null) {
                SetsList = getArguments().getParcelableArrayList("Sets List");
            }

            //if user actually inputted a set, save to internal storage
            if (SetsList.size() > 0) {

                //create new Workout obj and save the workout
                workout = new Workout();
                workout.saveWorkout(SetsList);

                //turn relevant info into string format
                setWorkoutStr();

                saveWorkout();
            }

            clearSets();

        }

        return view;
    }


    //method turns all relevant info from a workout into a string
    private void setWorkoutStr(){

        //get Workout's time and date
        workoutStr = workout.getTimeStr() + workout.getDateStr() + workout.getVolumeStr();

        //add all sets
        String setinfo = "";

        for (Sets currSet:SetsList){
            setinfo += "|"+currSet.getString();
        }

        //final String holding all relevant info
        workoutStr += setinfo +"\n";

    }

    //method saves workoutStr from setWorkoutStr to text file
    private void saveWorkout(){
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

    //method sets up listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MainActivityListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement MainActivityListener");

        }
    }

    //method empties sets from shared prefs
    private void clearSets(){

        SharedPreferences data = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        Gson gson = new Gson();
        SetsList.clear();
        String json = gson.toJson(SetsList);
        editor.putString("All Sets", json);

        editor.apply();
    }

    //method opens up "StartWorkoutFragment"
    private void openStartWorkoutFragment(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new StartWorkoutFragment())
                .addToBackStack(null)
                .commit();

    }


}