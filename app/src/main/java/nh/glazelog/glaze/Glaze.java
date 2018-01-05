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
 * Created by Nick Hansen on 9/22/2017.
 */

public class Glaze implements Parcelable,Storable,Listable {

    private String name;
    private String dateCreated;
    private String dateEdited;
    private String tags;
    private Finish finish;
    private Opacity opacity;
    private Atmosphere atmos;
    private String clayBody;
    private String application;
    private String imageUriString;
    private double spgr;
    private ArrayList<IngredientQuantity> materials;
    private ArrayList<IngredientQuantity> additions;
    private ArrayList<RampHold> firingCycle;
    private Cone bisquedTo;
    private String primaryNotes;
    private String secondaryNotes;

    public Glaze () {
        this.name = "";
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.finish = Finish.NONE;
        this.opacity = Opacity.NONE;
        this.atmos = Atmosphere.NONE;
        this.clayBody = "";
        this.application = "";
        this.imageUriString = "";
        this.spgr = 0;
        this.materials = new ArrayList<IngredientQuantity>();
        this.additions = new ArrayList<IngredientQuantity>();
        this.firingCycle = new ArrayList<RampHold>();
        this.bisquedTo = Cone.C05;
        this.primaryNotes = "";
        this.secondaryNotes = "";
    }

    public Glaze (String name) {
        this();
        this.setName(name);
    }

    // FOR DEBUG PURPOSES ONLY. SHOULD NEVER ACTUALLY BE USED
    public Glaze (String name, int debugdate) {
        this();
        this.setName(name);
        this.setDateCreated("201710" + debugdate + "_120000");
    }

    public Glaze (GlazeTemplate template) {
        if (template.getName().equals("No Template")) this.name = "";
        else this.name = template.getName();
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.finish = template.getFinish();
        this.opacity = template.getOpacity();
        this.atmos = template.getAtmos();
        this.clayBody = template.getClayBody();
        this.application = template.getApplication();
        this.primaryNotes = "";
        this.imageUriString = "";
        this.spgr = 0;
        this.materials = new ArrayList<IngredientQuantity>();
        this.additions = new ArrayList<IngredientQuantity>();
        this.firingCycle = template.getFiringCycle();
        this.bisquedTo = Cone.NONE;
        this.secondaryNotes = "";
    }

    public Glaze (Glaze g) {
        this.name = "Copy of " + g.getName();
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = g.getTags();
        this.finish = g.getFinish();
        this.opacity = g.getOpacity();
        this.atmos = g.getAtmos();
        this.clayBody = g.getClayBody();
        this.application = g.getApplication();
        this.imageUriString = "";
        this.spgr = g.getSpgr();
        this.materials = g.getMaterials();
        this.additions = g.getAdditions();
        this.firingCycle = g.getFiringCycle();
        this.bisquedTo = g.getBisquedTo();
        this.primaryNotes = g.getPrimaryNotes();
        this.secondaryNotes = g.getSecondaryNotes();
    }

    public Glaze(String name, String dateCreated, String dateEdited, String tags, Finish finish, Opacity opacity, Atmosphere atmos, String clayBody, String application, String imageUri, double spgr, ArrayList<IngredientQuantity> materials, ArrayList<IngredientQuantity> additions, ArrayList<RampHold> firingCycle, Cone bisquedTo, String primaryNotes, String secondaryNotes) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = tags;
        this.finish = finish;
        this.opacity = opacity;
        this.atmos = atmos;
        this.clayBody = clayBody;
        this.application = application;
        this.imageUriString = imageUri;
        this.spgr = spgr;
        this.materials = materials;
        this.additions = additions;
        this.firingCycle = firingCycle;
        this.bisquedTo = bisquedTo;
        this.primaryNotes = primaryNotes;
        this.secondaryNotes = secondaryNotes;
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
    public Uri getImageUri() {if (imageUriString.equals("")) return Uri.EMPTY; else return Uri.parse(imageUriString);}
    private String getImageUriString () {return imageUriString;}
    public void setImageUri(Uri imageUri) {this.imageUriString = imageUri.toString();}
    public void setImageUri(String imageUriString) {this.imageUriString = imageUriString;}
    public double getSpgr() {return spgr;}
    public void setSpgr(double spgr) {this.spgr = spgr;}
    public ArrayList<IngredientQuantity> getMaterials() {return materials;}
    public void setMaterials(ArrayList<IngredientQuantity> materials) {this.materials = materials;}
    public ArrayList<IngredientQuantity> getAdditions() {return additions;}
    public void setAdditions(ArrayList<IngredientQuantity> additions) {this.additions = additions;}
    public ArrayList<RampHold> getFiringCycle() {return firingCycle;}
    public void setFiringCycle(ArrayList<RampHold> firingCycle) {this.firingCycle = firingCycle;}
    public Cone getBisquedTo() {return bisquedTo;}
    public void setBisquedTo(Cone bisquedTo) {this.bisquedTo = bisquedTo;}
    public String getPrimaryNotes() {return primaryNotes;}
    public void setPrimaryNotes(String primaryNotes) {this.primaryNotes = primaryNotes;}
    public String getSecondaryNotes() {return secondaryNotes;}
    public void setSecondaryNotes(String secondaryNotes) {this.secondaryNotes = secondaryNotes;}



    //listable implementation
    @Override
    public String getSecondaryInfo(ArrayList<?> itemInfo) {
        Glaze currentGlaze = (Glaze) itemInfo.get(itemInfo.size()-1);
        Cone closestCone = Cone.NONE;
        if (currentGlaze.getFiringCycle().size() != 0)
            closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(currentGlaze.getFiringCycle()));

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
        values.put(DbHelper.SingleCN.FINISH,getFinish().toString());
        values.put(DbHelper.SingleCN.OPACITY,getOpacity().toString());
        values.put(DbHelper.SingleCN.ATMOSPHERE,getAtmos().toString());
        values.put(DbHelper.SingleCN.CLAY_BODY,getClayBody());
        values.put(DbHelper.SingleCN.APPLICATION,getApplication());
        values.put(DbHelper.SingleCN.IMAGE_URI_STRING,getImageUriString());
        values.put(DbHelper.SingleCN.SPGR,getSpgr());
        values.put(DbHelper.SingleCN.MATERIALS,IngredientQuantity.toLongString(getMaterials()));
        values.put(DbHelper.SingleCN.ADDITIONS,IngredientQuantity.toLongString(getAdditions()));
        values.put(DbHelper.SingleCN.FIRING_CYCLE,RampHold.toLongString(getFiringCycle()));
        values.put(DbHelper.SingleCN.BISQUED_TO,getBisquedTo().toString());
        values.put(DbHelper.SingleCN.PRIMARY_NOTES,getPrimaryNotes());
        values.put(DbHelper.SingleCN.SECONDARY_NOTES,getSecondaryNotes());

        return values;
    }

    @Override
    public Storable.Type getStorableType() {return Type.SINGLE;}
    @Override
    public String getRowName() {return name;}
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
                        Finish.getEnum(cursor.getString(5)),
                        Opacity.getEnum(cursor.getString(6)),
                        Atmosphere.getEnum(cursor.getString(7)),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        Double.parseDouble(cursor.getString(11)),
                        IngredientQuantity.parseFromLongString(cursor.getString(12)),
                        IngredientQuantity.parseFromLongString(cursor.getString(13)),
                        RampHold.parseFromLongString(cursor.getString(14)),
                        Cone.getEnum(cursor.getString(15)),
                        cursor.getString(16),
                        cursor.getString(17)
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
        finish = Finish.getEnum(in.readString());
        opacity = Opacity.getEnum(in.readString());
        atmos = Atmosphere.getEnum(in.readString());
        clayBody = in.readString();
        application = in.readString();
        imageUriString = in.readString();
        spgr = in.readDouble();
        materials = in.createTypedArrayList(IngredientQuantity.CREATOR);
        additions = in.createTypedArrayList(IngredientQuantity.CREATOR);
        firingCycle = in.createTypedArrayList(RampHold.CREATOR);
        bisquedTo = Cone.getEnum(in.readString());
        primaryNotes = in.readString();
        secondaryNotes = in.readString();
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
        dest.writeString(imageUriString);
        dest.writeDouble(spgr);
        dest.writeTypedList(materials);
        dest.writeTypedList(additions);
        dest.writeTypedList(firingCycle);
        dest.writeString(bisquedTo.toString());
        dest.writeString(primaryNotes);
        dest.writeString(secondaryNotes);
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
