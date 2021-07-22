package com.example.fitnesstracker.gui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.R;


public class PageViewerFragment extends Fragment {

    View view;

    private ViewPager2 viewPager;
    private PageViewerAdapter pagerAdapter;

    WorkoutLogFragment WorkoutLogFrag;
    TimerFragment TimerFrag;


    public PageViewerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_page_viewer, container, false);

        viewPager = view.findViewById(R.id.ViewPager2Widget);

        WorkoutLogFrag = new WorkoutLogFragment();
        TimerFrag = new TimerFragment();


        pagerAdapter = new PageViewerAdapter(getActivity(), WorkoutLogFrag, TimerFrag);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(1);


        viewPager.setOffscreenPageLimit(2);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        WorkoutLogFrag.onPause();
    }
}