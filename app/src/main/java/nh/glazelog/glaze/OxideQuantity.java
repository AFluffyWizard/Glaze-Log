package nh.glazelog.glaze;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;

/**
 * Created by Nick Hansen on 9/22/2017.
 */

public class OxideQuantity implements Parcelable {

    private Oxide oxide;
    private double amount; // in grams, per 100g batch of the ingredient this is in

    public OxideQuantity (Oxide oxide, double amount) {
        this.oxide = oxide;
        this.amount = amount;
    }

    public void setOxide (Oxide oxide) {this.oxide = oxide;}
    public void setAmount (double Amount) {this.amount = amount;}
    public Oxide getOxide () {return oxide;}
    public double getAmount () {return amount;}


    // STRING FORMAT EXAMPLE:   ZnO:20:
    public String toString() {
        return oxide.getAbbreviation() + DbHelper.SHORT_SEP + amount + DbHelper.SHORT_SEP;
    }

    public static String toLongString (OxideQuantity... data) {
        String longString = "";
        for (int i = 0; i < data.length; i++) {
            longString += data[i] + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static String toLongString (ArrayList<OxideQuantity> data) {
        String longString = "";
        for (int i = 0; i < data.size(); i++) {
            longString += data.get(i) + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static OxideQuantity parseFromString (String s) {
        String[] data = Util.stringToArray(s, DbHelper.SHORT_SEP);
        //for (String str : data) System.out.println("AFTER PARSING SHORT: " + str);
        return new OxideQuantity(Oxide.getEnum(data[0]),Double.parseDouble(data[1]));
    }

    public static ArrayList<OxideQuantity> parseFromLongString (String s) {
        String[] separateOxides = Util.stringToArray(s, DbHelper.LONG_SEP);
        //for (String str : separateRampHolds) System.out.println("AFTER PARSING LONG: " + str);
        ArrayList<OxideQuantity> list = new ArrayList<>();
        for (String str : separateOxides)
            list.add(OxideQuantity.parseFromString(str));
        return list;
    }


    // parcelable implementation
    public OxideQuantity(Parcel in) {
        oxide = Oxide.getEnum(in.readString());
        amount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oxide.getAbbreviation());
        dest.writeDouble(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OxideQuantity> CREATOR = new Creator<OxideQuantity>() {
        @Override
        public OxideQuantity createFromParcel(Parcel in) {
            return new OxideQuantity(in);
        }

        @Override
        public OxideQuantity[] newArray(int size) {
            return new OxideQuantity[size];
        }
    };
}
