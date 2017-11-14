package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import nh.glazelog.glaze.ComboGlaze;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.GlazeTemplate;

/**
 * Created by Nick Hansen on 9/27/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GlazeData.db";
    public static final int DATABASE_VERSION = 1;

    // for storing/parsing lists of data
    public static final String SHORT_SEP = ":";
    public static final String LONG_SEP = "+";

    // for loading the database
    private static DBHelper singletonInstance;
    public static synchronized DBHelper getSingletonInstance (Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (singletonInstance == null) {
            singletonInstance = new DBHelper(context.getApplicationContext());

            //singletonInstance.onCreate(singletonDatabase); // TODO - FOR DEBUG PURPOSES ONLY. REMOVE WHEN PUBLISHING
            if (!singletonInstance.contains(new GlazeTemplate(),false)) singletonInstance.write(new GlazeTemplate());
        }
        return singletonInstance;
    }

    private static SQLiteDatabase singletonDatabase;
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        singletonDatabase = this.getWritableDatabase();
    }

    public SQLiteDatabase getSingletonDatabase () {return singletonDatabase;}


    /*--------------------READING AND WRITING FROM DB--------------------*/
    public void write(Storable s) {
        long newRowId = singletonDatabase.insertOrThrow(s.getStorableType().getTableName(), null, s.getContentValues());
    }

    public void append(Storable s, ContentValues values) {
        singletonDatabase.updateWithOnConflict(s.getStorableType().getTableName(),values,
                CCN_CREATION_DATE + " = ?",new String[]{s.getCreationDateRaw()},SQLiteDatabase.CONFLICT_REPLACE);
    }
    public void append(Storable s) {
        append(s,s.getContentValues());
    }

    public void appendAllVersions(Storable s, ContentValues values) {
        singletonDatabase.updateWithOnConflict(s.getStorableType().getTableName(),values,
                CCN_NAME + " = ?",new String[]{s.getRowName()},SQLiteDatabase.CONFLICT_REPLACE);
    }
    public void appendAllVersions(Storable s) {
        appendAllVersions(s,s.getContentValues());
    }


    public void writeOrAppend(Storable s, boolean useDate) {
        if (contains(s,useDate))
            append(s);
        else
            write(s);
    }

    public boolean contains(Storable s, boolean useDate) {
        if (useDate)
            return readSingle(s.getStorableType(), CCN_CREATION_DATE, s.getCreationDateRaw()).size() > 0;
        else
            return readSingle(s.getStorableType(), CCN_NAME, s.getRowName()).size() > 0;
    }

    public void delete(Storable s, boolean deleteAll) {
        String criterion;
        String value;
        if (deleteAll) {criterion = CCN_NAME; value = s.getRowName();}
        else {criterion = CCN_CREATION_DATE; value = s.getCreationDateRaw();}

        singletonDatabase.execSQL("DELETE FROM " + s.getStorableType().getTableName() +
                " WHERE " + criterion + "=\"" + value + "\"");
    }

    public ArrayList<String> getDistinctNames (Storable.Type type) {
        Cursor cursor = singletonDatabase.rawQuery("SELECT DISTINCT " + CCN_NAME + " FROM " + type.getTableName(),null);
        ArrayList<String> names = new ArrayList<>();
        while(cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names;
    }

    public ArrayList<?> readSingle(Storable.Type type, String criterion, String value) {
        Cursor cursor = singletonDatabase.rawQuery("SELECT * FROM " + type.getTableName() +
                " WHERE " + criterion + " = \"" + value + "\"",null);
        switch(type) {
            default: break;
            case SINGLE: return Glaze.CURSOR_CREATOR.createFromCursor(cursor);
            case COMBO: return ComboGlaze.CURSOR_CREATOR.createFromCursor(cursor);
            case TEMPLATE: return GlazeTemplate.CURSOR_CREATOR.createFromCursor(cursor);
        }
        cursor.close();
        return null;
    }

    public ArrayList<?> readAll(Storable.Type type) {
        Cursor cursor = singletonDatabase.rawQuery("SELECT * FROM " + type.getTableName(),null);
        switch(type) {
            default: break;
            case SINGLE: return Glaze.CURSOR_CREATOR.createFromCursor(cursor);
            case COMBO: return ComboGlaze.CURSOR_CREATOR.createFromCursor(cursor);
            case TEMPLATE: return GlazeTemplate.CURSOR_CREATOR.createFromCursor(cursor);
        }
        cursor.close();
        return null;
    }



    /*--------------------DATABASE COLUMN NAMES--------------------*/
    // common column names - present in multiple tables
    public static final String CCN_NAME = "name";
    public static final String CCN_CREATION_DATE = "creation_date";
    public static final String CCN_EDITED_DATE = "date_edited";
    public static final String CCN_CLAY_BODY = "clay_body";
    public static final String CCN_FIRING_CYCLE = "firing_cycle";
    public static final String CCN_BISQUED_TO = "bisqued_to";
    public static final String CCN_PRIMARY_NOTES = "primary_notes";
    public static final String CCN_SECONDARY_NOTES = "secondary_notes";
    public static final String CCN_IMAGE_URI_STRING = "image_uri";

    public static class SingleCN implements BaseColumns {
        public static final String TABLE_NAME = "single_glaze_table";

        public static final String NAME = CCN_NAME;
        public static final String DATE_EDITED = CCN_EDITED_DATE;
        public static final String FINISH = "finish";
        public static final String OPACITY = "opaity";
        public static final String ATMOSPHERE = "atmos";
        public static final String CLAY_BODY = CCN_CLAY_BODY;
        public static final String APPLICATION = "application";
        public static final String PRIMARY_NOTES = CCN_PRIMARY_NOTES;
        public static final String IMAGE_URI_STRING = CCN_IMAGE_URI_STRING;
        public static final String SPGR = "sp_gr";
        public static final String MATERIALS = "recipe_materials";
        public static final String ADDITIONS = "recipe_additions";
        public static final String FIRING_CYCLE = CCN_FIRING_CYCLE;
        public static final String SECONDARY_NOTES = CCN_SECONDARY_NOTES;
    }

    public static class ComboCN implements BaseColumns {
        public static final String TABLE_NAME = "combo_glaze_table";

        public static final String DATE_EDITED = CCN_EDITED_DATE;
        public static final String CLAY_BODY = CCN_CLAY_BODY;
        public static final String GLAZES = "glazes";
        public static final String FIRING_CYCLE = CCN_FIRING_CYCLE;
        public static final String PRIMARY_NOTES = CCN_PRIMARY_NOTES;
        public static final String SECONDARY_NOTES = CCN_SECONDARY_NOTES;

    }

    public static class TemplateCN implements BaseColumns {
        public static final String TABLE_NAME = "glaze_template_table";

        public static final String FINISH = "finish";
        public static final String OPACITY = "opacity";
        public static final String ATMOSPHERE = "atmos";
        public static final String CLAY_BODY = "clay_body";
        public static final String APPLICATION = "application";
        public static final String FIRING_CYCLE = CCN_FIRING_CYCLE;
    }

    // TODO - IMPLEMENT TABLE FOR THIS
    public static class FiringCycleCN implements BaseColumns {
        public static final String TABLE_NAME = "firing_cycle_table";

        public static final String LONG_STRING = "long_string";
    }

    private static final String SQL_CREATE_ENTRIES_SINGLE =
            "CREATE TABLE IF NOT EXISTS " + SingleCN.TABLE_NAME + " (" +
                    SingleCN._ID + " INTEGER PRIMARY KEY," +
                    CCN_NAME + " TEXT," +
                    CCN_CREATION_DATE + " TEXT," +
                    SingleCN.DATE_EDITED + " TEXT," +
                    SingleCN.FINISH + " TEXT," +
                    SingleCN.OPACITY + " TEXT," +
                    SingleCN.ATMOSPHERE + " TEXT," +
                    SingleCN.CLAY_BODY + " TEXT," +
                    SingleCN.APPLICATION + " TEXT," +
                    SingleCN.PRIMARY_NOTES + " TEXT," +
                    SingleCN.IMAGE_URI_STRING + " TEXT," +
                    SingleCN.SPGR + " TEXT," +
                    SingleCN.MATERIALS + " TEXT," +
                    SingleCN.ADDITIONS + " TEXT," +
                    SingleCN.FIRING_CYCLE + " TEXT," +
                    CCN_BISQUED_TO + " TEXT," +
                    SingleCN.SECONDARY_NOTES + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_COMBO =
            "CREATE TABLE " + ComboCN.TABLE_NAME + " (" +
                    ComboCN._ID + " INTEGER PRIMARY KEY," +
                    CCN_NAME + " TEXT," +
                    CCN_CREATION_DATE + " TEXT," +
                    ComboCN.GLAZES + " TEXT," +
                    ComboCN.FIRING_CYCLE + " TEXT," +
                    CCN_BISQUED_TO + " TEXT," +
                    ComboCN.PRIMARY_NOTES + " TEXT," +
                    ComboCN.SECONDARY_NOTES + " TEXT" +
                    ");";

    private static final String SQL_CREATE_ENTRIES_TEMPLATE =
            "CREATE TABLE IF NOT EXISTS " + TemplateCN.TABLE_NAME + " (" +
                    TemplateCN._ID + " INTEGER PRIMARY KEY," +
                    CCN_NAME + " TEXT," +
                    CCN_CREATION_DATE + " TEXT," +
                    TemplateCN.FINISH + " TEXT," +
                    TemplateCN.OPACITY + " TEXT," +
                    TemplateCN.ATMOSPHERE + " TEXT," +
                    TemplateCN.CLAY_BODY + " TEXT," +
                    TemplateCN.APPLICATION + " TEXT," +
                    TemplateCN.FIRING_CYCLE + " TEXT," +
                    CCN_BISQUED_TO + " TEXT);";


    private static final String SQL_CREATE_ENTRIES_FIRINGCYCLE =
            "CREATE TABLE " + FiringCycleCN.TABLE_NAME + " (" +
                    FiringCycleCN._ID + " INTEGER PRIMARY KEY," +
                    CCN_NAME + " TEXT," +
                    CCN_CREATION_DATE + " TEXT," +
                    FiringCycleCN.LONG_STRING + " TEXT);";



    /*--------------------INHERITED DB FUNCTIONS--------------------*/
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS \"" + SingleCN.TABLE_NAME + "\""); // TODO - REMOVE WHEN FINISHED TESTING
        db.execSQL(SQL_CREATE_ENTRIES_SINGLE);
        //db.execSQL(SQL_CREATE_ENTRIES_COMBO);
        //db.execSQL("DROP TABLE IF EXISTS \"" + TemplateCN.TABLE_NAME + "\""); // TODO - REMOVE WHEN FINISHED TESTING
        db.execSQL(SQL_CREATE_ENTRIES_TEMPLATE);

        //db.execSQL("DROP TABLE IF EXISTS \"" + FiringCycleCN.TABLE_NAME + "\""); // TODO - REMOVE WHEN FINISHED TESTING
        //db.execSQL(SQL_CREATE_ENTRIES_FIRINGCYCLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no reason to implement this as of now
    }

    @Override
    public void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
