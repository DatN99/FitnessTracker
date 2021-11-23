package com.example.fitnesstracker.gui;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * This class contains the Recycler View used to display the user's sets in the current workout.
 * Note: this fragment is page 1 of "PageViewerFragment"
 *
 *
 * Upon entering this fragment, the app header bar will display a "FINISH" TextView that the user can click anywhere to finish their workout
 *
 * The user can also press the clock icon to enter the TimerFragment
 *
 * Recycler View Features:
 * Upon adding a new set, the new set will be displayed right below the previously added set
 * The user can add a duplicate set by clicking on the copy icon which will enter a new Sets object with the corresponding card
 * The user can edit a set by clicking on the pencil icon which will display an addSetDialog with that set's information already written in the corresponding fields
 * The user can delete a set by swiping from right to left on that specific set, and the deletion can be undone immediately by click "undo" on the bottom of the screen
 * Note: the RecyclerView uses "SetAdapter" to make any changes to the Set cards
 *
 * Dialog Features:
 * Upon clicking on the + icon, an dialog fragment will pop up allowing the user to enter the set's name, reps, and weight
 * The user can pick any name they want, but the reps and weight must be numbers otherwise the corresponding EditText will throw an error notifying the use to correct their input
 * Pressing "confirm set" will add the set to the SetsList and RecyclerView
 */
public class WorkoutLogFragment extends Fragment implements addSetDialog.addSetDialogListener{


    View view;

    //Widget variables
    private Button addSet;
    private TextView finish;
    private Button enterTimer;

    //RecyclerView variables
    private RecyclerView mRecyclerView;
    private SetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Sets variable which contains all of the Sets objects created by the user
    private ArrayList<Sets> mSetsList = new ArrayList<>();

    //Dialog variable which extends Dialog Fragment
    private addSetDialog mDialog;

    //Listener variable
    private WorkoutLogListener mListener;

    //interface uses the state pattern to notify the PageViewer that the user has touched the clock icon
    public interface WorkoutLogListener{
        void openTimer();
    }


    public WorkoutLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_workout_log, container, false);

        //Set "FINISH" to visible on app header
        finish = getActivity().findViewById(R.id.finishText);
        finish.setVisibility(View.VISIBLE);


        //Setting up widgets
        enterTimer = view.findViewById(R.id.entertimerButton);

        addSet = view.findViewById(R.id.addSetButton);

        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWorkout();
            }
        });

        enterTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (WorkoutLogFragment.WorkoutLogListener) getParentFragment();
                mListener.openTimer();
            }
        });

        //get all saved sets from shared prefs into SetsList
        loadSets();

        //RecyclerView Setup
        buildRecyclerView();

        return view;
    }


    //this method builds the RecyclerView and sets up the ItemTouchHelper to allow a "swipe to delete" feature on the sets
    private void buildRecyclerView() {

        mRecyclerView = view.findViewById(R.id.workoutlogrecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SetAdapter(mSetsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {

            //adds copy
            @Override
            public void onAddItem(int position) {

                Sets lCopy = mSetsList.get(position);

                addCopy(position+1, new Sets(lCopy.getName(), lCopy.getReps(), lCopy.getWeight()));

            }

            //edits item
            @Override
            public void onEditItem(int position){

                editItem(position);

            }
        });

        //Set up "swipe-to-delete" feature
        ItemTouchHelper lItemTouchHelper = new ItemTouchHelper(simpleCallback);
        lItemTouchHelper.attachToRecyclerView(mRecyclerView);



    }

    Sets lDeletedSet = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            //gets Set to be deleted and updated RecyclerView
            int position = viewHolder.getBindingAdapterPosition();

            lDeletedSet = mSetsList.get(position);

            removeItem(position);

            //Allows user to undo Set deletion
            Snackbar.make(mRecyclerView, lDeletedSet.getName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSetsList.add(position, lDeletedSet);
                            mAdapter.notifyItemInserted(position);
                        }
                    }).show();
        }


        //Sets up background for swiping (i.e. when swiping to delete, there will be a red background with a trash icon on display"
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light))
                    .addSwipeLeftActionIcon(R.drawable.workoutcarddelete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };



    //allows user to edit a Set card
    private void editItem(int position){

        //gets Set to be edited
        Sets lEditSet = mSetsList.get(position);

        //show lDialog with Set's info
        addSetDialog lDialog = new addSetDialog();
        lDialog.loadSetInfo(lEditSet, position);
        lDialog.show(getChildFragmentManager(), "editSet");

    }


    //allows user to remove item (used by ItemTouchHelper)
    private void removeItem(int position){

        //remove from SetsList
        mSetsList.remove(position);

        //remove from RecyclerView
        mAdapter.notifyItemRemoved(position);

    }


    //allows user to add a copy of the desired set
    private void addCopy(int position, Sets copy){

        //add to SetsList
        mSetsList.add(position, copy);

        //update RecyclerView
        mAdapter.notifyItemInserted(position);

    }


    //method is called when user hits "Confirm Set" Button
    @Override
    public void sendSetInfo(String name, String reps, String weight) {

        //creates new Set and adds to SetsList
        Sets lNewSet = new Sets(name, reps, weight);
        mSetsList.add(mSetsList.size(), lNewSet);

        //udpdate RecyclerView
        mAdapter.notifyItemInserted(mSetsList.size());
        mLayoutManager.scrollToPosition(mSetsList.size());

        Toast.makeText(getContext(), "Set Added", Toast.LENGTH_SHORT).show();
    }


    //method is called when user hits "Confirm Set" Button after editing a currently made set
    @Override
    public void changeSetInfo(String name, String reps, String weight, int position){

        //Get target set and change variables
        Sets lEditSet = mSetsList.get(position);
        lEditSet.setName(name);
        lEditSet.setReps(reps);
        lEditSet.setWeight(weight);

        //update RecyclerView
        mAdapter.notifyItemChanged(position);
        mLayoutManager.scrollToPosition(mSetsList.size());

        Toast.makeText(getContext(), "Set Changed", Toast.LENGTH_SHORT).show();

    }


    //opens fragment when "Add Set" button is clicked
    private void openDialog(){

        //show from "addSetDialog"
        mDialog = new addSetDialog();

        mDialog.show(getChildFragmentManager(), "addSetDialog");

    }


    //method calls for the finishing of a workout
    private void finishWorkout(){

        //pass SetsList into "FinishFragment"
        FinishFragment fragment = new FinishFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", mSetsList);
        fragment.setArguments(bundle);

        //open "FinishFragment"
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_top)
                .replace(R.id.fragment_container, fragment, "FinishFragment")
                .addToBackStack("FinishFragment")
                .commit();

    }


    //method is called whenever "WorkoutLogFragment" is back in view so RecyclerView can show already created sets
    private void loadSets(){

        //get Sets from shared prefs
        SharedPreferences lData = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = lData.getString("All Sets", null);

        try {

            if (!json.equals("[]")) {

                Type type = new TypeToken<ArrayList<Sets>>() {
                }.getType();
                mSetsList = gson.fromJson(json, type);

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    //all Sets will be saved whenever user leaves "WorkoutLogFragment"
    @Override
    public void onPause() {
        super.onPause();

        if (getActivity() != null) {

            //save SetsList to shared prefs
            SharedPreferences lData = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = lData.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mSetsList);
            editor.putString("All Sets", json);
            editor.apply();

        }

    }


}