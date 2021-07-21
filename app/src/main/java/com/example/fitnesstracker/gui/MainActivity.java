package com.example.fitnesstracker.gui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;


import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.fitnesstracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements MainActivityListener{


    private boolean workoutStarted = false;


    private Button test;

    private TextView Finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavWidget);


        Finish = findViewById(R.id.finishText);
        Finish.setVisibility(View.GONE);


        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                if (item.getTitle().toString().equals("Workout")){
                    onResume();
                }

                else if (item.getTitle().toString().equals("Past")){
                    openPastWorkoutsFragment();
                }

                else if (item.getTitle().toString().equals("Graph")){
                    openGraphFragment();
                }

                return true;
            }
        });


    }







    @Override
    public void updateWorkoutState(){
        workoutStarted = !workoutStarted;

        if (workoutStarted){
            openWorkoutFragment();
        }

    }


    @Override
    public boolean getWorkoutState(){

        return workoutStarted;
    }


    private void openStartWorkoutFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new StartWorkoutFragment())
                .addToBackStack(null)
                .commit();

    }


    private void openWorkoutFragment(){

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.fragment_container, new WorkoutLogFragment(), "WorkoutFragment")
                .addToBackStack("WorkoutFragment")
                .commit();


    }


    public void openPastWorkoutsFragment(){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PastWorkoutsFragment())
                .addToBackStack(null)
                .commit();

    }


    public void openGraphFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ProgressGraphFragment())
                .addToBackStack(null)
                .commit();

    }


    @Override
    public void onResume(){
        super.onResume();

        if (!workoutStarted) {
            openStartWorkoutFragment();
        }

        else{
            openWorkoutFragment();
        }

    }


    @Override
    public void onBackPressed(){

        if (getSupportFragmentManager().findFragmentByTag("WorkoutFragment") != null && getSupportFragmentManager().findFragmentByTag("WorkoutFragment").isVisible()){

        }

        else if (getSupportFragmentManager().findFragmentByTag("FinishFragment") != null && getSupportFragmentManager().findFragmentByTag("FinishFragment").isVisible()){
            openStartWorkoutFragment();
        }

        else{
            super.onBackPressed();

        }

    }


    @Override
    public void onDestroy(){
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            Fragment WorkoutLogFrag = getSupportFragmentManager().findFragmentByTag("WorkoutFragment");
            Fragment FinishFrag = getSupportFragmentManager().findFragmentByTag("FinishFragment");
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(FinishFrag)
                    .remove(WorkoutLogFrag)
                    .commitNow();
        }

        super.onDestroy();

    }


}