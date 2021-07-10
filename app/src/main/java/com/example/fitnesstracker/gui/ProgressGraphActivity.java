package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fitnesstracker.R;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class ProgressGraphActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);
    }
}