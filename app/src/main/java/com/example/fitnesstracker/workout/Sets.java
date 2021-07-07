package com.example.fitnesstracker.workout;

import android.os.Parcel;
import android.os.Parcelable;

public class Sets implements Parcelable {

    private String name;
    private String reps;
    private String weight;

    public Sets(String nameStr, String repsStr, String weightStr){

        this.name = nameStr;
        this.reps = repsStr;
        this.weight = weightStr;

    }

    protected Sets(Parcel in) {
        name = in.readString();
        reps = in.readString();
        weight = in.readString();
    }

    public String getName(){
        return name;
    }

    public String getReps(){
        return reps;
    }

    public String getWeight(){
        return weight;
    }

    public static final Creator<Sets> CREATOR = new Creator<Sets>() {
        @Override
        public Sets createFromParcel(Parcel in) {
            return new Sets(in);
        }

        @Override
        public Sets[] newArray(int size) {
            return new Sets[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(reps);
        dest.writeString(weight);
    }
}
