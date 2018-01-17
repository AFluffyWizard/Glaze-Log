package nh.glazelog.glaze;

import android.app.Activity;
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
 * Created by Nick Hansen on 9/22/2017.
 */

public class Glaze implements Parcelable,Storable,Listable {

    private String name;
    private String dateCreated;
    private String dateEdited;
    private String tags;
    private String notes;
    private String versionNotes;
    private int lastVersionOpen;
    private String imageUriString;
    private Finish finish;
    private Opacity opacity;
    private Atmosphere atmos;
    private String clayBody;
    private Cone bisquedTo;
    private String application;
    private double spgr;
    private ArrayList<IngredientQuantity> materials;
    private ArrayList<IngredientQuantity> additions;
    private String firingCycle;

    public Glaze () {
        this.name = "";
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.notes = "";
        this.versionNotes = "";
        this.lastVersionOpen = 0;
        this.imageUriString = "";
        this.finish = Finish.NONE;
        this.opacity = Opacity.NONE;
        this.atmos = Atmosphere.NONE;
        this.clayBody = "";
        this.bisquedTo = Cone.C05;
        this.application = "";
        this.spgr = 0;
        this.materials = new ArrayList<IngredientQuantity>();
        this.additions = new ArrayList<IngredientQuantity>();
        this.firingCycle = "";
    }

    public Glaze (String name) {
        this();
        this.setName(name);
    }

    public Glaze (Glaze g) {
        this.name = "Copy of " + g.getName();
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = g.getTags();
        this.notes = g.getNotes();
        this.versionNotes = g.getVersionNotes();
        this.lastVersionOpen = g.getLastVersionOpen();
        this.imageUriString = "";
        this.finish = g.getFinish();
        this.opacity = g.getOpacity();
        this.atmos = g.getAtmos();
        this.clayBody = g.getClayBody();
        this.bisquedTo = g.getBisquedTo();
        this.application = g.getApplication();
        this.spgr = g.getSpgr();
        this.materials = g.getMaterials();
        this.additions = g.getAdditions();
        this.firingCycle = g.getFiringCycleID();
    }

    public Glaze(String name, String dateCreated, String dateEdited, String tags, String notes, String versionNotes, int lastVersionOpen, String imageUriString, Finish finish, Opacity opacity, Atmosphere atmos, String clayBody, Cone bisquedTo, String application, double spgr, ArrayList<IngredientQuantity> materials, ArrayList<IngredientQuantity> additions, String firingCycle) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = tags;
        this.notes = notes;
        this.versionNotes = versionNotes;
        this.lastVersionOpen = lastVersionOpen;
        this.imageUriString = imageUriString;
        this.finish = finish;
        this.opacity = opacity;
        this.atmos = atmos;
        this.clayBody = clayBody;
        this.bisquedTo = bisquedTo;
        this.application = application;
        this.spgr = spgr;
        this.materials = materials;
        this.additions = additions;
        this.firingCycle = firingCycle;
    }

    /* Old implementation where the getDate() functions returned a Date object.
       Kept in case I need it in the future.

    public Date getDateCreated() {
        try {
            return new SimpleDateFormat("yyyyMMdd_HHmmss").parse(dateCreated);
        } catch (ParseException e) {e.printStackTrace();};
        return null;
    }
    */

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
    public Finish getFinish() {return finish;}
    public void setFinish(Finish finish) {this.finish = finish;}
    public Opacity getOpacity() {return opacity;}
    public void setOpacity(Opacity opacity) {this.opacity = opacity;}
    public Atmosphere getAtmos() {return atmos;}
    public void setAtmos(Atmosphere atmos) {this.atmos = atmos;}
    public String getClayBody() {return clayBody;}
    public void setClayBody(String clayBody) {this.clayBody = clayBody;}
    public Cone getBisquedTo() {return bisquedTo;}
    public void setBisquedTo(Cone bisquedTo) {this.bisquedTo = bisquedTo;}
    public String getApplication() {return application;}
    public void setApplication(String application) {this.application = application;}
    public double getSpgr() {return spgr;}
    public void setSpgr(double spgr) {this.spgr = spgr;}
    public ArrayList<IngredientQuantity> getMaterials() {return materials;}
    public void setMaterials(ArrayList<IngredientQuantity> materials) {this.materials = materials;}
    public ArrayList<IngredientQuantity> getAdditions() {return additions;}
    public void setAdditions(ArrayList<IngredientQuantity> additions) {this.additions = additions;}
    public String getFiringCycleID() {return firingCycle;}
    public FiringCycle getFiringCycle() {
        ArrayList<?> rh = DbHelper.getSingletonInstance(
                new Activity()).readSingle(Type.FIRING_CYCLE,DbHelper.CCN_DATE_CREATED, getFiringCycleID());
                /* I am very much aware it is totally retarded
                 * to pass a new activity as the Context parameter for this method.
                 * However, there is no other way to obtain the application's context,
                 * and at this point the singleton instance of DbHelper should be initialized,
                 * and the Context is no longer needed.
                 * The alternative would be to create a separate method with no parameters,
                 * that would then have the ability to return a null instance, and hence
                 * be EXTREMELY UNSAFE.
                 * So, I'm doing it this way. As are the famous last words of any programmer,
                 * it should work.
                 *
                 * Nick Hansen - 1/9/18
                 */
        if (rh.size() == 0) return new FiringCycle();
        else return (FiringCycle)rh.get(0);
    }
    public void setFiringCycle(String firingCycle) {this.firingCycle = firingCycle;}


    //listable implementation
    @Override
    public String getSecondaryInfo(ArrayList<?> itemInfo) {
        Glaze currentGlaze = (Glaze) itemInfo.get(itemInfo.size()-1);
        Cone closestCone = Cone.NONE;
        if (currentGlaze.getFiringCycle().getRampHolds().size() != 0)
            closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(currentGlaze.getFiringCycle().getRampHolds()));

        return closestCone.toString();
    }


    // storable implementation
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CCN_NAME,getName());
        values.put(DbHelper.CCN_DATE_CREATED,getDateCreatedRaw());
        values.put(DbHelper.CCN_DATE_EDITED,getDateEditedRaw());
        values.put(DbHelper.CCN_TAGS,getTags());
        values.put(DbHelper.CCN_NOTES,getNotes());
        values.put(DbHelper.SingleCN.VERSION_NOTES, getVersionNotes());
        values.put(DbHelper.SingleCN.LAST_VERSION_OPEN,getLastVersionOpen());
        values.put(DbHelper.SingleCN.IMAGE_URI_STRING,getImageUriString());
        values.put(DbHelper.SingleCN.FINISH,getFinish().toString());
        values.put(DbHelper.SingleCN.OPACITY,getOpacity().toString());
        values.put(DbHelper.SingleCN.ATMOSPHERE,getAtmos().toString());
        values.put(DbHelper.SingleCN.CLAY_BODY,getClayBody());
        values.put(DbHelper.SingleCN.BISQUED_TO,getBisquedTo().toString());
        values.put(DbHelper.SingleCN.APPLICATION,getApplication());
        values.put(DbHelper.SingleCN.SPGR,getSpgr());
        values.put(DbHelper.SingleCN.MATERIALS,IngredientQuantity.toLongString(getMaterials()));
        values.put(DbHelper.SingleCN.ADDITIONS,IngredientQuantity.toLongString(getAdditions()));
        values.put(DbHelper.SingleCN.FIRING_CYCLE_ID, getFiringCycleID());
        return values;
    }

    @Override
    public Storable.Type getStorableType() {return Type.SINGLE;}
    @Override
    public void updateDateEdited() {setDateEdited(Util.getDateTimeStamp());}

    public static final CursorCreator<Glaze> CURSOR_CREATOR = new CursorCreator<Glaze>() {
        @Override
        public ArrayList<Glaze> createFromCursor(Cursor cursor) {
            ArrayList<Glaze> glazes = new ArrayList<>();
            while (cursor.moveToNext()) {
                glazes.add (new Glaze(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        Finish.getEnum(cursor.getString(9)),
                        Opacity.getEnum(cursor.getString(10)),
                        Atmosphere.getEnum(cursor.getString(11)),
                        cursor.getString(12),
                        Cone.getEnum(cursor.getString(13)),
                        cursor.getString(14),
                        Double.parseDouble(cursor.getString(15)),
                        IngredientQuantity.parseFromLongString(cursor.getString(16)),
                        IngredientQuantity.parseFromLongString(cursor.getString(17)),
                        cursor.getString(18)
                ));
            }
            cursor.close();
            return glazes;
        }
    };
    
    
    // parcelable implementation
    public Glaze(Parcel in) {
        name = in.readString();
        dateCreated = in.readString();
        dateEdited = in.readString();
        tags = in.readString();
        notes = in.readString();
        versionNotes = in.readString();
        lastVersionOpen = in.readInt();
        imageUriString = in.readString();
        finish = Finish.getEnum(in.readString());
        opacity = Opacity.getEnum(in.readString());
        atmos = Atmosphere.getEnum(in.readString());
        clayBody = in.readString();
        bisquedTo = Cone.getEnum(in.readString());
        application = in.readString();
        spgr = in.readDouble();
        materials = in.createTypedArrayList(IngredientQuantity.CREATOR);
        additions = in.createTypedArrayList(IngredientQuantity.CREATOR);
        firingCycle = in.readString();
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
        dest.writeString(finish.toString());
        dest.writeString(opacity.toString());
        dest.writeString(atmos.toString());
        dest.writeString(clayBody);
        dest.writeString(bisquedTo.toString());
        dest.writeString(application);
        dest.writeDouble(spgr);
        dest.writeTypedList(materials);
        dest.writeTypedList(additions);
        dest.writeString(firingCycle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Glaze> CREATOR = new Creator<Glaze>() {
        @Override
        public Glaze createFromParcel(Parcel in) {
            return new Glaze(in);
        }

        @Override
        public Glaze[] newArray(int size) {
            return new Glaze[size];
        }
    };


}
