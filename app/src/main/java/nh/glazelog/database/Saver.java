package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import nh.glazelog.R;
import nh.glazelog.glaze.FiringCycle;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.IngredientQuantity;
import nh.glazelog.glaze.RampHold;

/**
 * Created by Nick Hansen on 11/8/2017.
 */

public class Saver {

    private static DbHelper dbHelper;
    private static String cvKey;

    public static void ingredientWithoutInstance(Context context, Glaze gToSave, TableLayout ingredientTable, boolean isMaterialsTable) {
        if (isMaterialsTable)   cvKey = DbHelper.SingleCN.MATERIALS;
        else                    cvKey = DbHelper.SingleCN.ADDITIONS;
        System.out.println("IngredientEnum Text Saver called without instance for " + cvKey + ".");
        if (dbHelper == null) dbHelper = DbHelper.getSingletonInstance(context);

        ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        for (int i = 1; i < ingredientTable.getChildCount(); i++) {
            TableRow row = (TableRow) ingredientTable.getChildAt(i);
            String ingredient = ((Spinner)row.findViewById(R.id.ingredientSpinner)).getSelectedItem().toString();
            String amount = ((TextView)row.findViewById(R.id.amountEditText)).getText().toString();
            ingredientQuantities.add(new IngredientQuantity(ingredient,amount));
        }
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey,IngredientQuantity.toLongString(ingredientQuantities));
        dbHelper.append(gToSave,cvToSave);
        System.out.println("\"" + cvToSave.get(cvKey).toString() + "\" saved in column \"" + cvKey + "\".");
    }

    public static void firingCycleRampHold(Context context, FiringCycle fcToSave, TableLayout firingCycleTable) {
        System.out.println("Firing Cycle Text Saver called without instance");
        cvKey = DbHelper.FiringCycleCN.RAMP_HOLD_LONG_STRING;
        if (dbHelper == null) dbHelper = DbHelper.getSingletonInstance(context);
        ArrayList<RampHold> rampHolds = new ArrayList<>();
        for (int i = 1; i < firingCycleTable.getChildCount(); i++) {
            TableRow row = (TableRow) firingCycleTable.getChildAt(i);
            String temp = ((TextView)row.findViewById(R.id.temperatureEditText)).getText().toString();
            String rate = ((TextView)row.findViewById(R.id.rateEditText)).getText().toString();
            String hold = ((TextView)row.findViewById(R.id.holdEditText)).getText().toString();
            rampHolds.add(new RampHold(temp,rate,hold));
        }
        genericSave(context,fcToSave,cvKey,RampHold.toLongString(rampHolds));
    }

    public static void genericSave(Context context, Storable sToSave, String key, String dataToSave) {
        System.out.println("Firing Cycle Text Saver called without instance");
        cvKey = key;
        if (dbHelper == null) dbHelper = DbHelper.getSingletonInstance(context);
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey,dataToSave);
        dbHelper.append(sToSave,cvToSave);
        System.out.println("\"" + cvToSave.get(cvKey).toString() + "\" saved in column \"" + cvKey + "\" of " + sToSave.getName() + ".");
    }


}
