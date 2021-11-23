package com.example.fitnesstracker.workout;

import android.content.SharedPreferences;
import android.icu.util.Calendar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class Workout {

    //Time variables used for key and searching up
    private int month;
    private int date;
    private int year;

    private int hour;
    private int minute;

    private String key;


    //Finished workout variables
    //Volume is in lbs
    private int volume;
    ArrayList<Sets> Sets;
    private String SetsStr;


    public Workout getWorkout(int date) {
        return null;
    }


    public void saveWorkout(ArrayList<Sets> SetsList) {

        Sets = SetsList;

        //Get current date to assign to workout
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);
        year = calendar.get(Calendar.YEAR);

        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        key = Integer.toString(month) + "/" + Integer.toString(date) + "/" + Integer.toString(year);

        calculateVolume();


    }

    private void calculateVolume() {
        for (Sets currSet : Sets) {
            volume += currSet.getRepsInt() * currSet.getWeightInt();
        }
    }

    //Format: HHMM
    //Note: 0 is used as a placeholder
    public String getTimeStr() {
        String hourStr;
        String minuteStr;

        if (hour < 10) {
            hourStr = "0" + Integer.toString(hour);
        } else {
            hourStr = Integer.toString(hour);
        }

        if (minute < 10) {
            minuteStr = "0" + Integer.toString(minute);
        } else {
            minuteStr = Integer.toString(minute);
        }

        return hourStr + minuteStr;
    }

    //returns volume as an int
    public String getVolumeStr() {
        return Integer.toString(volume);
    }

    //format: MMDDYYYY
    //Note: 0 is used as a placeholder
    public String getDateStr() {

        String monthStr;
        String dateStr;
        String yearStr;

        if (month < 10) {
            monthStr = "0" + Integer.toString(month);
        } else {
            monthStr = Integer.toString(month);
        }

        if (date < 10) {
            dateStr = "0" + Integer.toString(date);
        } else {
            dateStr = Integer.toString(date);
        }

        yearStr = Integer.toString(year);

        return monthStr + dateStr + yearStr;

    }

    public void getSetsStr(){
        SetsStr = "";

        for (Sets s : Sets){
            SetsStr+= s.getName() + ":   " + s.getReps() + "    " + s.getWeight();
            SetsStr += "\n";

        }
    }
}
