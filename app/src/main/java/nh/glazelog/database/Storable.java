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
    public String getCreationDateRaw();

    public interface CursorCreator<T> {
        public ArrayList<T> createFromCursor (Cursor cursor);
    }

    public enum Type {
        SINGLE(DBHelper.SingleCN.TABLE_NAME),
        COMBO(DBHelper.ComboCN.TABLE_NAME),
        TEMPLATE(DBHelper.TemplateCN.TABLE_NAME),
        FIRING_CYCLE(DBHelper.FiringCycleCN.TABLE_NAME);

        private String tableName;

        private Type (String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }
    }
}
