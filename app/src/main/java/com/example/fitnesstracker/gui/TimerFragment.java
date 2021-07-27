package com.example.fitnesstracker.gui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

/**
 * This class contains a timer that the user can use to count down the time until their next set
 * Note: this fragment is page 0 of "PageViewerFragment"
 *
 * Upon entering this fragment, the user can start the timer immediately or they can set their own time in seconds.
 *
 * Timer Features:
 * Once started, the timer can be paused or stopped. If paused, the time left is saved while the countdowntimer is cancelled. Once resumed, a new countdowntimer is created with the time left.
 * If stopped, the countdowntimer is cancelled and the time left is reset.
 *
 * The timer has an extra progress bar display that allows the user to see the overall count down progress visualized on a circle
 *
 * Once a timer has started, the user can roam around the app or their phone and the countdowntimer will still continue
 * The timer will be continuously updated as a notification so the user can see the progress on their timer without having to stay on this fragment.
 * The user can click on this notification from outside of the fragment to re-enter the fragment
 */

public class TimerFragment extends Fragment {

    View view;

    //widget variables
    private TextView countdownText;
    private Button StartPause;
    private Button StopTimer;
    private ProgressBar timerProgress;
    private EditText selectTime;
    private Button confirmTime;

    //timer variables
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    //inital time and amount of time left in ms
    private long initialTime;
    private long timeLeft;
    private long mEndTime;

    //time in seconds...used only for updating progress bar
    private int curr_sec;
    private int initial_sec;


    //notification variables
    private NotificationCompat.Builder notificationBuilder;
    NotificationManagerCompat managerCompat;
    boolean Paused;
    private int finished;

    //listener variable
    private MainActivityListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);


        listener = (MainActivityListener) getActivity();

        //sets up channel for notifications to be sent through
        createNotificationChannel();

        //sets up widget associated with the countdownText timer
        setupTimer();

        loadTimer();

        //loads options for user to select time
        selectTime();

        return view;
    }


    //method is used to build the notification and send it
    public void buildNotification(String time){

        //only make notification if user is currently working out
        if (listener.getWorkoutState()== true) {
            Intent resultIntent = new Intent(getActivity(), MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(), 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //make notification and send it
            notificationBuilder.setSmallIcon(R.drawable.dumbbell)
                    .setContentTitle("Title")
                    .setContentText(time)
                    .setContentIntent(resultPendingIntent);


            managerCompat = NotificationManagerCompat.from(getActivity());
            managerCompat.notify(1, notificationBuilder.build());
        }

    }


    //to be executed immediately upon timer creation
    public void createNotificationChannel(){
        notificationBuilder = new NotificationCompat.Builder(getActivity(), "1");

        // Create the NotificationChannel if API > 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Time";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            //register notification channel with system
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


    //method allows users to select their own time in seconds
    private void selectTime(){

        //find widgets
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

                //user's time fits appropriate requirements
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

    //method is only called after user inputs a new time
    //method is used to hide the keyboard to save user the inconvenience of having to hide the keyboard themselves
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


    //method is used to set up timer's initial settings
    private void setupTimer(){

        //find widgets
        countdownText = view.findViewById(R.id.countdownTextView);
        StartPause = view.findViewById(R.id.startpauseButton);

        StopTimer = view.findViewById(R.id.stopButton);
        StopTimer.setVisibility(View.GONE);

        timerProgress = view.findViewById(R.id.timerProgressBar);
        timerProgress.setProgress(100);

        //called once on timer initial setup
        updateTimer();

        //start/resume timer
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

        //stop timer
        StopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //prevents cancellation of timer in case it is null
                if (countDownTimer != null) {
                    //finishes and cancels timer
                    countDownTimer.onFinish();
                }

                //if countdowntimer is null, call this method instead
                else{

                    resetTimer();

                }

            }
        });
    }


    //method is used to pause the timer
    private void pauseTimer(){
        //saves time when user clicked pause
        saveTime();

        //cancels timer and sets to null
        countDownTimer.cancel();
        countDownTimer = null;

        //timer is no longer running
        timerRunning=false;

        //change text of Start button
        StartPause.setText("Resume");

    }


    //only used for updating progress bar
    private void getSec(){

        //converts from ms to s
        curr_sec = (int) (timeLeft)/1000;
        initial_sec = (int) (initialTime)/1000 ;

        mEndTime = System.currentTimeMillis() + timeLeft;

    }


    //method is used to either start or restart timer
    private void startTimer(){

        //get current amount of seconds from ms
        getSec();

        //create a new countdowntimer
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(timeLeft, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    //update timeLeft
                    timeLeft = millisUntilFinished;

                    updateTimer();

                }

                @Override
                public void onFinish() {

                    //cancels timer and set to null
                    countDownTimer.cancel();
                    countDownTimer = null;

                    //resets timer to initial state
                    resetTimer();


                }
            }.start();
        }



        //set fields for a running timer
        timerRunning = true;
        StartPause.setText("Pause");
        StopTimer.setVisibility(View.VISIBLE);

    }

    //method used to set timer to initial state
    private void resetTimer(){

        //reset timeLeft
        timeLeft = initialTime;

        //update timer status
        timerRunning = false;

        //update widgets
        StartPause.setText("Start");
        StopTimer.setVisibility(View.GONE);

        //update timer and save current time to shared prefs
        updateTimer();
        saveTime();

    }


    //method is used to update the visuals of the timer widget and progress bar
    private void updateTimer(){

        //get current sec from ms
        if (curr_sec == 0){
            getSec();
        }

        //updates countdowntext (i.e. text in center of circle)
        if (timerRunning) {

            int minutes = (int) (timeLeft / 1000) / 60;
            int seconds = (int) (timeLeft / 1000) % 60;

            //countdown Text only gets updated every second
            String updatedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            countdownText.setText(updatedTime);

        }


        int total_seconds = (int) (timeLeft)/1000;

        //reset timer if timeLeft is 0 (i.e. 0 set by loadTimer())
        if (timeLeft == initialTime){

            int resetMinutes = (int) (initialTime / 1000) / 60;
            int resetSeconds = (int) (initialTime / 1000) % 60;

            String resetTime = String.format(Locale.getDefault(), "%02d:%02d", resetMinutes, resetSeconds);

            countdownText.setText(resetTime);
            timerProgress.setProgress(100);

        }


        //progressbar only gets updated every second (Note: timer will sometimes not update every second if progressFloat is the same as last progress
        //since timerProgress.setProgress(int i) only accepts ints. So 99.8% = 99.0% for example
        else if (curr_sec != total_seconds && timerRunning){

            //get ratio of total sec/initial sec
            float progressFloat = ((float) total_seconds/ (float) initial_sec) * 100;

            //convert to int
            int progressInt = (int) progressFloat;

            //set progress
            timerProgress.setProgress(progressInt);

            //update curr_sec
            curr_sec = total_seconds;

        }

        System.out.println("Paused:  "+Paused);

        //if fragment is paused, make a notification with the timer
        if (Paused){
            buildNotification(countdownText.getText().toString());
        }

        //if fragment is not paused, cancel the notification
        else{
            if (managerCompat != null){
                managerCompat.cancel(1);
            }
        }

    }


    //method loads timer from previously saved state before onPause()
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


    //method saves current time when onPause() is called
    @Override
    public void onPause() {
        super.onPause();

        saveTime();

        Paused=true;

    }

    //updated Paused for notification purposes
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