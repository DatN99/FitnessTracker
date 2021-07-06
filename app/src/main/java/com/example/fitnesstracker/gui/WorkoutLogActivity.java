package com.example.fitnesstracker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WorkoutLogActivity extends AppCompatActivity {


    //Widget variables
    private TextInputLayout nameInput;
    private TextInputLayout repsInput;
    private TextInputLayout weightInput;
    private Button confirmSet;
    private Button finishWorkout;

    //Sets Variables
    ArrayList<Sets> SetsList = new ArrayList<>();
    String Name;
    String Reps;
    String Weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_log);

        //finding widgets
        nameInput = findViewById(R.id.nameTextInput);
        repsInput = findViewById(R.id.repsTextInput);
        weightInput = findViewById(R.id.weightTextInput);
        confirmSet = findViewById(R.id.confirmSetButton);
        finishWorkout = findViewById(R.id.finishButton);

        
    }

    protected boolean validateName() {
        //String value of user input in "Name"
        Name = nameInput.getEditText().getText().toString().trim();

        //Name field cannot be empty
        if (Name.isEmpty()){
            nameInput.setError("Field can't be empty");
            return false;
        }

        //Valid Input
        else {
            nameInput.setError(null);
            nameInput.setErrorEnabled(false);
            return true;
        }
    }

    protected boolean validateReps(){
        //String value of user input in "Reps"
        Reps = repsInput.getEditText().getText().toString().trim();

        boolean containsLetters = Reps.matches("[0-9]+");


        //Reps field cannot be empty
        if (Reps.isEmpty()){
            repsInput.setError("Field can't be empty");
            return false;
        }

        //Reps field cannot have an alpha character
        else if (!containsLetters){
            repsInput.setError("Reps must be a valid number");
            return false;
        }

        //Valid Input
        else {
            repsInput.setError(null);
            repsInput.setErrorEnabled(false);
            return true;
        }
    }

    protected boolean validateWeight(){
        //String value of user input in "Weight (lbs)"
        Weight = weightInput.getEditText().getText().toString().trim();

        boolean containsLetters = Weight.matches("[0-9]+");

        //Weight field cannot be empty
        if (Weight.isEmpty()){
            weightInput.setError("Field can't be empty");
            return false;
        }

        //Weight field cannot have an alpha character
        else if (!containsLetters){
            weightInput.setError("Reps must be a valid number");
            return false;
        }

        //Valid Input
        else {
            weightInput.setError(null);
            weightInput.setErrorEnabled(false);
            return true;
        }
    }

    //called when Add Workout button is clicked
    public void addWorkout(View V){
        String input;

        if (validateName() && validateReps() && validateWeight()){
            //Create new Set Object and add to ArrayList SetsList
            SetsList.add(new Sets(Name, Reps, Weight));
            Toast.makeText(this, "Set Added", Toast.LENGTH_SHORT).show();

            //Store in shared preferences to retrieve data in future
            SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = data.edit();
            Gson gson = new Gson();
            String json = gson.toJson(SetsList);
            editor.putString("All Sets", json);
            editor.apply();

        }
    }

    //add all sets to workout
    public void openFinishActivity(View V){

        Intent SwitchToFinish = new Intent(this, FinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Sets List", SetsList);
        SwitchToFinish.putExtras(bundle);
        startActivity(SwitchToFinish);

    }
}