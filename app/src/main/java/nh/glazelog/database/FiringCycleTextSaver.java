package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nh.glazelog.R;
import nh.glazelog.glaze.RampHold;

/**
 * Created by Nick Hansen on 11/1/2017.
 */

public class FiringCycleTextSaver implements TextWatcher {

    DbHelper dbHelper;
    Storable sToSave;
    TableLayout firingCycleTable;
    private static final String cvKey = DbHelper.SingleCN.FIRING_CYCLE;

    private Timer timer = new Timer();
    private final long DELAY = 500; // milliseconds

    public FiringCycleTextSaver(Context context, Storable s, TableLayout table) {
        dbHelper = DbHelper.getSingletonInstance(context);
        sToSave = s;
        firingCycleTable = table;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        timer.cancel();
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run() {
                        save();
                    }
                },
                DELAY
        );
    }

    public void save() {
        System.out.println("Firing Cycle Text Saver called");
        ArrayList<RampHold> rampHolds = new ArrayList<>();
        for (int i = 1; i < firingCycleTable.getChildCount(); i++) {
            TableRow row = (TableRow) firingCycleTable.getChildAt(i);
            String temp = ((TextView)row.findViewById(R.id.temperatureEditText)).getText().toString();
            String rate = ((TextView)row.findViewById(R.id.rateEditText)).getText().toString();
            String hold = ((TextView)row.findViewById(R.id.holdEditText)).getText().toString();
            rampHolds.add(new RampHold(temp,rate,hold));
        }
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey,RampHold.toLongString(rampHolds));
        dbHelper.append(sToSave,cvToSave);
        System.out.println("\"" + cvToSave.get(cvKey).toString() + "\" saved in column \"" + cvKey + "\".");
    }

}
