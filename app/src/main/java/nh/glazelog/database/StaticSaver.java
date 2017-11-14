package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import nh.glazelog.R;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.IngredientQuantity;
import nh.glazelog.glaze.RampHold;

/**
 * Created by Nick Hansen on 11/8/2017.
 */

public class StaticSaver {

    public static void ingredientWithoutInstance(Context context, Glaze gToSave, TableLayout ingredientTable, boolean isMaterialsTable) {
        String cvKey;
        if (isMaterialsTable)   cvKey = DBHelper.SingleCN.MATERIALS;
        else                    cvKey = DBHelper.SingleCN.ADDITIONS;
        System.out.println("Ingredient Text Saver called without instance for " + cvKey + ".");
        DBHelper dbHelper = DBHelper.getSingletonInstance(context);

        ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        for (int i = 1; i < ingredientTable.getChildCount(); i++) {
            TableRow row = (TableRow) ingredientTable.getChildAt(i);
            String ingredient = ((Spinner)row.findViewById(R.id.ingredientEditText)).getSelectedItem().toString();
            String amount = ((TextView)row.findViewById(R.id.amountEditText)).getText().toString();
            ingredientQuantities.add(new IngredientQuantity(ingredient,amount));
        }
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey,IngredientQuantity.toLongString(ingredientQuantities));
        dbHelper.append(gToSave,cvToSave);
        System.out.println("\"" + cvToSave.get(cvKey).toString() + "\" saved in column \"" + cvKey + "\".");
    }

    public static void firingCycleWithoutInstance(Context context, Storable sToSave, TableLayout firingCycleTable) {
        String cvKey = DBHelper.CCN_FIRING_CYCLE;
        System.out.println("Firing Cycle Text Saver called without instance");
        DBHelper dbHelper = DBHelper.getSingletonInstance(context);
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
