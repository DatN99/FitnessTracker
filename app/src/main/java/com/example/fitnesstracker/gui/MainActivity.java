package com.example.fitnesstracker.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.fitnesstracker.R;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

/**
 * Dummbell icon from: <div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 */


public class MainActivity extends AppCompatActivity {
    //Widget Variables
    MeowBottomNavigation bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.nav_pastworkouts){
                    openPastWorkoutsActivity();
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