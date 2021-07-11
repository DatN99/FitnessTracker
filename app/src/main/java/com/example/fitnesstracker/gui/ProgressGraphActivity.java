package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;

import com.example.fitnesstracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgressGraphActivity extends AppCompatActivity {

    private LineChart Chart;


    ArrayList<String> WorkoutList = new ArrayList<String>();
    ArrayList<String> VolumeList = new ArrayList<String>();
    ArrayList<String> TimeDateList = new ArrayList<String>();

    private String workoutStr = "";
    private int totalWorkouts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);


        Chart = findViewById(R.id.progressChartView);
        Chart.setDragEnabled(true);
        Chart.setScaleEnabled(false);

        /**
        //TEST IMAGE
        ArrayList<Entry> values1 = new ArrayList<>();

        values1.add(new Entry(0, 60f));
        values1.add(new Entry(1, 70f));
        values1.add(new Entry(2, 80f));

        LineDataSet set1 = new LineDataSet(values1, "Data Set1");

        set1.setFillAlpha(110);

        set1.setColor(Color.RED);

        ArrayList<ILineDataSet> dataset1 = new ArrayList<>();

        dataset1.add(set1);


        LineData data1 = new LineData(dataset1);


        Chart.setData(data1);

        Chart.invalidate();
        //TEST IMAGE
         */


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

    }


    public void getWorkouts() {
        FileInputStream fis = null;

        String currLine = "";

        try {
            fis = openFileInput("workouts.txt");
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