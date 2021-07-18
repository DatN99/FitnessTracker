package com.example.fitnesstracker.gui;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;


import com.example.fitnesstracker.R;
import com.google.android.material.navigation.NavigationView;


import org.jetbrains.annotations.NotNull;



public class MainActivity extends AppCompatActivity implements MainActivityListener{


    private boolean workoutStarted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                if (item.getItemId() == R.id.nav_pastworkouts){

                    openPastWorkoutsFragment();
                }

                else if (item.getItemId() == R.id.nav_progressgraph){
                    openGraphFragment();
                }

                else if (item.getItemId() == R.id.nav_home){
                    onResume();

                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
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