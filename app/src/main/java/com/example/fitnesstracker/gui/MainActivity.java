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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find Widgets
        addWorkoutButton = findViewById(R.id.addWorkout);


        //Click to add a workout
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWorkoutLogActivity();
            }
        });
    }


    public void openWorkoutLogActivity(){
        Intent SwitchToWorkoutLog = new Intent(this, WorkoutLogActivity.class);
        startActivity(SwitchToWorkoutLog);

    }
}