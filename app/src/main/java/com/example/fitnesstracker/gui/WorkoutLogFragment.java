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


public class WorkoutLogFragment extends Fragment implements addSetDialog.addSetDialogListener{

    View view;

    //Widget variables
    private Button addSet;
    private Button finishWorkout;
    private TextView finish;


    //RecyclerView variables
    private RecyclerView mRecyclerView;
    private SetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Sets Variables
    ArrayList<Sets> SetsList = new ArrayList<>();

    addSetDialog Dialog;

    //Timer variables
    private TextView countdownText;
    private Button StartPause;
    private Button StopTimer;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    private long initialTime = 6000;
    private long timeLeft = initialTime;
    private ProgressBar timerProgress;



    public WorkoutLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_workout_log, container, false);

        setupTimer();

        finish = getActivity().findViewById(R.id.finishText);
        finish.setVisibility(View.VISIBLE);

        //finding widgets
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

        loadSets();

        //RecyclerView Setup
        buildRecyclerView();

        return view;
    }

    private void setupTimer(){

        countdownText = view.findViewById(R.id.countdownTextView);
        StartPause = view.findViewById(R.id.startpauseButton);

        StopTimer = view.findViewById(R.id.stopButton);
        StopTimer.setVisibility(View.GONE);

        timerProgress = view.findViewById(R.id.timerProgressBar);


        StartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning){
                    pauseTimer();
                }

                else{
                    startTimer();
                }
            }
        });

        StopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
            }
        });

        updateTimer();

    }

    private void pauseTimer(){
        countDownTimer.cancel();
        timerRunning=false;
        StartPause.setText("Resume");

    }



    private void startTimer(){

        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                initialTime=6000;
                timeLeft = initialTime;
                timerRunning = false;
                StartPause.setText("Start");
                StopTimer.setVisibility(View.GONE);
                updateTimer();

            }
        }.start();

        timerRunning = true;
        StartPause.setText("Pause");
        StopTimer.setVisibility(View.VISIBLE);

    }

    private void updateTimer(){

        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String updatedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        countdownText.setText(updatedTime);


        float timeLeftINT = (float) timeLeft;
        float initialTimeINT = (float) initialTime;

        float newtime = (timeLeftINT/initialTimeINT) * 100;

        int newtimeINT = (int) newtime;

        timerProgress.setProgress(newtimeINT);

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
            public void onEditItem(int position){
                editItem(position);
            }
        });



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);



    }

    Sets deletedSet = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();

            deletedSet = SetsList.get(position);
            removeItem(position);

            Snackbar.make(mRecyclerView, deletedSet.getName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SetsList.add(position, deletedSet);
                            mAdapter.notifyItemInserted(position);
                        }
                    }).show();
        }

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
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_top)
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