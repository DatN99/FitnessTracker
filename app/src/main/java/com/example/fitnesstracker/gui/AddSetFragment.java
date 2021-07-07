package com.example.fitnesstracker.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Set;

public class AddSetFragment extends AppCompatDialogFragment {

    //Widget variables
    private Button confirmSetButton;
    private TextInputLayout nameInput;
    private TextInputLayout repsInput;
    private TextInputLayout weightInput;

    //Sets variables
    String Name;
    String Reps;
    String Weight;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.setfragment, null);
        builder.setView(view);

        //finding widgets
        confirmSetButton = view.findViewById(R.id.confirmSetButton);
        nameInput = view.findViewById(R.id.nameTextInput);
        repsInput = view.findViewById(R.id.repsTextInput);
        weightInput = view.findViewById(R.id.weightTextInput);

        confirmSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSet();
            }
        });

        return builder.create();
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

    public void confirmSet(){
        if (validateName() && validateReps() && validateWeight()){
            Sets test = new Sets(Name, Reps, Weight);

            Intent SwitchToFinish = new Intent(getActivity(), WorkoutLogActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("New Set", test);
            SwitchToFinish.putExtras(bundle);
            startActivity(SwitchToFinish);

        }
    }

}
