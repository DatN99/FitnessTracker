package com.example.fitnesstracker.gui;

/**
 * This interface follows the Observer State Pattern in which multiple fragments observe the state change of "mWorkoutStarted" in MainActivity
 * "StartWorkoutFragment", "FinishFragment", and "TimerFragment" all use this interface to either observe or change "mWorkoutStarted"
 */
public interface MainActivityListener {

    //changes mWorkoutStarted accordingly
    void updateWorkoutState();

    //retrieve mWorkoutStarted
    boolean getWorkoutState();


}
