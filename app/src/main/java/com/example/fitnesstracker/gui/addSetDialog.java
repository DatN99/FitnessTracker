package com.example.fitnesstracker.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;

/**
 * This class opens up a Dialog Fragment when user either wants to add a new set or edit an already created set
 *
 * Features:
 * Upon adding a new set, all edittext fields will be empty
 * Once the user presses "Confirm Set", the Dialog Fragment disappears
 *
 * Upon editing an already created set, all edittext fields will be automatically set to that set's attributes
 */

public class addSetDialog extends AppCompatDialogFragment {

    //Set variables
    private String name;
    private String reps;
    private String weight;
    private Sets set = null;

    //Widget variables
    private Button confirmSetButton;
    private TextInputLayout nameInput;
    private TextInputLayout repsInput;
    private TextInputLayout weightInput;

    //DialogFragment variables
    private addSetDialogListener listener;
    private int position;


    public interface addSetDialogListener{
        void sendSetInfo(String name, String reps, String weight);
        void changeSetInfo(String name, String reps, String weight, int position);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStanec) {

        //set up builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view);
        builder.setTitle("New Set");
        builder.create();

        //find widgets
        nameInput = view.findViewById(R.id.nameTextInput);
        repsInput = view.findViewById(R.id.repsTextInput);
        weightInput = view.findViewById(R.id.weightTextInput);
        confirmSetButton = view.findViewById(R.id.confirmSetButton);

        //when user edits a set
        if (set != null){
            nameInput.getEditText().setText(set.getName());
            repsInput.getEditText().setText(set.getReps());
            weightInput.getEditText().setText(set.getWeight());
        }

        //confirm new set info
        confirmSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //All edittext fields must have valid inputs
                if (set != null){
                    if (validateName() && validateReps() && validateWeight()){

                        //send back to "WorkoutLogFragment"
                        listener.changeSetInfo(name, reps, weight, position);
                        dismiss();

                    }
                }

                else if (validateName() && validateReps() && validateWeight()){

                    //send back to "WorkoutLogFragment"
                    listener.sendSetInfo(name, reps, weight);
                    dismiss();

                }
            }
        });

        //show Dialog Fragment
        return builder.show();

    }


    //method sets up listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (addSetDialogListener) getParentFragment();
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

    public void loadSetInfo(Sets setobj, int pos){
        position = pos;
        set = setobj;

    }

    public TextInputLayout getNameTextInput(){
        return nameInput;
    }


}
