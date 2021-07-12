package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WorkoutLogActivity extends AppCompatActivity implements addSetDialog.addSetDialogListener {

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
        mRecyclerView = findViewById(R.id.workoutlogrecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SetAdapter(SetsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


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
        });


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
    public void openAddSetFragment(View V){

        addSetDialog Dialog = new addSetDialog();
        Dialog.show(getSupportFragmentManager(), "addSetDialog");


        /**
        addSetFragment fragment = addSetFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.addSetFragment, fragment, "New Fragment").commit();
        */

    }


    //called after user selects "Confirm Set"
   // @Override
    public void onFragmentInteraction(Sets CurrSet) {

        /**
        //add new CurrSet to SetsList
        SetsList.add(SetsList.size(), CurrSet);


        //udpdate RecyclerView
        mAdapter.notifyItemInserted(SetsList.size());
        mLayoutManager.scrollToPosition(SetsList.size());


        Toast.makeText(this, "Set Added", Toast.LENGTH_SHORT).show();
        */


    }


    //add all sets to workout
    public void openFinishActivity(View V){

        Intent SwitchToFinish = new Intent(this, FinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", SetsList);
        SwitchToFinish.putExtras(bundle);
        startActivity(SwitchToFinish);

    }


    @Override
    public void sendSetInfo(String name, String reps, String weight) {


        SetsList.add(SetsList.size(), new Sets(name, reps, weight));


        //udpdate RecyclerView
        mAdapter.notifyItemInserted(SetsList.size());
        mLayoutManager.scrollToPosition(SetsList.size());


        Toast.makeText(this, "Set Added", Toast.LENGTH_SHORT).show();
    }
}