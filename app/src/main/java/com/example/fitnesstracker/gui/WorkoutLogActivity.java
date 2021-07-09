package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WorkoutLogActivity extends AppCompatActivity implements addSetFragment.OnFragmentInteractionListener {

    //Widget variables
    private Button addSet;
    private Button finishWorkout;
    private FrameLayout addSetFragment1;

    //RecyclerView variables
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static int position = 0;

    //Sets Variables
    ArrayList<Sets> SetsList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_log);


        //finding Fragment layout
        addSetFragment1 = findViewById(R.id.addSetFragment);

        //finding widgets
        addSet = findViewById(R.id.addSetButton);
        finishWorkout = findViewById(R.id.finishButton);


        //RecyclerView Setup
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SetAdapter(SetsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        
    }


    //opens fragment when "Add Set" button is clicked
    public void openAddSetFragment(View V){

        addSetFragment fragment = addSetFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.addSetFragment, fragment, "New Fragment").commit();

    }


    //called after user selects "Confirm Set"
    @Override
    public void onFragmentInteraction(Sets CurrSet) {

        //add new CurrSet to SetsList
        SetsList.add(position, CurrSet);

        //udpdate RecyclerView
        mAdapter.notifyItemInserted(position);
        mLayoutManager.scrollToPosition(position);
        position++;

        Toast.makeText(this, "Set Added", Toast.LENGTH_SHORT).show();

        //Store in shared preferences to retrieve data in future
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SetsList);
        editor.putString("All Sets", json);
        editor.apply();
        onBackPressed();

    }


    //add all sets to workout
    public void openFinishActivity(View V){

        Intent SwitchToFinish = new Intent(this, FinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", SetsList);
        SwitchToFinish.putExtras(bundle);
        startActivity(SwitchToFinish);

    }
}