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
 * Created by Nick Hansen on 12/31/2017.
 */

public class Ingredient implements Parcelable,Storable,Listable{

    private String name;
    private String dateCreated;
    private String dateEdited;
    private String tags;
    private String aliases;
    private ArrayList<OxideQuantity> oxideQuantities;
    private double costPerKg;
    private String notes;


    public Ingredient() {
        this.name = "";
        this.dateCreated = Util.getDateTimeStamp();
        this.dateEdited = Util.getDateTimeStamp();
        this.tags = "";
        this.aliases = "";
        this.oxideQuantities = new ArrayList<>();
        this.costPerKg = 0;
        this.notes = "";
    }

    public Ingredient(String name) {
        this();
        this.setName(name);
    }

    public Ingredient (String name, String dateCreated, String dateEdited, String tags, String aliases, ArrayList<OxideQuantity> oxideQuantities, double costPerKg, String notes) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = tags;
        this.aliases = aliases;
        this.oxideQuantities = oxideQuantities;
        this.costPerKg = costPerKg;
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
    public String getAliasesLongString() {return aliases;}
    public ArrayList<String> getAliases() {
        ArrayList<String> stringList = new ArrayList<>();
        Collections.addAll(stringList,Util.stringToArray(aliases,DbHelper.LONG_SEP));
        return stringList;
    }
    public void setAliases(String aliases) {this.aliases = aliases;}
    public void setAliases(ArrayList<String> aliases) {
        String aliasString = "";
        for (String s : aliases)
            aliasString += s + DbHelper.LONG_SEP;
        this.aliases = aliasString;
    }
    public ArrayList<OxideQuantity> getOxideQuantitys() {return oxideQuantities;}
    public void setOxideQuantitys(ArrayList<OxideQuantity> oxideQuantities) {this.oxideQuantities = oxideQuantities;}
    public double getCostPerKg() {return costPerKg;}
    public void setCostPerKg(Double costPerKg) {this.costPerKg = costPerKg;}
    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}


    //listable implementation
    @Override
    public String getSecondaryInfo(ArrayList<?> itemInfo) {
        String costString = "Unknown cost";
        if (getCostPerKg() != 0) costString = "$" + getCostPerKg() + " per KG";

        return costString;
    }
    @Override
    public Uri getImageUri() {return Uri.EMPTY;}


    // storable implementation
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CCN_NAME,getName());
        values.put(DbHelper.CCN_DATE_CREATED,getDateCreatedRaw());
        values.put(DbHelper.CCN_DATE_EDITED,getDateEditedRaw());
        values.put(DbHelper.CCN_TAGS,getTags());
        values.put(DbHelper.IngredientCN.ALIASES, getAliasesLongString());
        values.put(DbHelper.IngredientCN.OXIDE_QUANTITY_LONG_STRING,OxideQuantity.toLongString(getOxideQuantitys()));
        values.put(DbHelper.IngredientCN.COST_PER_KG,getCostPerKg());
        values.put(DbHelper.IngredientCN.NOTES,getNotes());
        
        return values;
    }

    @Override
    public Type getStorableType() {return Type.INGREDIENT;}
    @Override
    public void updateDateEdited() {setDateEdited(Util.getDateTimeStamp());}

    public static final CursorCreator<Ingredient> CURSOR_CREATOR = new CursorCreator<Ingredient>() {
        @Override
        public ArrayList<Ingredient> createFromCursor(Cursor cursor) {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while (cursor.moveToNext()) {
                ingredients.add (new Ingredient(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        OxideQuantity.parseFromLongString(cursor.getString(6)),
                        cursor.getDouble(7),
                        cursor.getString(8)
                ));
            }
            cursor.close();
            return ingredients;
        }
    };


    // parcelable implementation
    public Ingredient(Parcel in) {
        name = in.readString();
        dateCreated = in.readString();
        dateEdited = in.readString();
        tags = in.readString();
        aliases = in.readString();
        oxideQuantities = OxideQuantity.parseFromLongString(in.readString());
        costPerKg = in.readDouble();
        notes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateCreated);
        dest.writeString(dateEdited);
        dest.writeString(tags);
        dest.writeString(aliases);
        dest.writeString(OxideQuantity.toLongString(oxideQuantities));
        dest.writeDouble(costPerKg);
        dest.writeString(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {return new Ingredient(in);}

        @Override
        public Ingredient[] newArray(int size) {return new Ingredient[size];}
    };
}
