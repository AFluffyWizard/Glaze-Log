package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nick Hansen on 10/26/2017.
 */

public class TextSaver implements TextWatcher {

    Context context;
    DbHelper dbHelper;
    Storable sToSave;
    String cvKey;
    boolean appendAll;

    private Timer timer = new Timer();
    private final long DELAY = 350; // milliseconds

    public TextSaver(Context context, Storable s, String key, boolean appendAllVersions) {
        this.context = context;
        dbHelper = DbHelper.getSingletonInstance(context);
        sToSave = s;
        cvKey = key;
        appendAll = appendAllVersions;
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // do nothing
    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {} // do nothing

    @Override
    final public void afterTextChanged(Editable s) {
        final String newString = s.toString();
        timer.cancel();
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run() {
                        save(newString);
                    }
                },
                DELAY
        );
    }

    public void save(String newString) {
        System.out.println("Text saver called.");
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey, newString);
        if (appendAll) dbHelper.appendAllVersions(sToSave, cvToSave);
        else dbHelper.append(sToSave, cvToSave);
        System.out.println("\"" + newString + "\" saved in column \"" + cvKey + "\".");
    }

}
