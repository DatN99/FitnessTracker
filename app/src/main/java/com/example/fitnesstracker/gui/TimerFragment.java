package com.example.fitnesstracker.gui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.R;

import java.util.Locale;

import javax.xml.parsers.SAXParser;


public class TimerFragment extends Fragment {

    View view;

    //Timer variables
    private TextView countdownText;
    private Button StartPause;
    private Button StopTimer;
    private ProgressBar timerProgress;
    private EditText selectTime;
    private Button confirmTime;

    //
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    //inital time and amount of time left in ms
    private long initialTime;
    private long timeLeft;

    //time in seconds...used only for updating progress bar
    private int curr_sec;
    private int initial_sec;

    private long mEndTime;

    private NotificationCompat.Builder notificationBuilder;

    NotificationManagerCompat managerCompat;

    boolean Paused;

    private int finished;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);

        System.out.println("ON CREATE VIEW");



        createNotificationChannel();


        //sets up widget associated with the countdownText timer
        setupTimer();

        loadTimer();

        //loads options for user to select time
        selectTime();

        return view;
    }



    public void buildNotification(String time){
        notificationBuilder.setSmallIcon(R.drawable.dumbbell)
                .setContentTitle("Title")
                .setContentText(time);

        managerCompat = NotificationManagerCompat.from(getActivity());
        managerCompat.notify(1, notificationBuilder.build());

    }

    //to be executed immediately upon timer creation
    public void createNotificationChannel(){
        notificationBuilder = new NotificationCompat.Builder(getActivity(), "1");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Time";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void selectTime(){

        selectTime = view.findViewById(R.id.timeSelectEditText);

        confirmTime = view.findViewById(R.id.setTimeButton);

        confirmTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //User must select a valid quantity of seconds
                String newTime = selectTime.getText().toString();

                boolean containsLetters = newTime.matches("[0-9]+");


                if (newTime.equals("") || !containsLetters){
                    selectTime.setError("Time must not contain letters");
                }

                else if (timerRunning){
                    selectTime.setError("Please cancel the current timer to make changes");
                }

                else {

                    //change initial time and timeLeft
                    initialTime = Integer.parseInt(newTime) * 1000;
                    timeLeft = initialTime;

                    //update seconds, timer, and default time
                    getSec();
                    updateTimer();
                    saveTime();


                    hideKeyboard();

                    selectTime.setText("");

                }
            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void setupTimer(){

        countdownText = view.findViewById(R.id.countdownTextView);
        StartPause = view.findViewById(R.id.startpauseButton);

        StopTimer = view.findViewById(R.id.stopButton);
        StopTimer.setVisibility(View.GONE);

        timerProgress = view.findViewById(R.id.timerProgressBar);
        timerProgress.setProgress(100);

        //called once on timer initial setup
        updateTimer();


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

                if (countDownTimer != null) {
                    //finishes and cancels timer
                    countDownTimer.onFinish();
                }

                else{
                    resetTimer();
                }


            }
        });


    }


    private void pauseTimer(){
        //cancels timer
        saveTime();
        countDownTimer.cancel();
        countDownTimer = null;
        timerRunning=false;
        StartPause.setText("Resume");

    }


    //only used for updating progress bar
    private void getSec(){

        curr_sec = (int) (timeLeft)/1000;
        initial_sec = (int) (initialTime)/1000 ;

        mEndTime = System.currentTimeMillis() + timeLeft;

    }


    private void startTimer(){

        //get current amount of seconds
        getSec();

        if (countDownTimer == null) {
            System.out.println("NEW COUNTDOWN TIMER CREATED");
            countDownTimer = new CountDownTimer(timeLeft, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    System.out.println(millisUntilFinished);

                    timeLeft = millisUntilFinished;


                    updateTimer();

                }

                @Override
                public void onFinish() {

                    countDownTimer.cancel();
                    countDownTimer = null;
                    finished++;
                    resetTimer();


                }
            }.start();
        }



        //set fields for a running timer
        timerRunning = true;
        StartPause.setText("Pause");
        StopTimer.setVisibility(View.VISIBLE);

    }

    private void resetTimer(){
        timeLeft = initialTime;
        timerRunning = false;
        StartPause.setText("Start");
        StopTimer.setVisibility(View.GONE);
        updateTimer();
        saveTime();
    }


    private void updateTimer(){


        if (curr_sec == 0){
            getSec();
        }


        //continue timer if not finished
        if (timerRunning) {

            int minutes = (int) (timeLeft / 1000) / 60;
            int seconds = (int) (timeLeft / 1000) % 60;

            //countdown Text only gets updated every second
            String updatedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            countdownText.setText(updatedTime);

        }


        //reset timer if timeLeft is 0 (i.e. 0 set by loadTimer())
        if (timeLeft == initialTime){

            int resetMinutes = (int) (initialTime / 1000) / 60;
            int resetSeconds = (int) (initialTime / 1000) % 60;

            String resetTime = String.format(Locale.getDefault(), "%02d:%02d", resetMinutes, resetSeconds);

            countdownText.setText(resetTime);
            timerProgress.setProgress(100);

        }




        int total_seconds = (int) (timeLeft)/1000;

        //progressbar only gets updated once every second
        if (curr_sec != total_seconds && timerRunning){

            float progressFloat = ((float) total_seconds/ (float) initial_sec) * 100;

            int progressInt = (int) progressFloat;


            timerProgress.setProgress(progressInt);

            curr_sec = total_seconds;

        }

        System.out.println("Paused:  "+Paused);
        if (Paused){
            buildNotification(countdownText.getText().toString());
        }

        else{
            if (managerCompat != null){
                managerCompat.cancel(1);
            }
        }


    }


    public void loadTimer(){


        //gather previously stored time
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        initialTime = prefs.getLong("initialTime", 60000);
        timeLeft = prefs.getLong("millisLeft", initialTime);
        timerRunning = prefs.getBoolean("timerRunning", false);


        if (finished >0){
            System.out.println("dddd");
        }

        //updates countdown TextView and ProgressBar
        updateTimer();



        //need to update timer again if timer has already finished
        if (timerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            timeLeft = mEndTime - System.currentTimeMillis();

            if (timeLeft < 0) {
                timeLeft = 0;
                timerRunning = false;
                updateTimer();

            }

            else {
                startTimer();
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        saveTime();

        Paused=true;

    }

    @Override
    public void onResume() {
        super.onResume();
        Paused=false;
    }

    //called anytime the user leaves the timer/stops the clocks or inputs a new time
    public void saveTime(){


        if (getActivity() !=null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("initialTime", initialTime);
            editor.putLong("millisLeft", timeLeft);
            editor.putBoolean("timerRunning", timerRunning);
            editor.putLong("endTime", mEndTime);
            editor.apply();

        }


    }

}