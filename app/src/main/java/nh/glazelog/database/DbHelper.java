package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import nh.glazelog.glaze.ComboGlaze;
import nh.glazelog.glaze.FiringCycle;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.Ingredient;

/**
 * Created by Nick Hansen on 9/27/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GlazeData.db";
    public static final int DATABASE_VERSION = 7;

    // for storing/parsing lists of data
    public static final String SHORT_SEP = ":";
    public static final String LONG_SEP = "+";

    // for loading the database
    private static DbHelper singletonInstance;
    public static synchronized DbHelper getSingletonInstance (Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (singletonInstance == null) {
            singletonInstance = new DbHelper(context.getApplicationContext());
        }
        return singletonInstance;
    }

    private static SQLiteDatabase singletonDatabase;
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        singletonDatabase = this.getWritableDatabase();
    }

    public SQLiteDatabase getSingletonDatabase () {
        if (singletonDatabase == null)singletonDatabase = this.getWritableDatabase();
        return singletonDatabase;
    }


    /*--------------------READING AND WRITING FROM DB--------------------*/
    public boolean write(Storable s) {
        ArrayList<String> names = getDistinctNames(s.getStorableType());
        for (String name : names)
            if (name == s.getName()) return false;
        long newRowId = singletonDatabase.insertOrThrow(s.getStorableType().getTableName(), null, s.getContentValues());
        return true;
    }

    public void append(Storable s, ContentValues values) {
        s.updateDateEdited();
        values.put(CCN_DATE_EDITED,s.getDateEditedRaw());
        singletonDatabase.updateWithOnConflict(s.getStorableType().getTableName(),values,
                CCN_DATE_CREATED + " = ?",new String[]{s.getDateCreatedRaw()},SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void appendAllVersions(Storable s, ContentValues values) {
        s.updateDateEdited();
        values.put(CCN_DATE_EDITED,s.getDateEditedRaw());
        singletonDatabase.updateWithOnConflict(s.getStorableType().getTableName(),values,
                CCN_NAME + " = ?",new String[]{s.getName()},SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean contains(Storable s, boolean useDate) {
        if (useDate)
            return readSingle(s.getStorableType(), CCN_DATE_CREATED, s.getDateCreatedRaw()).size() > 0;
        else
            return readSingle(s.getStorableType(), CCN_NAME, s.getName()).size() > 0;
    }

    public void delete(Storable s, boolean deleteAll) {
        String criterion;
        String value;
        if (deleteAll) {criterion = CCN_NAME; value = s.getName();}
        else {criterion = CCN_DATE_CREATED; value = s.getDateCreatedRaw();}

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
            case FIRING_CYCLE: return FiringCycle.CURSOR_CREATOR.createFromCursor(cursor);
            case INGREDIENT: return Ingredient.CURSOR_CREATOR.createFromCursor(cursor);
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
            case FIRING_CYCLE: return FiringCycle.CURSOR_CREATOR.createFromCursor(cursor);
            case INGREDIENT: return Ingredient.CURSOR_CREATOR.createFromCursor(cursor);
        }
        cursor.close();
        return null;
    }



    /*--------------------DATABASE COLUMN NAMES--------------------*/
    // Common Column Names - these columns should be present in EVERY table
    // this allows me to write one set of read/write functions
    // and have them apply to any kind of storable data I want.
    public static final String CCN_NAME = "name";
    public static final String CCN_DATE_CREATED = "creation_date";
    public static final String CCN_DATE_EDITED = "date_edited";
    public static final String CCN_TAGS = "tags";
    public static final String CCN_NOTES = "notes";
    private static final String CREATE_ENTRIES_HEADER =
            CCN_NAME + " TEXT," + CCN_DATE_CREATED + " TEXT," + CCN_DATE_EDITED + " TEXT," + CCN_TAGS + " TEXT," + CCN_NOTES + " TEXT";

    public static class SingleCN implements BaseColumns {
        public static final String TABLE_NAME = "single_glaze_table";

        public static final String FINISH = "finish";
        public static final String OPACITY = "opacity";
        public static final String ATMOSPHERE = "atmos";
        public static final String CLAY_BODY = "clay_body";
        public static final String APPLICATION = "application";
        public static final String VERSION_NOTES = "version_notes";
        public static final String IMAGE_URI_STRING = "image_uri";
        public static final String SPGR = "sp_gr";
        public static final String MATERIALS = "recipe_materials";
        public static final String ADDITIONS = "recipe_additions";
        public static final String FIRING_CYCLE = "firing_cycle";
        public static final String BISQUED_TO = "bisqued_to";
    }

    public static class ComboCN implements BaseColumns {
        public static final String TABLE_NAME = "combo_glaze_table";

        public static final String VERSION_NOTES = "version_notes";
        public static final String IMAGE_URI_STRING = "image_uri";
        public static final String CLAY_BODY = "clay_body";
        public static final String GLAZES = "glazes";
        public static final String FIRING_CYCLE = "firing_cycle";
        public static final String BISQUED_TO = "bisqued_to";

    }

    public static class FiringCycleCN implements BaseColumns {
        public static final String TABLE_NAME = "firing_cycle_table";

        public static final String KILN_TYPE = "kiln_type";
        public static final String RAMP_HOLD_LONG_STRING = "ramp_hold_long_string";
    }

    public static class IngredientCN implements BaseColumns {
        public static final String TABLE_NAME = "ingredient_table";

        public static final String ALIASES = "aliases";
        public static final String OXIDE_QUANTITY_LONG_STRING = "oxide_quantity_long_string";
        public static final String COST_PER_KG = "cost_per_kg";

    }


    private static String generateCreateEntriesString(String tableName, String... rowNames) {
        String createEntriesString = "CREATE TABLE IF NOT EXISTS " + tableName +
                " (" + BaseColumns._ID + " INTEGER PRIMARY KEY," + CREATE_ENTRIES_HEADER;

        for (int i = 0; i < rowNames.length; i++)
            createEntriesString += "," + rowNames[i] + " TEXT";
        createEntriesString += ");";

        return createEntriesString;
    }

    private static final String SQL_CREATE_ENTRIES_SINGLE =
            generateCreateEntriesString(SingleCN.TABLE_NAME,
                    SingleCN.FINISH,
                    SingleCN.OPACITY,
                    SingleCN.ATMOSPHERE,
                    SingleCN.CLAY_BODY,
                    SingleCN.APPLICATION,
                    SingleCN.VERSION_NOTES,
                    SingleCN.IMAGE_URI_STRING,
                    SingleCN.SPGR,
                    SingleCN.MATERIALS,
                    SingleCN.ADDITIONS,
                    SingleCN.FIRING_CYCLE,
                    SingleCN.BISQUED_TO);

    private static final String SQL_CREATE_ENTRIES_COMBO =
            generateCreateEntriesString(ComboCN.TABLE_NAME,
                    ComboCN.VERSION_NOTES,
                    ComboCN.IMAGE_URI_STRING,
                    ComboCN.CLAY_BODY,
                    ComboCN.GLAZES,
                    ComboCN.FIRING_CYCLE,
                    ComboCN.BISQUED_TO);

    private static final String SQL_CREATE_ENTRIES_FIRINGCYCLE =
            generateCreateEntriesString(FiringCycleCN.TABLE_NAME,
                    FiringCycleCN.KILN_TYPE,
                    FiringCycleCN.RAMP_HOLD_LONG_STRING);

    private static final String SQL_CREATE_ENTRIES_INGREDIENT =
            generateCreateEntriesString(IngredientCN.TABLE_NAME,
                    IngredientCN.ALIASES,
                    IngredientCN.OXIDE_QUANTITY_LONG_STRING,
                    IngredientCN.COST_PER_KG);


    /*--------------------INHERITED DB FUNCTIONS--------------------*/
    public void onCreate(SQLiteDatabase db) {
        System.out.println("db onCreate called");
        db.execSQL("DROP TABLE IF EXISTS \"" + SingleCN.TABLE_NAME + "\"");
        db.execSQL("DROP TABLE IF EXISTS \"" + ComboCN.TABLE_NAME + "\"");
        db.execSQL("DROP TABLE IF EXISTS \"" + FiringCycleCN.TABLE_NAME + "\"");
        db.execSQL("DROP TABLE IF EXISTS \"" + IngredientCN.TABLE_NAME + "\"");

        db.execSQL(SQL_CREATE_ENTRIES_SINGLE);
        db.execSQL(SQL_CREATE_ENTRIES_COMBO);
        db.execSQL(SQL_CREATE_ENTRIES_FIRINGCYCLE);
        db.execSQL(SQL_CREATE_ENTRIES_INGREDIENT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
