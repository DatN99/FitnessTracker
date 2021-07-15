package com.example.fitnesstracker.gui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProgressGraphFragment extends Fragment {

    View view;

    private LineChart Chart;


    ArrayList<String> WorkoutList = new ArrayList<String>();
    ArrayList<String> VolumeList = new ArrayList<String>();
    ArrayList<String> TimeDateList = new ArrayList<String>();

    private String workoutStr = "";
    private int totalWorkouts = 0;


    public ProgressGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nav_progress, container, false);


        Chart = view.findViewById(R.id.progressChartView);
        Chart.setDragEnabled(true);
        Chart.setScaleEnabled(false);


        getWorkouts();
        getTimeDateArray();

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < WorkoutList.size(); i++){

            values.add(new Entry(i, Integer.parseInt(VolumeList.get(i))));
        }

        LineDataSet set = new LineDataSet(values, "Data Set");

        ArrayList<ILineDataSet> dataset = new ArrayList<>();
        dataset.add(set);

        LineData data = new LineData(dataset);

        Chart.setData(data);


        ArrayList<String> s = new ArrayList<String>();

        for (int i = 0; i < VolumeList.size(); i++){
            s.add(TimeDateList.get(i));
        }

        XAxis xAxis = Chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);

        xAxis.setLabelCount(s.size(), true);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(s));

        Chart.invalidate();
        return view;
    }

    public void getWorkouts() {
        FileInputStream fis = null;

        String currLine = "";

        try {
            fis = getActivity().openFileInput("workouts.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                totalWorkouts++;
            }

            workoutStr = sb.toString();

            for (int i = 0; i < workoutStr.length(); i++) {
                currLine += workoutStr.charAt(i);
                if (workoutStr.charAt(i) == '\n') {
                    WorkoutList.add(currLine);
                    currLine = "";
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fis.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void getTimeDateArray(){
        String currVolume = "";
        String currDate = "";


        for (String s : WorkoutList){
            currDate += "(";
            for (int i = 0; i < 12; i ++) {

                currDate += s.charAt(i);

                //Format: (XX:XX) MM/DD/YYYY
                if (i == 1){
                    currDate += ":";
                }
                if (i == 3){
                    currDate += ") ";
                }

                if (i == 5) {
                    currDate += "/";
                }

                if (i == 7){
                    currDate += "/";
                }

                if (i == 11) {
                    i++;

                    while (s.charAt(i) != '|') {
                        currVolume += s.charAt(i);
                        i++;
                    }
                }
            }


            TimeDateList.add(currDate);
            VolumeList.add(currVolume);
            currVolume="";
            currDate="";

        }

    }

}