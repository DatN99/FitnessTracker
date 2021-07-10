package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.example.fitnesstracker.workout.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class FinishActivity extends AppCompatActivity {
    //Widget variables

    //Workout/Sets variables
    Workout workout;
    ArrayList<Sets> SetsList;

    //File Workout is saved to
    private static final String ALL_WORKOUTS = "workouts.txt";
    private String workoutStr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);


        Intent intent = getIntent();
        SetsList = intent.getExtras().getParcelableArrayList("Sets List");

        workout = new Workout();
        workout.saveWorkout(SetsList);

        setWorkoutStr();

        saveWorkout();



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
            fos = openFileOutput(ALL_WORKOUTS, MODE_APPEND);
            fos.write(workoutStr.getBytes());
            Toast.makeText(this, "Workout saved to " + getFilesDir() + "/" + ALL_WORKOUTS , Toast.LENGTH_SHORT).show();

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


}