package nh.glazelog.glaze;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;

/**
 * Created by Nick Hansen on 9/22/2017.
 */

public class RampHold implements Parcelable {

    private double rate;
    private double temperature;
    private double hold; // in hours


    public RampHold() {
        rate = 0;
        temperature = 0;
        hold = 0;
    }

    public RampHold ( double rateOfChange, double temp, double holdTime) {
        rate = rateOfChange;
        temperature = temp;
        hold = holdTime;
    }

    public RampHold (String temp, String rateOfChange, String holdTime) {
        if (!rateOfChange.equals("")) rate = Double.parseDouble(rateOfChange);
        if (!temp.equals("")) temperature = Double.parseDouble(temp);
        if (!holdTime.equals("")) hold = Double.parseDouble(holdTime);
    }

    public void setRate (double rateOfChange) {rate = rateOfChange;}
    public void setTemperature (double temp) {temperature = temp;}
    public void setHold (double holdTime) {hold = holdTime;}
    public double getRate () {return rate;}
    public double getTemperature () {return temperature;}
    public double getHold () {return hold;}

    public static double getHighestTemp (ArrayList<RampHold> fc) {
        if (fc.size() == 0) return 0;
        ArrayList<Double> temps = new ArrayList<>();
        for (RampHold rh : fc) temps.add(rh.getTemperature());
        Collections.sort(temps,Collections.<Double>reverseOrder());
        return temps.get(0);
    }

    public String toString() {
        return rate + DbHelper.SHORT_SEP + temperature + DbHelper.SHORT_SEP + hold + DbHelper.SHORT_SEP;
    }

    public static String toLongString (RampHold... data) {
        String longString = "";
        for (int i = 0; i < data.length; i++) {
            longString += data[i] + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static String toLongString (ArrayList<RampHold> data) {
        String longString = "";
        for (int i = 0; i < data.size(); i++) {
            longString += data.get(i) + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static RampHold parseFromString (String s) {
        String[] data = Util.stringToArray(s, DbHelper.SHORT_SEP);
        //for (String str : data) System.out.println("AFTER PARSING SHORT: " + str);
        return new RampHold (data[0], data[1], data[2]);
    }

    public static ArrayList<RampHold> parseFromLongString (String s) {
        String[] separateRampHolds = Util.stringToArray(s, DbHelper.LONG_SEP);
        //for (String str : separateRampHolds) System.out.println("AFTER PARSING LONG: " + str);
        ArrayList<RampHold> list = new ArrayList<>();
        for (String str : separateRampHolds)
            list.add(RampHold.parseFromString(str));
        return list;
    }

    // parcelable implementation
    public RampHold(Parcel in) {
        temperature = in.readDouble();
        rate = in.readDouble();
        hold = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(temperature);
        dest.writeDouble(rate);
        dest.writeDouble(hold);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RampHold> CREATOR = new Creator<RampHold>() {
        @Override
        public RampHold createFromParcel(Parcel in) {
            return new RampHold(in);
        }

        @Override
        public RampHold[] newArray(int size) {
            return new RampHold[size];
        }
    };

}
