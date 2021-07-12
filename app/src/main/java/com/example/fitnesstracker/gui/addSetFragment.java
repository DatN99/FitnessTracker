package com.example.fitnesstracker.gui;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.workout.Sets;
import com.google.android.material.textfield.TextInputLayout;

public class addSetFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    //Widget variables
    private Button confirmSetButton;
    private TextInputLayout nameInput;
    private TextInputLayout repsInput;
    private TextInputLayout weightInput;

    //Sets variables
    String Name;
    String Reps;
    String Weight;

    //creates new instance of fragment
    public static addSetFragment newInstance() {
        addSetFragment fragment = new addSetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //constructor
    public addSetFragment() {
        // Required empty public constructor
    }

    //displays layout upon creating
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_set, container, false);

        //find widgets
        confirmSetButton = view.findViewById(R.id.confirmSetButton);
        nameInput = view.findViewById(R.id.nameTextInput);
        repsInput = view.findViewById(R.id.repsTextInput);
        weightInput = view.findViewById(R.id.weightTextInput);


        //if user selects "confirm set", go to confirmSet()
        confirmSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSet();
            }
        });


        return view;
    }

    public void sendBack(Sets newSet){
        if (mListener != null){
            mListener.onFragmentInteraction(newSet);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //method for exercise name limitations and regulations
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

    //method for reps limitations and regulations
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

    //method for weight name limitations and regulations
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
            Sets newSet = new Sets(Name, Reps, Weight);

            sendBack(newSet);

        }

    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Sets CurrSet);

    }
}