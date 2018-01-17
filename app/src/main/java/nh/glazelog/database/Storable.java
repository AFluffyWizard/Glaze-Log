package nh.glazelog.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.StringRes;

import java.util.ArrayList;

import nh.glazelog.R;
import nh.glazelog.activity.EditFiringCycle;
import nh.glazelog.activity.SingleGlaze;

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


    public enum Type { // TODO - UPDATE ACTIVTY CLASSES AFTER CREATING THEM
        SINGLE(DbHelper.SingleCN.TABLE_NAME, true, SingleGlaze.class, R.string.list_title_single, R.string.list_dialog_title_single),
        COMBO(DbHelper.ComboCN.TABLE_NAME, true, SingleGlaze.class, R.string.list_title_combo, R.string.list_dialog_title_combo),
        FIRING_CYCLE(DbHelper.FiringCycleCN.TABLE_NAME, false, EditFiringCycle.class, R.string.list_title_firingcycle, R.string.list_dialog_title_firingcycle),
        INGREDIENT(DbHelper.IngredientCN.TABLE_NAME, false, SingleGlaze.class, R.string.list_title_ingredient, R.string.list_dialog_title_ingredient);

        // used for database
        private String tableName;

        // used for list
        private boolean hasImage;
        private Class activityClass;
        private int listTitle;
        private int dialogTitle;

        private Type (String tableName, boolean hasImage, Class activityClass, @StringRes int listTitle, @StringRes int dialogTitle) {
            this.tableName = tableName;
            this.hasImage = hasImage;
            this.activityClass = activityClass;
            this.listTitle = listTitle;
            this.dialogTitle = dialogTitle;
        }

        public String getTableName() {
            return tableName;
        }

        public boolean hasImage() {return hasImage;}

        public Class getActivityClass() {return activityClass;}

        public int getListTitle() {return listTitle;}

        public int getDialogTitle() {return dialogTitle;}
    }
}
