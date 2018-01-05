package nh.glazelog.glaze;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.Storable;

/**
 * Created by Nick Hansen on 9/29/2017.
 */

public class GlazeTemplate implements Parcelable,Storable,Listable {

    private String name;
    private String dateCreated;
    private String dateEdited;
    private String tags;
    private Finish finish;
    private Opacity opacity;
    private Atmosphere atmos;
    private String clayBody;
    private String application;
    private ArrayList<RampHold> firingCycle;
    private Cone bisquedTo;

    public GlazeTemplate(String name, Finish finish, Opacity opacity, Atmosphere atmos, String clayBody, String application, RampHold[] firingCycle, Cone bisquedTo) {
        this.name = name;
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.finish = finish;
        this.opacity = opacity;
        this.atmos = atmos;
        this.clayBody = clayBody;
        this.application = application;
        this.firingCycle = new ArrayList<RampHold>();
        Collections.addAll(this.firingCycle,firingCycle);
        this.bisquedTo = bisquedTo;
    }

    public GlazeTemplate(String name, String dateCreated, String dateEdited, String tags, Finish finish, Opacity opacity, Atmosphere atmos, String clayBody, String application, ArrayList<RampHold> firingCycle, Cone bisquedTo) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = tags;
        this.finish = finish;
        this.opacity = opacity;
        this.atmos = atmos;
        this.clayBody = clayBody;
        this.application = application;
        this.firingCycle = firingCycle;
        this.bisquedTo = bisquedTo;
    }

    public GlazeTemplate(Glaze g, String name) {
        this.name = name;
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = g.getTags();
        this.finish = g.getFinish();
        this.opacity = g.getOpacity();
        this.atmos = g.getAtmos();
        this.clayBody = g.getClayBody();
        this.application = g.getApplication();
        this.firingCycle = g.getFiringCycle();
        this.bisquedTo = g.getBisquedTo();
    }

    public GlazeTemplate() {
        this.name = "No Template";
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.finish = Finish.NONE;
        this.opacity = Opacity.NONE;
        this.atmos = Atmosphere.NONE;
        this.clayBody = "";
        this.application = "";
        this.firingCycle = new ArrayList<>();
        firingCycle.add(new RampHold(0,0,0));
        this.bisquedTo = Cone.C05;
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
    public Finish getFinish() {return finish;}
    public void setFinish(Finish finish) {this.finish = finish;}
    public Opacity getOpacity() {return opacity;}
    public void setOpacity(Opacity opacity) {this.opacity = opacity;}
    public Atmosphere getAtmos() {return atmos;}
    public void setAtmos(Atmosphere atmos) {this.atmos = atmos;}
    public String getClayBody() {return clayBody;}
    public void setClayBody(String clayBody) {this.clayBody = clayBody;}
    public String getApplication() {return application;}
    public void setApplication(String application) {this.application = application;}
    public ArrayList<RampHold> getFiringCycle() {return firingCycle;}
    public void setFiringCycle(ArrayList<RampHold> firingCycle) {this.firingCycle = firingCycle;}
    public Cone getBisquedTo () {return bisquedTo;}
    public void setBisquedTo (Cone bisquedTo) {this.bisquedTo = bisquedTo;}

    public String toString() {
        return name;
    }


    //listable implementation
    @Override
    public String getSecondaryInfo(ArrayList<?> itemInfo) {
        return null;
    }
    @Override
    public Uri getImageUri() {return Uri.EMPTY;}


    // storable implementation
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CCN_NAME,getName());
        values.put(DbHelper.CCN_DATE_CREATED, getDateCreatedRaw());
        values.put(DbHelper.CCN_DATE_EDITED, getDateEditedRaw());
        values.put(DbHelper.CCN_TAGS, getTags());
        values.put(DbHelper.TemplateCN.FINISH,getFinish().toString());
        values.put(DbHelper.TemplateCN.OPACITY,getOpacity().toString());
        values.put(DbHelper.TemplateCN.ATMOSPHERE,getAtmos().toString());
        values.put(DbHelper.TemplateCN.CLAY_BODY,getClayBody());
        values.put(DbHelper.TemplateCN.APPLICATION,getApplication());
        values.put(DbHelper.TemplateCN.FIRING_CYCLE,RampHold.toLongString(getFiringCycle()));
        values.put(DbHelper.TemplateCN.BISQUED_TO, getBisquedTo().toString());

        return values;
    }

    @Override
    public Storable.Type getStorableType() {
        return Type.TEMPLATE;
    }
    public String getRowName() {return name;}
    public void updateDateEdited() {setDateEdited(Util.getDateTimeStamp());}


    public static final CursorCreator<GlazeTemplate> CURSOR_CREATOR = new CursorCreator<GlazeTemplate>() {
        @Override
        public ArrayList<GlazeTemplate> createFromCursor(Cursor cursor) {
            ArrayList<GlazeTemplate> templates = new ArrayList<>();
            while (cursor.moveToNext()) {
                templates.add(new GlazeTemplate (
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Finish.getEnum(cursor.getString(5)),
                        Opacity.getEnum(cursor.getString(6)),
                        Atmosphere.getEnum(cursor.getString(7)),
                        cursor.getString(8),
                        cursor.getString(9),
                        RampHold.parseFromLongString(cursor.getString(10)),
                        Cone.getEnum(cursor.getString(11))));
            }
            cursor.close();
            return templates;
        }
    };


    // parcelable implementation
    public GlazeTemplate(Parcel in) {
        name = in.readString();
        dateCreated = in.readString();
        dateEdited = in.readString();
        tags = in.readString();
        finish = Finish.getEnum(in.readString());
        opacity = Opacity.getEnum(in.readString());
        atmos = Atmosphere.getEnum(in.readString());
        clayBody = in.readString();
        application = in.readString();
        firingCycle = in.createTypedArrayList(RampHold.CREATOR);
        bisquedTo = Cone.getEnum(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateCreated);
        dest.writeString(dateEdited);
        dest.writeString(tags);
        dest.writeString(finish.toString());
        dest.writeString(opacity.toString());
        dest.writeString(atmos.toString());
        dest.writeString(clayBody);
        dest.writeString(application);
        dest.writeTypedList(firingCycle);
        dest.writeString(bisquedTo.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GlazeTemplate> CREATOR = new Creator<GlazeTemplate>() {
        @Override
        public GlazeTemplate createFromParcel(Parcel in) {
            return new GlazeTemplate(in);
        }

        @Override
        public GlazeTemplate[] newArray(int size) {
            return new GlazeTemplate[size];
        }
    };
}
