package com.example.fitnesstracker.gui;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.R;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class PastWorkoutsFragment extends Fragment {
    View view;

    //String representation of workouts.txt (holds data on all previous workouts)
    ArrayList<String> WorkoutList = new ArrayList<String>();
    ArrayList<String> VolumeList = new ArrayList<String>();
    ArrayList<String> TimeDateList = new ArrayList<String>();
    ArrayList<String> SetsList = new ArrayList<String>();

    private String workoutStr = "";
    private int totalWorkouts = 0;

    //RecyclerView variables
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static int position = 0;

    public PastWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_past_workouts, container, false);

        loadWorkouts();

        getTimeDateArray();

        getSets();

        showWorkouts();

        return view;

    }


    private void showWorkouts() {

        //RecyclerView Setup
        mRecyclerView = view.findViewById(R.id.pastworkoutsrecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PastWorkoutAdapter(TimeDateList, VolumeList, SetsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void loadWorkouts(){
        FileInputStream fis = null;

        String currLine = "";

        try {
            fis = getActivity().openFileInput("workouts.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                totalWorkouts++;
            }

            workoutStr = sb.toString();

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
                    currDate += ") ";
                }

                if (i == 5) {
                    currDate += "/";
                }

                if (i == 7){
                    currDate += "/";
                }

                if (i == 11) {
                    i++;

                    while (s.charAt(i) != '|') {
                        currVolume += s.charAt(i);
                        i++;
                    }
                }
            }

            TimeDateList.add(currDate);
            VolumeList.add(currVolume);
            currVolume="";
            currDate="";

        }
    }


    public void getSets(){

        for (String s: WorkoutList){
            int i = s.indexOf("|") +1;

            String currSet = "";

            while (i != s.length()-1){

                if (s.charAt(i) == '|') {
                    currSet += "\n";
                    i++;
                }

                currSet += s.charAt(i);
                i++;
            }

            SetsList.add(currSet);

        }

    }





}