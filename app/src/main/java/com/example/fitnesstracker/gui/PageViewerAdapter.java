package com.example.fitnesstracker.gui;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Timer;


/**
 * This class is used by "PageViewerFragment" to allow the user to switch between "WorkoutLogFragment" and "TimerFragment"
 */
public class PageViewerAdapter extends FragmentStateAdapter {

    //Fragment Variables
    WorkoutLogFragment WorkoutLogFrag;
    TimerFragment TimerFrag;

    //initialized class variables
    public PageViewerAdapter(Fragment F, WorkoutLogFragment WLF, TimerFragment TF) {

        super(F);

        if (WorkoutLogFrag == null && TimerFrag == null) {
            WorkoutLogFrag = WLF;
            TimerFrag = TF;
        }
    }


    //creates corresponding variables
    @Override
    public Fragment createFragment(int position) {

        if (position == 1){
            return WorkoutLogFrag;
        }

        else{
            return TimerFrag;
        }

    }


    //returns number of items
    @Override
    public int getItemCount() {
        return 2;
    }


}
