package nh.glazelog.glaze;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;

/**
 * Created by Nick Hansen on 10/4/2017.
 */

public class ComboGlazeInfo implements Parcelable {

    private String glazeName;
    private String notes;

    public ComboGlazeInfo(String glazeName, String notes) {
        this.glazeName = glazeName;
        this.notes = notes;
    }

    public String getGlazeName() {return glazeName;}
    public void setGlazeName(String glazeName) {this.glazeName = glazeName;}
    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}

    public String toString() {
        return glazeName + DbHelper.SHORT_SEP + notes + DbHelper.SHORT_SEP;
    }

    public static String toLongString (ComboGlazeInfo... data) {
        String longString = "";
        for (int i = 0; i < data.length; i++) {
            longString += data[i] + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static String toLongString (ArrayList<ComboGlazeInfo> data) {
        String longString = "";
        for (int i = 0; i < data.size(); i++) {
            longString += data.get(i) + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static ComboGlazeInfo parseFromString (String s) {
        String[] data = Util.stringToArray(s, DbHelper.SHORT_SEP);
        //for (String str : data) System.out.println("AFTER PARSING SHORT: " + str);
        return new ComboGlazeInfo (data[0], data[1]);
    }

    public static ArrayList<ComboGlazeInfo> parseFromLongString (String s) {
        String[] separateComboGlazeInfos = Util.stringToArray(s, DbHelper.LONG_SEP);
        //for (String str : separateComboGlazeInfos) System.out.println("AFTER PARSING LONG: " + str);
        ArrayList<ComboGlazeInfo> list = new ArrayList<>();
        for (String str : separateComboGlazeInfos)
            list.add(ComboGlazeInfo.parseFromString(str));
        return list;
    }


    // parcelable implementation
    public ComboGlazeInfo(Parcel in) {
        glazeName = in.readString();
        notes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(glazeName);
        dest.writeString(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ComboGlazeInfo> CREATOR = new Creator<ComboGlazeInfo>() {
        @Override
        public ComboGlazeInfo createFromParcel(Parcel in) {
            return new ComboGlazeInfo(in);
        }

        @Override
        public ComboGlazeInfo[] newArray(int size) {
            return new ComboGlazeInfo[size];
        }
    };
}
