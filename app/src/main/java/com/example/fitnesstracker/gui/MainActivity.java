package com.example.fitnesstracker.gui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


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

        Fragment curr_frag = getSupportFragmentManager().findFragmentByTag("ViewPager");

        if (curr_frag == null){
            curr_frag = new PageViewerFragment();
        }


        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.fragment_container, curr_frag, "ViewPager")
                .addToBackStack("ViewPager")
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

        boolean allowBack = false;

        if (allowBack){
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