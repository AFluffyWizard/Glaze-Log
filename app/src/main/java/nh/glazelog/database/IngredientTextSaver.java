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

/**
 * Created by Nick Hansen on 11/1/2017.
 */

public class IngredientTextSaver extends TextSaver {

    TableLayout ingredientTable;
    Glaze g;
    boolean isMaterials;

    public IngredientTextSaver(Context context, Glaze g, TableLayout ingredientTable, boolean isMaterialsTable) {
        super(context,g,"",false);
        this.ingredientTable = ingredientTable;
        this.g = g;
        this.isMaterials = isMaterialsTable;
        if (isMaterialsTable)   cvKey = DbHelper.SingleCN.MATERIALS;
        else                    cvKey = DbHelper.SingleCN.ADDITIONS;
    }

    public void save(String newString) {
        System.out.println("IngredientEnum Text Saver called for " + cvKey + ".");
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
        System.out.println("\"" + IngredientQuantity.toLongString(ingredientQuantities) + "\" saved in column \"" + cvKey + "\" of " + sToSave.getName() + ".");
    }
}
