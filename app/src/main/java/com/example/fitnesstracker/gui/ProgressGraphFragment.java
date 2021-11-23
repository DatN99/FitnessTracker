package com.example.fitnesstracker.gui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

/**
 * This Fragment is responsible for showing a graph of user's volume over a function of time
 *
 * Features:
 * y-axis = volume
 * x-axis = time/date
 *
 * x-axis labels are rotated for neatness
 */
public class ProgressGraphFragment extends Fragment {

    View view;

    //LineChart from Mike Phil Android Chart
    private LineChart Chart;

    //String representation of workouts.txt (holds data on all previous workouts)
    private ArrayList<String> WorkoutList = new ArrayList<String>();
    private ArrayList<String> VolumeList = new ArrayList<String>();
    private ArrayList<String> TimeDateList = new ArrayList<String>();

    private String workoutStr = "";

    public ProgressGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_progress_graph, container, false);

        //Chart initial setup
        Chart = view.findViewById(R.id.progressChartView);
        Chart.setDragEnabled(true);
        Chart.setScaleEnabled(false);

        //get Workout from internal storage
        getWorkouts();

        //get time/date and volume from WorkoutList
        getTimeDateArray();

        //add y-values
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < WorkoutList.size(); i++){

            values.add(new Entry(i, Integer.parseInt(VolumeList.get(i))));

        }

        //Connect y-values to LineDataSet
        LineDataSet set = new LineDataSet(values, "Volume");
        ArrayList<ILineDataSet> dataset = new ArrayList<>();
        dataset.add(set);
        LineData data = new LineData(dataset);

        //add LineDataSet to Chart
        Chart.setData(data);

        //get x values
        ArrayList<String> s = new ArrayList<String>();

        for (int i = 0; i < VolumeList.size(); i++){
            s.add(TimeDateList.get(i));
        }

        //Chart axis configurations
        Chart.setVisibleXRangeMaximum(TimeDateList.size());

        YAxis yAxisRight = Chart.getAxisRight();
        yAxisRight.setEnabled(false);

        XAxis xAxis = Chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);

        xAxis.setLabelCount(s.size(), true);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setCenterAxisLabels(false);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(s));

        Chart.getXAxis().setGranularityEnabled(true);
        Chart.getAxisLeft().setGranularity(1f);

        //show Chart
        Chart.invalidate();

        return view;
    }

    //method gets all data from file input from internal storage and adds all lines to WorkoutList
    private void getWorkouts() {
        FileInputStream fis = null;

        String currLine = "";

        try {

            //open file
            fis = getActivity().openFileInput("workouts.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            //add lines to sb
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            //add sb to workoutStr
            workoutStr = sb.toString();

            //add workoutStr to WorkoutList
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

    //method gets all time/date and volume info from WorkoutList
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

                //get Volume
                if (i == 11) {
                    i++;

                    while (s.charAt(i) != '|') {
                        currVolume += s.charAt(i);
                        i++;
                    }
                }
            }

            //add to array
            TimeDateList.add(currDate);
            VolumeList.add(currVolume);
            currVolume="";
            currDate="";

        }

    }

}