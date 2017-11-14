package nh.glazelog.glaze;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nh.glazelog.database.DBHelper;
import nh.glazelog.database.Storable;

/**
 * Created by Nick Hansen on 9/22/2017.
 */

public class Glaze implements Parcelable,Storable {

    private String name;
    private String creationDate;
    private String editedDate;
    private Finish finish;
    private Opacity opacity;
    private Atmosphere atmos;
    private String clayBody;
    private String application;
    private String primaryNotes;
    private String imageUriString;
    private double spgr;
    private ArrayList<IngredientQuantity> materials;
    private ArrayList<IngredientQuantity> additions;
    private ArrayList<RampHold> firingCycle;
    private Cone bisquedTo;
    private String secondaryNotes;

    public Glaze () {
        this.name = "";
        this.creationDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.editedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.finish = Finish.NONE;
        this.opacity = Opacity.NONE;
        this.atmos = Atmosphere.NONE;
        this.clayBody = "";
        this.application = "";
        this.primaryNotes = "";
        this.imageUriString = "";
        this.spgr = 0;
        this.materials = new ArrayList<IngredientQuantity>();
        this.additions = new ArrayList<IngredientQuantity>();
        this.firingCycle = new ArrayList<RampHold>();
        bisquedTo = Cone.C04;
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
        this.setCreationDate("201710" + debugdate + "_120000");
    }

    public Glaze (GlazeTemplate template) {
        if (template.getName().equals("No Template")) this.name = "";
        else this.name = template.getName();
        this.creationDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.editedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
        bisquedTo = Cone.NONE;
        this.secondaryNotes = "";
    }

    public Glaze(String name, String creationDate, String editedDate, Finish finish, Opacity opacity, Atmosphere atmos, String clayBody, String application, String primaryNotes, String imageUri, double spgr, ArrayList<IngredientQuantity> materials, ArrayList<IngredientQuantity> additions, ArrayList<RampHold> firingCycle, Cone bisquedTo, String secondaryNotes) {
        this.name = name;
        this.creationDate = creationDate;
        this.editedDate = editedDate;
        this.finish = finish;
        this.opacity = opacity;
        this.atmos = atmos;
        this.clayBody = clayBody;
        this.application = application;
        this.primaryNotes = primaryNotes;
        this.imageUriString = imageUri;
        this.spgr = spgr;
        this.materials = materials;
        this.additions = additions;
        this.firingCycle = firingCycle;
        this.bisquedTo = bisquedTo;
        this.secondaryNotes = secondaryNotes;
    }


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Date getCreationDate() {
        try {
            return new SimpleDateFormat("yyyyMMdd_HHmmss").parse(creationDate);
        } catch (ParseException e) {e.printStackTrace();};
        return null;
    }
    public void setCreationDate(Date date) {this.creationDate = date.toString();}
    public void setCreationDate(String date) {this.creationDate = date;}
    public Date getEditedDate() {
        try {
            return new SimpleDateFormat("yyyyMMdd_HHmmss").parse(editedDate);
        } catch (ParseException e) {e.printStackTrace();};
        return null;
    }
    public String getEditedDateRaw() {return editedDate;}
    public void setEditedDate(Date date) {this.editedDate = date.toString();}
    public void setEditedDate(String date) {this.editedDate = date;}
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
    public String getPrimaryNotes() {return primaryNotes;}
    public void setPrimaryNotes(String primaryNotes) {this.primaryNotes = primaryNotes;}
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
    public String getSecondaryNotes() {return secondaryNotes;}
    public void setSecondaryNotes(String secondaryNotes) {this.secondaryNotes = secondaryNotes;}



    // storable implementation
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CCN_NAME,getName());
        values.put(DBHelper.CCN_CREATION_DATE,getCreationDateRaw());
        values.put(DBHelper.SingleCN.DATE_EDITED,getEditedDateRaw());
        values.put(DBHelper.SingleCN.FINISH,getFinish().toString());
        values.put(DBHelper.SingleCN.OPACITY,getOpacity().toString());
        values.put(DBHelper.SingleCN.ATMOSPHERE,getAtmos().toString());
        values.put(DBHelper.SingleCN.CLAY_BODY,getClayBody());
        values.put(DBHelper.SingleCN.APPLICATION,getApplication());
        values.put(DBHelper.SingleCN.PRIMARY_NOTES,getPrimaryNotes());
        values.put(DBHelper.SingleCN.IMAGE_URI_STRING,getImageUriString());
        values.put(DBHelper.SingleCN.SPGR,getSpgr());
        values.put(DBHelper.SingleCN.MATERIALS,IngredientQuantity.toLongString(getMaterials()));
        values.put(DBHelper.SingleCN.ADDITIONS,IngredientQuantity.toLongString(getAdditions()));
        values.put(DBHelper.SingleCN.FIRING_CYCLE,RampHold.toLongString(getFiringCycle()));
        values.put(DBHelper.SingleCN.FIRING_CYCLE, RampHold.toLongString(getFiringCycle()));
        values.put(DBHelper.CCN_BISQUED_TO, bisquedTo.toString());
        values.put(DBHelper.SingleCN.SECONDARY_NOTES,getSecondaryNotes());

        return values;
    }

    @Override
    public Storable.Type getStorableType() {
        return Type.SINGLE;
    }
    public String getRowName() {return name;}
    public String getCreationDateRaw() {return creationDate;}

    public static final CursorCreator<Glaze> CURSOR_CREATOR = new CursorCreator<Glaze>() {
        @Override
        public ArrayList<Glaze> createFromCursor(Cursor cursor) {
            ArrayList<Glaze> glazes = new ArrayList<>();
            while (cursor.moveToNext()) {
                glazes.add (new Glaze(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Finish.getEnum(cursor.getString(4)),
                        Opacity.getEnum(cursor.getString(5)),
                        Atmosphere.getEnum(cursor.getString(6)),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        Double.parseDouble(cursor.getString(11)),
                        IngredientQuantity.parseFromLongString(cursor.getString(12)),
                        IngredientQuantity.parseFromLongString(cursor.getString(13)),
                        RampHold.parseFromLongString(cursor.getString(14)),
                        Cone.getEnum(cursor.getString(15)),
                        cursor.getString(16)
                ));
            }
            cursor.close();
            return glazes;
        }
    };
    
    
    // parcelable implementation
    public Glaze(Parcel in) {
        name = in.readString();
        creationDate = in.readString();
        editedDate = in.readString();
        finish = Finish.getEnum(in.readString());
        opacity = Opacity.getEnum(in.readString());
        atmos = Atmosphere.getEnum(in.readString());
        clayBody = in.readString();
        application = in.readString();
        primaryNotes = in.readString();
        imageUriString = in.readString();
        spgr = in.readDouble();
        materials = in.createTypedArrayList(IngredientQuantity.CREATOR);
        additions = in.createTypedArrayList(IngredientQuantity.CREATOR);
        firingCycle = in.createTypedArrayList(RampHold.CREATOR);
        bisquedTo = Cone.getEnum(in.readString());
        secondaryNotes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(creationDate);
        dest.writeString(editedDate);
        dest.writeString(finish.toString());
        dest.writeString(opacity.toString());
        dest.writeString(atmos.toString());
        dest.writeString(clayBody);
        dest.writeString(application);
        dest.writeString(primaryNotes);
        dest.writeString(imageUriString);
        dest.writeDouble(spgr);
        dest.writeTypedList(materials);
        dest.writeTypedList(additions);
        dest.writeTypedList(firingCycle);
        dest.writeString(bisquedTo.toString());
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
