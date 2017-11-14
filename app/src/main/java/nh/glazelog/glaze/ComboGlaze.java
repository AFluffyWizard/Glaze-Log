package nh.glazelog.glaze;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nh.glazelog.database.DBHelper;
import nh.glazelog.database.Storable;

/**
 * Created by Nick Hansen on 10/4/2017.
 */

public class ComboGlaze implements Parcelable,Storable {

    private String name;
    private String creationDate;
    private ArrayList<ComboGlazeInfo> glazes;
    private ArrayList<RampHold> firingCycle;
    private String primaryNotes;
    private String secondaryNotes;

    public ComboGlaze(String name, ArrayList<ComboGlazeInfo> glazes, ArrayList<RampHold> firingCycle, String primaryNotes, String secondaryNotes) {
        this.name = name;
        this.creationDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.glazes = glazes;
        this.firingCycle = firingCycle;
        this.primaryNotes = primaryNotes;
        this.secondaryNotes = secondaryNotes;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public ArrayList<ComboGlazeInfo> getGlazes() {return glazes;}
    public void setGlazes(ArrayList<ComboGlazeInfo> glazes) {this.glazes = glazes;}
    public ArrayList<RampHold> getFiringCycle() {return firingCycle;}
    public void setFiringCycle(ArrayList<RampHold> firingCycle) {this.firingCycle = firingCycle;}
    public String getPrimaryNotes() {return primaryNotes;}
    public void setPrimaryNotes(String primaryNotes) {this.primaryNotes = primaryNotes;}
    public String getSecondaryNotes() {return secondaryNotes;}
    public void setSecondaryNotes(String secondaryNotes) {this.secondaryNotes = secondaryNotes;}




    // storable implementation
    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public Storable.Type getStorableType() {
        return Type.COMBO;
    }
    public String getRowName() {return name;}
    public String getCreationDateRaw() {return creationDate;}

    public static final CursorCreator<ComboGlaze> CURSOR_CREATOR = new CursorCreator<ComboGlaze>() {
        @Override
        public ArrayList<ComboGlaze> createFromCursor(Cursor cursor) {
            return null;
        }
    };


    // parcelable implementation
    public ComboGlaze(Parcel in) {
        name = in.readString();
        glazes = in.createTypedArrayList(ComboGlazeInfo.CREATOR);
        firingCycle = in.createTypedArrayList(RampHold.CREATOR);
        primaryNotes = in.readString();
        secondaryNotes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(glazes);
        dest.writeTypedList(firingCycle);
        dest.writeString(primaryNotes);
        dest.writeString(secondaryNotes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ComboGlaze> CREATOR = new Creator<ComboGlaze>() {
        @Override
        public ComboGlaze createFromParcel(Parcel in) {
            return new ComboGlaze(in);
        }

        @Override
        public ComboGlaze[] newArray(int size) {
            return new ComboGlaze[size];
        }
    };
}
