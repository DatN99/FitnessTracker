package com.example.fitnesstracker.workout;

import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

public class Workout {

    private int month;
    private int date;
    private int year;

    private int hour;
    private int minute;

    private int volume;
    private Sets[] Sets;


    public Workout getWorkout(int date){
        return null;
    }


    public void saveWorkout(ArrayList<Sets> SetsList){


        //Get current date to assign to workout
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        year = calendar.get(Calendar.YEAR);

        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

    }
}
