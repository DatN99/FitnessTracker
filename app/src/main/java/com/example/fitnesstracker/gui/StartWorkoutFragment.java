package com.example.fitnesstracker.gui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartWorkoutFragment extends Fragment {

    View view;



    public StartWorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_workout, container, false);

        return view;
    }
}