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
    Glaze g;
    boolean isMaterials;

    public IngredientSpinnerSaver(Context context, Glaze g, TableLayout table, boolean isMaterialsTable) {
        super(context,g, DbHelper.SingleCN.MATERIALS,false);
        this.g = g;
        this.isMaterials = isMaterialsTable;
        if (!isMaterialsTable) cvKey = DbHelper.SingleCN.ADDITIONS;
        ingredientTable = table;
    }

    @Override
    protected void save(AdapterView<?> callingParent, int positionSelected) {
        saveWithoutParameters(); // I made my own function here to show neither parameter is needed in this implementation.
    }

    private void saveWithoutParameters() {
        System.out.println("IngredientEnum Spinner Saver called for " + cvKey + ".");
        ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        for (int i = 0; i < ingredientTable.getChildCount(); i++) {
            TableRow row = (TableRow) ingredientTable.getChildAt(i);
            String ingredient = ((Spinner)row.findViewById(R.id.ingredientSpinner)).getSelectedItem().toString();
            String amount = ((TextView)row.findViewById(R.id.amountEditText)).getText().toString();
            ingredientQuantities.add(new IngredientQuantity(ingredient,amount));
        }
        if (isMaterials)    g.setMaterials(ingredientQuantities);
        else                g.setAdditions(ingredientQuantities);
        dbHelper.append(sToSave,cvKey,IngredientQuantity.toLongString(ingredientQuantities));
        System.out.println("\"" + IngredientQuantity.toLongString(ingredientQuantities) + "\" saved in column \"" + cvKey + "\".");
    }

}
