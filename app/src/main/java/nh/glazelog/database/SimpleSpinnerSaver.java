package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nick Hansen on 10/30/2017.
 */

public class SimpleSpinnerSaver extends SpinnerSaver {

    public SimpleSpinnerSaver(Context context, Storable s, String key, boolean appendAllVersions) {
        super(context,s,key,appendAllVersions);
    }

    @Override
    protected void save(AdapterView<?> callingParent, int positionSelected) {
        System.out.println("Spinner Saver called for " + cvKey + ".");
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey,callingParent.getItemAtPosition(positionSelected).toString());
        if (appendAllVersions)  dbHelper.appendAllVersions(sToSave,cvToSave);
        else                    dbHelper.append(sToSave,cvToSave);
        System.out.println("\"" + cvToSave.get(cvKey) + "\" saved in column \"" + cvKey + "\"");
    }

}
