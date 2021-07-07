package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.example.fitnesstracker.workout.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class FinishActivity extends AppCompatActivity {
    //Widget variables



    //Set variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);


        Intent intent = getIntent();
        ArrayList<Sets> SetsList = intent.getExtras().getParcelableArrayList("Sets List");

        Workout workout = new Workout();
        workout.saveWorkout(SetsList);


    }


}