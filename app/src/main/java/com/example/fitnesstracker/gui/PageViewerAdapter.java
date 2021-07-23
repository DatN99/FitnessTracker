package com.example.fitnesstracker.gui;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Timer;

public class PageViewerAdapter extends FragmentStateAdapter {


    WorkoutLogFragment WorkoutLogFrag;
    TimerFragment TimerFrag;




    public PageViewerAdapter(Fragment F, WorkoutLogFragment WLF, TimerFragment TF) {

        super(F);

        if (WorkoutLogFrag == null && TimerFrag == null) {
            WorkoutLogFrag = WLF;
            TimerFrag = TF;
        }
    }

    @Override
    public Fragment createFragment(int position) {

        if (position == 1){
            return WorkoutLogFrag;
        }

        else{
            return TimerFrag;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }



}
