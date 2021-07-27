package com.example.fitnesstracker.gui;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitnesstracker.R;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class is responsible for showing all of the user's past workouts in a RecyclerView
 * The RecyclerView uses a reverse layout, so the top card shows the most recent workout
 *
 * Features:
 * Each card displays a (time) date and volume
 * Clicking on the down arrow will expand the card and show all of the users sets during that workout
 */
public class PastWorkoutsFragment extends Fragment {
    View view;

    //String representation of workouts.txt (holds data on all previous workouts)
    private ArrayList<String> WorkoutList = new ArrayList<String>();
    private ArrayList<String> VolumeList = new ArrayList<String>();
    private ArrayList<String> TimeDateList = new ArrayList<String>();
    private ArrayList<String> SetsList = new ArrayList<String>();

    private String workoutStr = "";
    private int totalWorkouts = 0;

    //RecyclerView variables
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static int position = 0;

    public PastWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_past_workouts, container, false);

        //display "No Workouts" if user has not yet completed a workout
        TextView noWorkouts = view.findViewById(R.id.noWorkoutsTextView);
        noWorkouts.setVisibility(View.GONE);

        //get all workouts into WorkoutList
        loadWorkouts();

        //get time/date and volume info
        getTimeDateArray();

        //get sets
        getSets();

        //display workouts
        showWorkouts();

        if (SetsList.size() == 0){
            noWorkouts.setVisibility(View.VISIBLE);
        }

        return view;

    }


    //method shows all past Workouts with a Reversed RecyclerView
    private void showWorkouts() {

        //RecyclerView Setup
        mRecyclerView = view.findViewById(R.id.pastworkoutsrecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mAdapter = new PastWorkoutAdapter(TimeDateList, VolumeList, SetsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(SetsList.size()-1);

    }


    //method gets all data from file input from internal storage and adds all lines to WorkoutList
    private void loadWorkouts(){
        FileInputStream fis = null;

        String currLine = "";

        try {

            //open file
            fis = getActivity().openFileInput("workouts.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            //add lines to sb
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                totalWorkouts++;
            }

            //add sb to workoutStr
            workoutStr = sb.toString();

            //add workoutStr to WorkoutList
            for (int i = 0; i < workoutStr.length(); i++) {
                currLine += workoutStr.charAt(i);

                if (workoutStr.charAt(i) == '\n') {
                    WorkoutList.add(currLine);
                    currLine = "";
                }

            }
        }

        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    //method gets all time/date and volume info from WorkoutList
    private void getTimeDateArray(){
        String currVolume = "";
        String currDate = "";

        for (String s : WorkoutList){
            currDate += "(";
            for (int i = 0; i < 12; i ++) {

                currDate += s.charAt(i);

                //Format: (XX:XX) MM/DD/YYYY
                if (i == 1){
                    currDate += ":";
                }
                if (i == 3){
                    currDate += ")  ";

                }

                if (i == 5) {
                    currDate += "/";
                }

                if (i == 7){
                    currDate += "/";
                }

                //get Volume
                if (i == 11) {
                    i++;

                    while (s.charAt(i) != '|') {
                        currVolume += s.charAt(i);
                        i++;
                    }
                }
            }

            //add to array
            TimeDateList.add(currDate);
            VolumeList.add(currVolume);
            currVolume="";
            currDate="";

        }
    }


    //method gets all set info from WorkoutList
    private void getSets(){

        for (String s: WorkoutList){
            int i = s.indexOf("|") +1;

            String currSet = "";

            //get each set and format
            while (i != s.length()-1){

                if (s.charAt(i) == '|') {
                    currSet += "\n\n";
                    i++;
                }

                if (s.charAt(i) == '-'){
                    currSet+="           ";
                    i++;
                }

                currSet += s.charAt(i);
                i++;
            }

            SetsList.add(currSet);

        }

    }





}