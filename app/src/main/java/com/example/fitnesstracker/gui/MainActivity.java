package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitnesstracker.R;

public class MainActivity extends AppCompatActivity {
    //Widget Variables
    Button addWorkoutButton;
    Button pastWorkoutsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find Widgets
        addWorkoutButton = findViewById(R.id.addWorkout);
        pastWorkoutsButton = findViewById(R.id.previousWorkoutsButton);

        //Click to add a workout
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openWorkoutLogActivity();
            }
        });


        //Click to see past workouts
        pastWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPastWorkoutsActivity();
            }
        });

    }


    public void openWorkoutLogActivity(){
        Intent SwitchToWorkoutLog = new Intent(this, WorkoutLogActivity.class);
        startActivity(SwitchToWorkoutLog);

    }

    public void openPastWorkoutsActivity(){
        Intent SwitchToPastWorkouts = new Intent(this, PastWorkouts.class);
        startActivity(SwitchToPastWorkouts);

    }
}