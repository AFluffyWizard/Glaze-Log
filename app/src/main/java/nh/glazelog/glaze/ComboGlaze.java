package nh.glazelog.glaze;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.Storable;

/**
 * Created by Nick Hansen on 10/4/2017.
 */

public class ComboGlaze implements Parcelable,Storable,Listable {

    private String name;
    private String dateCreated;
    private String dateEdited;
    private String tags;
    private String notes;
    private String versionNotes;
    private int lastVersionOpen;
    private String imageUriString;
    private String clayBody;
    private Cone bisquedTo;
    private ArrayList<ComboGlazeInfo> glazes;
    private ArrayList<RampHold> firingCycle;


    public ComboGlaze() {
        this.name = "";
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.notes = "";
        this.versionNotes = "";
        this.lastVersionOpen = 0;
        this.imageUriString = "";
        this.clayBody = "";
        this.bisquedTo = Cone.C05;
        this.glazes = new ArrayList<ComboGlazeInfo>();
        this.firingCycle = new ArrayList<RampHold>();
    }

    public ComboGlaze (String name) {
        this();
        this.setName(name);
    }


    public ComboGlaze(String name, String dateCreated, String dateEdited, String tags, String notes, String versionNotes, int lastVersionOpen, String imageUriString, String clayBody, Cone bisquedTo, ArrayList<ComboGlazeInfo> glazes, ArrayList<RampHold> firingCycle) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = tags;
        this.notes = notes;
        this.versionNotes = versionNotes;
        this.lastVersionOpen = lastVersionOpen;
        this.imageUriString = imageUriString;
        this.clayBody = clayBody;
        this.bisquedTo = bisquedTo;
        this.glazes = glazes;
        this.firingCycle = firingCycle;
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
    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}
    public String getVersionNotes() {return versionNotes;}
    public void setVersionNotes(String versionNotes) {this.versionNotes = versionNotes;}
    public int getLastVersionOpen() {return lastVersionOpen;}
    public void setLastVersionOpen(int lastVersionOpen) {this.lastVersionOpen = lastVersionOpen;}
    public Uri getImageUri() {if (imageUriString.equals("")) return Uri.EMPTY; else return Uri.parse(imageUriString);}
    private String getImageUriString () {return imageUriString;}
    public void setImageUri(Uri imageUri) {this.imageUriString = imageUri.toString();}
    public void setImageUri(String imageUriString) {this.imageUriString = imageUriString;}
    public String getClayBody() {return clayBody;}
    public void setClayBody(String clayBody) {this.clayBody = clayBody;}
    public Cone getBisquedTo() {return bisquedTo;}
    public void setBisquedTo(Cone bisquedTo) {this.bisquedTo = bisquedTo;}
    public ArrayList<ComboGlazeInfo> getGlazes() {return glazes;}
    public void setGlazes(ArrayList<ComboGlazeInfo> glazes) {this.glazes = glazes;}
    public ArrayList<RampHold> getFiringCycle() {return firingCycle;}
    public void setFiringCycle(ArrayList<RampHold> firingCycle) {this.firingCycle = firingCycle;}



    //listable implementation
    @Override
    public String getSecondaryInfo(ArrayList<?> itemInfo) {
        ComboGlaze currentCombo = (ComboGlaze) itemInfo.get(itemInfo.size()-1);
        Cone closestCone = Cone.NONE;
        if (currentCombo.getFiringCycle().size() != 0)
            closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(currentCombo.getFiringCycle()));

        return closestCone.toString() + ", " + currentCombo.getGlazes().size() + " glazes";
    }


    // storable implementation
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CCN_NAME,getName());
        values.put(DbHelper.CCN_DATE_CREATED, getDateCreatedRaw());
        values.put(DbHelper.CCN_DATE_EDITED, getDateEditedRaw());
        values.put(DbHelper.CCN_TAGS, getTags());
        values.put(DbHelper.CCN_NOTES, getNotes());
        values.put(DbHelper.ComboCN.VERSION_NOTES, getVersionNotes());
        values.put(DbHelper.ComboCN.LAST_VERSION_OPEN, getLastVersionOpen());
        values.put(DbHelper.ComboCN.IMAGE_URI_STRING, getImageUriString());
        values.put(DbHelper.ComboCN.CLAY_BODY, getClayBody());
        values.put(DbHelper.ComboCN.BISQUED_TO, getBisquedTo().toString());
        values.put(DbHelper.ComboCN.GLAZES, ComboGlazeInfo.toLongString(getGlazes()));
        values.put(DbHelper.ComboCN.FIRING_CYCLE,RampHold.toLongString(getFiringCycle()));
        return values;
    }

    @Override
    public Storable.Type getStorableType() {
        return Type.COMBO;
    }
    @Override
    public void updateDateEdited() {setDateEdited(Util.getDateTimeStamp());}

    public static final CursorCreator<ComboGlaze> CURSOR_CREATOR = new CursorCreator<ComboGlaze>() {
        @Override
        public ArrayList<ComboGlaze> createFromCursor(Cursor cursor) {
            ArrayList<ComboGlaze> combos = new ArrayList<>();
            while (cursor.moveToNext()) {
                combos.add (new ComboGlaze(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(7),
                        cursor.getString(8),
                        Cone.getEnum(cursor.getString(11)),
                        ComboGlazeInfo.parseFromLongString(cursor.getString(9)),
                        RampHold.parseFromLongString(cursor.getString(10))
                ));
            }
            cursor.close();
            return combos;
        }
    };


    // parcelable implementation
    public ComboGlaze(Parcel in) {
        name = in.readString();
        dateCreated = in.readString();
        dateEdited = in.readString();
        tags = in.readString();
        notes = in.readString();
        versionNotes = in.readString();
        lastVersionOpen = in.readInt();
        imageUriString = in.readString();
        clayBody = in.readString();
        bisquedTo = Cone.getEnum(in.readString());
        glazes = in.createTypedArrayList(ComboGlazeInfo.CREATOR);
        firingCycle = in.createTypedArrayList(RampHold.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateCreated);
        dest.writeString(dateEdited);
        dest.writeString(tags);
        dest.writeString(notes);
        dest.writeString(versionNotes);
        dest.writeInt(lastVersionOpen);
        dest.writeString(imageUriString);
        dest.writeString(clayBody);
        dest.writeString(bisquedTo.toString());
        dest.writeTypedList(glazes);
        dest.writeTypedList(firingCycle);
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
