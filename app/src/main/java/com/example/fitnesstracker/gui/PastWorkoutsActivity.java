package com.example.fitnesstracker.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fitnesstracker.R;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class PastWorkoutsActivity extends AppCompatActivity {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_past_workouts);


        loadWorkouts();

        getTimeDateArray();

        getSets();


        showWorkouts();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    openMainActivity();
                }

                else if (item.getItemId() == R.id.nav_progressgraph){
                    openGraphActivity();
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void showWorkouts() {

        //RecyclerView Setup
        mRecyclerView = findViewById(R.id.pastworkoutsrecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PastWorkoutAdapter(TimeDateList, VolumeList, SetsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadWorkouts(){
        FileInputStream fis = null;

        String currLine = "";

        try {
            fis = openFileInput("workouts.txt");
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

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openGraphActivity(){
        Intent SwitchToGraph = new Intent(this, ProgressGraphActivity.class);
        startActivity(SwitchToGraph);
    }


}
