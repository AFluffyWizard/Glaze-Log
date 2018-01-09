package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nick Hansen on 10/26/2017.
 */

public class SimpleTextSaver extends TextSaver {

    public SimpleTextSaver(Context context, Storable s, String key, boolean appendAllVersions) {
        super(context,s,key,appendAllVersions);
    }

}
