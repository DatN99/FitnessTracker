package com.example.fitnesstracker.gui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WorkoutLogFragment extends Fragment implements addSetDialog.addSetDialogListener{

    View view;

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



    public WorkoutLogFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workout_log, container, false);


        //finding widgets
        addSet = view.findViewById(R.id.addSetButton);
        finishWorkout = view.findViewById(R.id.finishButton);


        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        finishWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWorkout();
            }
        });

        loadSets();
        //RecyclerView Setup
        buildRecyclerView();



        return view;
    }

    private void buildRecyclerView() {

        mRecyclerView = view.findViewById(R.id.workoutlogrecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
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
        dialog.show(getChildFragmentManager(), "editSet");


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
        Dialog.show(getChildFragmentManager(), "addSetDialog");


    }



    //add all sets to workout
    public void finishWorkout(){

        FinishFragment fragment = new FinishFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", SetsList);
        fragment.setArguments(bundle);





        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "FinishFragment")
                .addToBackStack("FinishFragment")
                .commit();



    }


    @Override
    public void sendSetInfo(String name, String reps, String weight) {

        Sets newSet = new Sets(name, reps, weight);
        SetsList.add(SetsList.size(), newSet);

        //udpdate RecyclerView
        mAdapter.notifyItemInserted(SetsList.size());
        mLayoutManager.scrollToPosition(SetsList.size());


        Toast.makeText(getContext(), "Set Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeSetInfo(String name, String reps, String weight, int position){

        Sets editSet = SetsList.get(position);
        editSet.setName(name);
        editSet.setReps(reps);
        editSet.setWeight(weight);

        mAdapter.notifyItemChanged(position);
        mLayoutManager.scrollToPosition(SetsList.size());

        Toast.makeText(getContext(), "Set Changed", Toast.LENGTH_SHORT).show();


    }



    public void loadSets(){

        SharedPreferences data = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
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


    }

    @Override
    public void onPause(){
        super.onPause();

        SharedPreferences data = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SetsList);
        editor.putString("All Sets", json);
        editor.apply();
    }




}