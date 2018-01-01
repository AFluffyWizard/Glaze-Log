package nh.glazelog.glaze;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.Storable;

/**
 * Created by Nick Hansen on 12/31/2017.
 */

public class FiringCycle implements Parcelable,Storable{

    private String name;
    private String dateCreated;
    private String dateEdited;
    private String tags;
    private ArrayList<RampHold> rampHolds;
    private String notes;


    public FiringCycle() {
        this.name = "";
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.rampHolds = new ArrayList<>();
        this.notes = "";
    }

    public FiringCycle(String name) {
        this();
        this.setName(name);
    }

    public FiringCycle (String name, String tags, ArrayList<RampHold> rampHolds, String notes) {
        this.name = name;
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = tags;
        this.rampHolds = rampHolds;
        this.notes = notes;
    }

    public FiringCycle (String name, String dateCreated, String dateEdited, String tags, ArrayList<RampHold> rampHolds, String notes) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = tags;
        this.rampHolds = rampHolds;
        this.notes = notes;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDateCreatedRaw() {return dateCreated;}
    public void setDateCreated(Date date) {this.dateCreated = date.toString();}
    public void setDateCreated(String date) {this.dateCreated = date;}
    public String getDateEditedRaw() {return dateEdited;}
    public void setDateEdited(Date date) {this.dateEdited = date.toString();}
    public void setDateEdited(String date) {this.dateEdited = date;}
    public String getTags() {return tags;}
    public void setTags(String tags) {this.tags = tags;}
    public ArrayList<RampHold> getRampHolds() {return rampHolds;}
    public void setRampHolds(ArrayList<RampHold> rampHolds) {this.rampHolds = rampHolds;}
    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}


    // storable implementation
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CCN_NAME,getName());
        values.put(DbHelper.CCN_DATE_CREATED,getDateCreatedRaw());
        values.put(DbHelper.CCN_DATE_EDITED,getDateEditedRaw());
        values.put(DbHelper.CCN_TAGS,getTags());
        values.put(DbHelper.FiringCycleCN.RAMP_HOLD_LONG_STRING,RampHold.toLongString(getRampHolds()));
        values.put(DbHelper.FiringCycleCN.NOTES,getNotes());

        return values;
    }

    @Override
    public Type getStorableType() {return Type.FIRING_CYCLE;}

    public String getRowName() {return name;}
    public void updateDateEdited() {setDateEdited(Util.getDateTimeStamp());}

    public static final CursorCreator<FiringCycle> CURSOR_CREATOR = new CursorCreator<FiringCycle>() {
        @Override
        public ArrayList<FiringCycle> createFromCursor(Cursor cursor) {
            ArrayList<FiringCycle> ingredients = new ArrayList<>();
            while (cursor.moveToNext()) {
                ingredients.add (new FiringCycle(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        RampHold.parseFromLongString(cursor.getString(5)),
                        cursor.getString(6)
                ));
            }
            cursor.close();
            return ingredients;
        }
    };


    // parcelable implementation
    public FiringCycle(Parcel in) {
        name = in.readString();
        dateCreated = in.readString();
        dateEdited = in.readString();
        tags = in.readString();
        rampHolds = RampHold.parseFromLongString(in.readString());
        notes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateCreated);
        dest.writeString(dateEdited);
        dest.writeString(tags);
        dest.writeString(RampHold.toLongString(rampHolds));
        dest.writeString(notes);
    }

    @Override
    public int describeContents() {return 0;}

    public static final Creator<FiringCycle> CREATOR = new Creator<FiringCycle>() {
        @Override
        public FiringCycle createFromParcel(Parcel in) {return new FiringCycle(in);}

        @Override
        public FiringCycle[] newArray(int size) {return new FiringCycle[size];}
    };
}
