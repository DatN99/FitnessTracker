package com.example.fitnesstracker.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;

public class addSetDialog extends AppCompatDialogFragment {

    private String name;
    private String reps;
    private String weight;


    //Widget variables
    private Button confirmSetButton;
    private TextInputLayout nameInput;
    private TextInputLayout repsInput;
    private TextInputLayout weightInput;
    private addSetDialogListener listener;

    public interface addSetDialogListener{
        void sendSetInfo(String name, String reps, String weight);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStanec) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view);
        builder.setTitle("Add Set");


        confirmSetButton = view.findViewById(R.id.confirmSetButton);
        nameInput = view.findViewById(R.id.nameTextInput);
        repsInput = view.findViewById(R.id.repsTextInput);
        weightInput = view.findViewById(R.id.weightTextInput);

        confirmSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateName() && validateReps() && validateWeight()){
                    listener.sendSetInfo(name, reps, weight);
                    dismiss();

                }
            }
        });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (addSetDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement addSetDialogListener");
        }
    }


    //method for exercise name limitations and regulations
    protected boolean validateName() {
        //String value of user input in "Name"
        name = nameInput.getEditText().getText().toString();

        //Name field cannot be empty
        if (name.equals("")){
            nameInput.setError("Field can't be empty");
            return false;
        }

        //Valid Input
        else {
            nameInput.setError(null);
            return true;
        }
    }

    //method for reps limitations and regulations
    protected boolean validateReps(){
        //String value of user input in "Reps"
        reps = repsInput.getEditText().getText().toString().trim();

        boolean containsLetters = reps.matches("[0-9]+");

        //Reps field cannot be empty
        if (reps.isEmpty()){
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
            return true;
        }
    }

    //method for weight name limitations and regulations
    protected boolean validateWeight(){

        //String value of user input in "Weight (lbs)"
        weight = weightInput.getEditText().getText().toString().trim();

        boolean containsLetters = weight.matches("[0-9]+");

        //Weight field cannot be empty
        if (weight.isEmpty()){
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
            return true;
        }
    }


}
