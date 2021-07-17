package com.example.fitnesstracker.gui;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;


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
                .replace(R.id.fragment_container, new WorkoutLogFragment(), "WorkoutLogFragment")
                .addToBackStack(null)
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

        if (getSupportFragmentManager().findFragmentByTag("WorkoutLogFragment") != null && getSupportFragmentManager().findFragmentByTag("WorkoutLogFragment").isVisible()){

        }

        else if (getSupportFragmentManager().findFragmentByTag("FinishFragment") != null && getSupportFragmentManager().findFragmentByTag("FinishFragment").isVisible()){

        }
        else{
            super.onBackPressed();

        }

    }





}