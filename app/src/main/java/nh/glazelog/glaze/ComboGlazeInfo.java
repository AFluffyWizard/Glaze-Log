package nh.glazelog.glaze;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nick Hansen on 10/4/2017.
 */

public class ComboGlazeInfo implements Parcelable {

    private Glaze glaze;
    private String notes;

    public ComboGlazeInfo(Glaze glaze, String notes) {
        this.glaze = glaze;
        this.notes = notes;
    }

    public Glaze getGlaze() {return glaze;}
    public void setGlaze(Glaze glaze) {this.glaze = glaze;}
    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}


    // parcelable implementation
    public ComboGlazeInfo(Parcel in) {
        glaze = in.readParcelable(Glaze.class.getClassLoader());
        notes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(glaze, flags);
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
