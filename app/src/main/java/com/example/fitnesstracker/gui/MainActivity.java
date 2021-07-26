package com.example.fitnesstracker.gui;


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

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class is the sole activity in this app. It's responsibility is to switch out fragments on it's FrameLayout "fragment_container"
 *
 * Users can switch between fragments by selecting on the desired tab using the bottom navigation. The opening fragment will always be the "StartWorkoutFragment" class
 *
 * This class implements "MainActivityListener" which fragments/classes such as "StartWorkoutFragment", "FinishFragment", and "TimerFragment"
 * use in order to update the activity's mWorkoutStarted
 *
 * This class also initializes a textfile, "workouts.txt" which will be used to store all previous workouts from the user in internal storage.
 * The file is initialized here with an empty string, edited in "FinishWorkoutFragment", and read in "PastWorkoutsFragment" and "ProgressGraphFragment"
 */
public class MainActivity extends AppCompatActivity implements MainActivityListener{

    //indicates whether user is currently working out
    private boolean mWorkoutStarted = false;

    //internal storage file storing all workouts
    private static final String ALL_WORKOUTS = "workouts.txt";

    //TextView appears in app header only when user has started a workout
    private TextView Finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set Finish to invisible initially
        Finish = findViewById(R.id.finishText);
        Finish.setVisibility(View.GONE);


        //Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavWidget);

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


        //initializes "workouts.txt"
        try {
            setupWorkoutText();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //method initializes "workouts.txt" for internal storage
    private void setupWorkoutText() throws IOException {

        FileOutputStream fos = null;
        String initialize = "";

        fos = openFileOutput(ALL_WORKOUTS, MODE_APPEND);

        fos.write(initialize.getBytes());

    }


    //this method is part of MainActivityListener interface
    //this method is used by "StartWorkoutFragment" and "FinishWorkoutFragment" to indicate whether a user has started or finished a workout
    @Override
    public void updateWorkoutState(){

        mWorkoutStarted = !mWorkoutStarted;

        if (mWorkoutStarted){
            openWorkoutFragment();
        }

    }


    //this method is part of MainActivityListener interface
    //this method returns the current state the user (i.e. working out or not)
    @Override
    public boolean getWorkoutState(){

        return mWorkoutStarted;

    }


    //this method is initially called in onResume() to provide the appropriate entry screen for the user
    private void openStartWorkoutFragment(){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new StartWorkoutFragment())
                .addToBackStack(null)
                .commit();

    }


    //this method is only called by clicking on the StartWorkoutButton in the "StartWorkoutFragment"
    private void openWorkoutFragment(){

        //Find the current workoutFragment if user is already working out
        Fragment curr_frag = getSupportFragmentManager().findFragmentByTag("ViewPager");

        //else create a new one
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


    //this method is only called by clicking on "Past" on the Bottom Navigation
    public void openPastWorkoutsFragment(){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PastWorkoutsFragment())
                .addToBackStack(null)
                .commit();

    }


    //this method is only called by clicking on "Graph" on the Bottom Navigation
    public void openGraphFragment(){

         getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ProgressGraphFragment())
                .addToBackStack(null)
                .commit();

    }


    //Overriding method allows app to check if user is currently working out or not and opens up the corresponding fragment
    @Override
    public void onResume(){
        super.onResume();

        if (!mWorkoutStarted) {
            openStartWorkoutFragment();
        }

        else{
            openWorkoutFragment();
        }

    }


    //This method disables pressing the back button
    //As a Bottom Navigation already makes navigation easier, this was removed to ensure not hidden bugs would disrupt the user's experience
    @Override
    public void onBackPressed(){

        boolean allowBack = false;

        if (allowBack){
            super.onBackPressed();
        }

    }


    //This method is used to pop all fragments off the backstack when the user is finished with the app (e.g. used to avoid memory leakage)
    @Override
    public void onDestroy(){

        if (getSupportFragmentManager().getBackStackEntryCount()>0){

            Fragment WorkoutLogFrag = getSupportFragmentManager().findFragmentByTag("WorkoutFragment");
            Fragment FinishFrag = getSupportFragmentManager().findFragmentByTag("FinishFragment");

            if (WorkoutLogFrag != null && FinishFrag != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(FinishFrag)
                        .remove(WorkoutLogFrag)
                        .commitNow();
            }
        }

        super.onDestroy();

    }


}