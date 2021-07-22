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

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    private long initialTime = 60000;
    private long timeLeft = initialTime;
    private ProgressBar timerProgress;


    private int curr_sec;
    private int initial_sec;

    private long mEndTime;



    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);


        setupTimer();




        return view;
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
                    startTimer();
                }
            }
        });

        StopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                countDownTimer.cancel();
                saveTime();

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
        /**START HERE. FIND A WAY FOR USER TO CUSTOMIZE TIME INPUT*/
        /**ALSO NEED TO FIND A WAY TO KEEP TIMER RUNNING IN BACKGROUND*/



        curr_sec = (int) (timeLeft);
        initial_sec = (int) (initialTime) ;

        mEndTime = System.currentTimeMillis() + timeLeft;



        countDownTimer = new CountDownTimer(timeLeft, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

                    initialTime = 60000;
                    timeLeft = initialTime;
                    timerRunning = false;
                    StartPause.setText("Start");
                    StopTimer.setVisibility(View.GONE);
                    updateTimer();
                    timerProgress.setProgress(100);


            }
        }.start();

        timerRunning = true;
        StartPause.setText("Pause");
        StopTimer.setVisibility(View.VISIBLE);

    }

    private void updateTimer(){

        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        int total_seconds = (int) (timeLeft);


        if (curr_sec != total_seconds && timerRunning){

            float progressFloat = ((float) total_seconds/ (float) initial_sec) * 100;

            int progressInt = (int) progressFloat;

            timerProgress.setProgress(progressInt);

            curr_sec = seconds;

        }


        String updatedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        countdownText.setText(updatedTime);


    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        timeLeft = prefs.getLong("millisLeft", initialTime);
        timerRunning = prefs.getBoolean("timerRunning", false);
        updateTimer();
        if (timerRunning) {
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

    public void saveTime(){
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", timeLeft);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        countDownTimer.cancel();
    }

}