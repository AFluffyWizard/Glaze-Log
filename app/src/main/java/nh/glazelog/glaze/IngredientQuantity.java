package nh.glazelog.glaze;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;

/**
 * Created by Nick Hansen on 10/17/2017.
 */

public class IngredientQuantity implements Parcelable {

    private IngredientEnum ingredientEnum;
    private double amount; // in grams, per 100g batch of the ingredientEnum this is in

    public IngredientQuantity (IngredientEnum ingredientEnum, double amount) {
        this.ingredientEnum = ingredientEnum;
        this.amount = amount;
    }

    public IngredientQuantity (String ingredient, String amount) {
        this.ingredientEnum = IngredientEnum.getEnum(ingredient);
        if (!amount.equals("")) this.amount = Double.parseDouble(amount);
    }

    public void setIngredientEnum(IngredientEnum ingredientEnum) {this.ingredientEnum = ingredientEnum;}
    public void setAmount (double Amount) {this.amount = amount;}
    public IngredientEnum getIngredientEnum() {return ingredientEnum;}
    public double getAmount () {return amount;}


    // STRING FORMAT EXAMPLE:   Ferro Frit 3124:18:
    public String toString() {
        return ingredientEnum.toString() + DbHelper.SHORT_SEP + amount + DbHelper.SHORT_SEP;
    }

    public static String toLongString (IngredientQuantity... data) {
        String longString = "";
        for (int i = 0; i < data.length; i++) {
            longString += data[i] + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static String toLongString (ArrayList<IngredientQuantity> data) {
        String longString = "";
        for (int i = 0; i < data.size(); i++) {
            longString += data.get(i) + DbHelper.LONG_SEP;
        }
        return longString;
    }

    public static IngredientQuantity parseFromString (String s) {
        String[] data = Util.stringToArray(s, DbHelper.SHORT_SEP);
        //for (String str : data) System.out.println("AFTER PARSING SHORT: " + str);
        return new IngredientQuantity(data[0],data[1]);
    }

    public static ArrayList<IngredientQuantity> parseFromLongString (String s) {
        String[] separateIngredients = Util.stringToArray(s, DbHelper.LONG_SEP);
        //for (String str : separateRampHolds) System.out.println("AFTER PARSING LONG: " + str);
        ArrayList<IngredientQuantity> list = new ArrayList<>();
        for (String str : separateIngredients)
            list.add(IngredientQuantity.parseFromString(str));
        return list;
    }


    /*
    public static ContentValues getCvToSave (ArrayList<RampHold> rampHolds, int index, RampHold newRampHold) {
        ContentValues toSave = new ContentValues();
        rampHolds.set(index,newRampHold);
        toSave.put(DbHelper.CCN_FIRING_CYCLE,toLongString(rampHolds));
        return toSave;
    }

    public static ContentValues getCvToSave (String ingredientEnum, String quantity) {

    }
    */


    // parcelable implementation
    public IngredientQuantity(Parcel in) {
        ingredientEnum = IngredientEnum.getEnum(in.readString());
        amount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredientEnum.toString());
        dest.writeDouble(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IngredientQuantity> CREATOR = new Creator<IngredientQuantity>() {
        @Override
        public IngredientQuantity createFromParcel(Parcel in) {
            return new IngredientQuantity(in);
        }

        @Override
        public IngredientQuantity[] newArray(int size) {
            return new IngredientQuantity[size];
        }
    };
}

