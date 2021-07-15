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


public class MainActivity extends AppCompatActivity implements addSetDialog.addSetDialogListener{


    private Button startWorkout;
    private boolean workoutStarted;
    //Widget Variables
    MeowBottomNavigation bottomNav;

    //Widget variables
    private Button addSet;
    private Button finishWorkout;
    private FrameLayout addSetFragment1;

    //RecyclerView variables
    private RecyclerView mRecyclerView;
    private SetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Sets Variables
    ArrayList<Sets> SetsList = new ArrayList<>();



    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    addSetDialog Dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_startworkout);


        startWorkout = findViewById(R.id.startWorkoutButton);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutStarted = true;
                openWorkoutFrame();

            }
        });




        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                startWorkout.setVisibility(View.INVISIBLE);

                if (item.getItemId() == R.id.nav_pastworkouts){

                    openPastWorkoutsFragment();
                }

                else if (item.getItemId() == R.id.nav_progressgraph){
                    openGraphFragment();
                }

                else if (item.getItemId() == R.id.nav_home){
                    startWorkout.setVisibility(View.VISIBLE);

                    FragmentManager fm = getSupportFragmentManager();

                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });




    }

    private void openWorkoutFrame(){
        setContentView(R.layout.nav_activity_main);

        //finding widgets
        addSet = findViewById(R.id.addSetButton);
        finishWorkout = findViewById(R.id.finishButton);


        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        finishWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFinishActivity();
            }
        });
        //RecyclerView Setup
        buildRecyclerView();

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


    private void buildRecyclerView() {

        if (SetsList.size() == 0) {
            mRecyclerView = findViewById(R.id.workoutlogrecyclerView);
            mRecyclerView.setHasFixedSize(false);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new SetAdapter(SetsList);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }

        else{
            mAdapter = new SetAdapter(SetsList);
            mRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {
            @Override
            public void onAddItem(int position) {
                Sets copy = SetsList.get(position);
                addCopy(position+1, copy);
            }

            @Override
            public void onDeleteItem(int position){
                removeItem(position);
            }

            @Override
            public void onEditItem(int position){
                editItem(position);
            }
        });


    }

    public void editItem(int position){
        Sets editSet = SetsList.get(position);
        addSetDialog dialog = new addSetDialog();
        dialog.loadSetInfo(editSet, position);
        dialog.show(getSupportFragmentManager(), "editSet");


    }

    public void removeItem(int position){
        SetsList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void addCopy(int position, Sets copy){
        SetsList.add(position, copy);
        mAdapter.notifyItemInserted(position);
    }


    //opens fragment when "Add Set" button is clicked
    public void openDialog(){

        Dialog = new addSetDialog();
        Dialog.show(getSupportFragmentManager(), "addSetDialog");



    }



    //add all sets to workout
    public void openFinishActivity(){

        /**
        Intent SwitchToFinish = new Intent(this, FinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", SetsList);
        SwitchToFinish.putExtras(bundle);
        startActivity(SwitchToFinish);

        */
        FinishFragment fragment = new FinishFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", SetsList);
        fragment.setArguments(bundle);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();





    }


    @Override
    public void sendSetInfo(String name, String reps, String weight) {

        Sets newSet = new Sets(name, reps, weight);
        SetsList.add(SetsList.size(), newSet);

        //udpdate RecyclerView
        mAdapter.notifyItemInserted(SetsList.size());
        mLayoutManager.scrollToPosition(SetsList.size());


        Toast.makeText(this, "Set Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeSetInfo(String name, String reps, String weight, int position){

        Sets editSet = SetsList.get(position);
        editSet.setName(name);
        editSet.setReps(reps);
        editSet.setWeight(weight);

        mAdapter.notifyItemChanged(position);
        mLayoutManager.scrollToPosition(SetsList.size());

        Toast.makeText(this, "Set Changed", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onResume(){
        super.onResume();

        startWorkout.setVisibility(View.VISIBLE);

        if (startWorkout.getVisibility() == View.INVISIBLE){
            setContentView(R.layout.nav_activity_startworkout);

        }


        if (workoutStarted) {
            SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = data.getString("All Sets", null);
            try {
                if (!json.equals("[]")) {

                    Type type = new TypeToken<ArrayList<Sets>>() {
                    }.getType();
                    SetsList = gson.fromJson(json, type);

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            buildRecyclerView();
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        saveSets();

    }

    public void saveSets(){
        //Store in shared preferences to retrieve data in future
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SetsList);
        editor.putString("All Sets", json);
        editor.apply();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





}