package com.example.fitnesstracker.gui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private int test;


    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);


        System.out.println("ON CREATE VIEW");


        onResume();


        return view;
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

                if (timerRunning){
                    selectTime.setError("Please cancel the current timer to make changes");
                }

                else{

                    //change initial time and timeLeft
                    initialTime = Integer.parseInt(newTime) * 1000;
                    timeLeft = initialTime;

                    //update seconds, timer, and default time
                    getSec();
                    updateTimer();
                    saveTime();

                }
            }
        });

    }


    private void setupTimer(){

        countdownText = view.findViewById(R.id.countdownTextView);
        StartPause = view.findViewById(R.id.startpauseButton);

        StopTimer = view.findViewById(R.id.stopButton);
        StopTimer.setVisibility(View.GONE);

        timerProgress = view.findViewById(R.id.timerProgressBar);


        timerProgress.setProgress(100);


        StartPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (timerRunning){
                    pauseTimer();
                }

                else{
                    if (test > 0){
                        System.out.println("f");
                    }
                    startTimer();
                }
            }
        });

        StopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finishes and cancels timer
                countDownTimer.onFinish();
                countDownTimer.cancel();

                timerRunning=false;
                saveTime();
                //updates timer to reset
                updateTimer();


            }
        });

        //called once on timer initial setup
        updateTimer();

    }

    private void pauseTimer(){
        //cancels timer
        countDownTimer.cancel();
        timerRunning=false;
        StartPause.setText("Resume");

    }

    private void getSec(){

        curr_sec = (int) (timeLeft)/1000;
        initial_sec = (int) (initialTime)/1000 ;

        mEndTime = System.currentTimeMillis() + timeLeft;
    }



    private void startTimer(){


        //get current amount of seconds
        getSec();

        countDownTimer = new CountDownTimer(timeLeft, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;

                updateTimer();
            }

            @Override
            public void onFinish() {

                timeLeft = initialTime;
                timerRunning = false;
                StartPause.setText("Start");
                StopTimer.setVisibility(View.GONE);
                updateTimer();
                saveTime();
                timerProgress.setProgress(100);


            }
        }.start();

        //set fields for a running timer
        timerRunning = true;
        StartPause.setText("Pause");
        StopTimer.setVisibility(View.VISIBLE);

    }

    private void updateTimer(){


        if (curr_sec == 0){
            getSec();
        }

        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        int total_seconds = (int) (timeLeft)/1000;


        //progressbar only gets updated once every second
        if (curr_sec != total_seconds && timerRunning){

            float progressFloat = ((float) total_seconds/ (float) initial_sec) * 100;

            int progressInt = (int) progressFloat;

            timerProgress.setProgress(progressInt);

            curr_sec = total_seconds;

        }

        //countdown Text only gets updated every second
        String updatedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);


        countdownText.setText(updatedTime);

        if (timeLeft == 0){

            int resetMinutes = (int) (initialTime / 1000) / 60;
            int resetSeconds = (int) (initialTime / 1000) % 60;


            String resetTime = String.format(Locale.getDefault(), "%02d:%02d", resetMinutes, resetSeconds);

            countdownText.setText(resetTime);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "Timer OnRESUME", Toast.LENGTH_SHORT).show();


        System.out.println("ON RESUME");


        //gather previously stored time
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        initialTime = prefs.getLong("initialTime", 60000);
        timeLeft = prefs.getLong("millisLeft", initialTime);
        timerRunning = prefs.getBoolean("timerRunning", false);
        updateTimer();


        if (timerRunning) {
            test++;
            mEndTime = prefs.getLong("endTime", 0);
            timeLeft = mEndTime - System.currentTimeMillis();
            if (timeLeft < 0) {
                timeLeft = 0;
                timerRunning = false;
                updateTimer();

            } else {
                startTimer();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        saveTime();

    }



    //called anytime the user leaves the timer/stops the clocks or inputs a new time
    public void saveTime(){
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("initialTime", initialTime);
        editor.putLong("millisLeft", timeLeft);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (timerRunning) {
            countDownTimer.cancel();
        }
    }

}