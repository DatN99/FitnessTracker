package com.example.fitnesstracker.gui;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fitnesstracker.R;


/**
 * This fragment comes into play after the user has started working out after clicking "Start Workout" in "StartWorkoutFragment"
 *
 * A PageViewer is used to allow the user to switch between "WorkoutLogFragment" and "TimerFragment"
 * This way, the user can easily switch between seeing their completed sets and the timer before their next set
 *
 * This fragment implements "WorkoutLogFragment.WorkoutLogListener" to allow the user to touch the timer icon in the "WorkoutLogFragment" to switch to the "TimerFragment"
 *
 * Note: the PageViewer always starts on viewing "WorkoutLogFragment" and the user can view the "TimerFragment" by swiping from left to right
 */
public class PageViewerFragment extends Fragment implements WorkoutLogFragment.WorkoutLogListener {

    View view;

    //ViewPager variables
    private ViewPager2 viewPager;
    private PageViewerAdapter pagerAdapter;

    //Fragment Variables
    WorkoutLogFragment WorkoutLogFrag;
    TimerFragment TimerFrag;


    public PageViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_page_viewer, container, false);

        //create new Fragments
        WorkoutLogFrag = new WorkoutLogFragment();
        TimerFrag = new TimerFragment();

        //Set up ViewPager
        viewPager = view.findViewById(R.id.ViewPager2Widget);

        pagerAdapter = new PageViewerAdapter(this, WorkoutLogFrag, TimerFrag);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);


        return view;
    }


    //This method overrides onPause() allowing the app to also pause the corresponding fragments to make sure their instances are saved
    @Override
    public void onPause() {
        super.onPause();

        WorkoutLogFrag.onPause();

        TimerFrag.onPause();
    }


    //called in "WorkoutLogFragment" and switches the current page to 0
    @Override
    public void openTimer() {

        viewPager.setCurrentItem(0);

    }


}