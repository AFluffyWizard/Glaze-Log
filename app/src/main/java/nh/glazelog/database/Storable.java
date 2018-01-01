package nh.glazelog.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Nick Hansen on 10/5/2017.
 */

public interface Storable {

    public ContentValues getContentValues();
    public Type getStorableType();
    public String getRowName();
    public String getDateCreatedRaw();
    public String getDateEditedRaw();
    public void updateDateEdited();
    public String getTags();

    public interface CursorCreator<T> {
        public ArrayList<T> createFromCursor (Cursor cursor);
    }

    public enum Type {
        SINGLE("Glazes", DbHelper.SingleCN.TABLE_NAME),
        COMBO("Glaze Combinations", DbHelper.ComboCN.TABLE_NAME),
        TEMPLATE("Glaze Templates", DbHelper.TemplateCN.TABLE_NAME),
        FIRING_CYCLE("Firing Cycle", DbHelper.FiringCycleCN.TABLE_NAME),
        INGREDIENT("Ingredients", DbHelper.IngredientCN.TABLE_NAME);

        private String friendlyName;
        private String tableName;

        private Type (String friendlyName, String tableName) {
            this.friendlyName = friendlyName;
            this.tableName = tableName;
        }

        public String toString() {return friendlyName;}

        public String getTableName() {
            return tableName;
        }
    }
}
