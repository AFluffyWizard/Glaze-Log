package nh.glazelog.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;

import java.net.URI;
import java.util.ArrayList;

import nh.glazelog.glaze.Glaze;

/**
 * Created by Nick Hansen on 10/5/2017.
 */

public interface Storable {

    public ContentValues getContentValues();
    public Type getStorableType();
    public String getName();
    public String getDateCreatedRaw();
    public String getDateEditedRaw();
    public void updateDateEdited();
    public String getTags();
    public String getNotes();

    public interface CursorCreator<T> {
        public ArrayList<T> createFromCursor (Cursor cursor);
    }


    public enum Type {
        SINGLE(DbHelper.SingleCN.TABLE_NAME, true),
        COMBO(DbHelper.ComboCN.TABLE_NAME, true),
        FIRING_CYCLE(DbHelper.FiringCycleCN.TABLE_NAME, false),
        INGREDIENT(DbHelper.IngredientCN.TABLE_NAME, false);

        private String tableName;
        private boolean hasImage;

        private Type (String tableName, boolean hasImage) {
            this.tableName = tableName;
            this.hasImage = hasImage;
        }

        public String getTableName() {
            return tableName;
        }

        public boolean hasImage() {return hasImage;}
    }
}
