package com.example.fitnesstracker.workout;

import android.os.Parcel;
import android.os.Parcelable;

public class Sets implements Parcelable {

    private static String name;
    private static String reps;
    private static String weight;

    public Sets(String nameStr, String repsStr, String weightStr){
        name = nameStr;
        reps = repsStr;
        weight = weightStr;
    }

    protected Sets(Parcel in) {
        name = in.readString();
        reps = in.readString();
        weight = in.readString();
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
