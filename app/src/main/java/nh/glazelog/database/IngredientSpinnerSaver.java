package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import nh.glazelog.R;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.IngredientQuantity;

/**
 * Created by Nick Hansen on 10/30/2017.
 */

public class IngredientSpinnerSaver extends SpinnerSaver {

    TableLayout ingredientTable;

    public IngredientSpinnerSaver(Context context, Glaze g, TableLayout table, boolean isMaterialsTable) {
        super(context,g, DbHelper.SingleCN.MATERIALS,false);
        if (!isMaterialsTable) cvKey = DbHelper.SingleCN.ADDITIONS;
        ingredientTable = table;
    }

    @Override
    protected void save(AdapterView<?> callingParent, int positionSelected) {
        saveWithoutParameters(); // I made my own function here to show neither parameter is needed in this implementation.
    }

    private void saveWithoutParameters() {
        System.out.println("IngredientEnum Text Saver called for " + cvKey + ".");
        ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        for (int i = 0; i < ingredientTable.getChildCount(); i++) {
            TableRow row = (TableRow) ingredientTable.getChildAt(i);
            String ingredient = ((Spinner)row.findViewById(R.id.ingredientSpinner)).getSelectedItem().toString();
            String amount = ((TextView)row.findViewById(R.id.amountEditText)).getText().toString();
            ingredientQuantities.add(new IngredientQuantity(ingredient,amount));
        }
        ContentValues cvToSave = new ContentValues();
        cvToSave.put(cvKey,IngredientQuantity.toLongString(ingredientQuantities));
        dbHelper.append(sToSave,cvToSave);
        System.out.println("\"" + cvToSave.get(cvKey).toString() + "\" saved in column \"" + cvKey + "\".");
    }

}
