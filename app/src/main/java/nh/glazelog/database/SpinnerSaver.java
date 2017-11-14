package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Created by Nick Hansen on 10/30/2017.
 */

public abstract class SpinnerSaver implements AdapterView.OnItemSelectedListener {

    DBHelper dbHelper;
    Storable sToSave;
    String cvKey;
    boolean appendAllVersions;

    public SpinnerSaver(Context context, Storable s, String key, boolean appendAllVersions) {
        dbHelper = DBHelper.getSingletonInstance(context);
        sToSave = s;
        cvKey = key;
        this.appendAllVersions = appendAllVersions;
    }

    public final void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        save(parent,position);
    }

    @Override
    public final void onNothingSelected(AdapterView<?> parent) {
        // nothing happens
    }

    protected abstract void save(AdapterView<?> callingParent, int positionSelected);

}
