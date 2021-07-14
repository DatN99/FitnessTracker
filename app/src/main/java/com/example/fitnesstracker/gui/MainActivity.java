package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.fitnesstracker.R;


public class MainActivity extends AppCompatActivity {
    //Widget Variables
    MeowBottomNavigation bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find Widgets
        bottomNav = findViewById(R.id.MeowBottomNav);
        bottomNav.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));


        //Click to add a workout



        //Click to see past workouts


    }



    public void openWorkoutLogActivity(){
        Intent SwitchToWorkoutLog = new Intent(this, WorkoutLogActivity.class);
        startActivity(SwitchToWorkoutLog);

    }

    public void openPastWorkoutsActivity(){
        Intent SwitchToPastWorkouts = new Intent(this, PastWorkoutsActivity.class);
        startActivity(SwitchToPastWorkouts);

    }

    public void openGraphActivity(){
        Intent SwitchToGraph = new Intent(this, ProgressGraphActivity.class);
        startActivity(SwitchToGraph);
    }
}