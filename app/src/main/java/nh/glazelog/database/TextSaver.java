package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.Timer;
import java.util.TimerTask;

import nh.glazelog.glaze.Glaze;

/**
 * Created by Nick Hansen on 10/26/2017.
 */

public class TextSaver implements TextWatcher {

    DBHelper dbHelper;
    Storable sToSave;
    String cvKey;
    boolean appendAll;
    boolean saveEmptyText;

    private Timer timer = new Timer();
    private final long DELAY = 350; // milliseconds

    public TextSaver(Context context, Storable s, String key, boolean appendAllVersions, boolean saveEmptyText) {
        dbHelper = DBHelper.getSingletonInstance(context);
        sToSave = s;
        cvKey = key;
        appendAll = appendAllVersions;
        this.saveEmptyText = saveEmptyText;
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
                        System.out.println("Text saver called.");
                        ContentValues cvToSave = new ContentValues();
                        cvToSave.put(cvKey, newString);
                        if (newString.equals("") && saveEmptyText) {
                            /* When saving the glaze's name
                             * because the glazes are referenced based on their name
                             * a blank name causes the program to fuck up
                             * and not save the name properly.
                             * This prevents that.
                             *
                             * Nick Hansen 11/13/17
                             */
                            System.out.println("Text empty - no text saved.");
                        }
                        else {
                            if (appendAll) dbHelper.appendAllVersions(sToSave, cvToSave);
                            else dbHelper.append(sToSave, cvToSave);
                            System.out.println("\"" + newString + "\" saved in column \"" + cvKey + "\".");
                        }
                    }
                },
                DELAY
        );
    }

}
