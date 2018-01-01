package nh.glazelog.database;

import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nh.glazelog.R;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.IngredientQuantity;

/**
 * Created by Nick Hansen on 11/1/2017.
 */

public class IngredientTextSaver implements TextWatcher {

    DbHelper dbHelper;
    Glaze gToSave;
    TableLayout ingredientTable;
    String cvKey;

    private Timer timer = new Timer();
    private final long DELAY = 500; // milliseconds

    public IngredientTextSaver(Context context, Glaze g, TableLayout table, boolean isMaterialsTable) {
        dbHelper = DbHelper.getSingletonInstance(context);
        gToSave = g;
        ingredientTable = table;
        if (isMaterialsTable)   cvKey = DbHelper.SingleCN.MATERIALS;
        else                    cvKey = DbHelper.SingleCN.ADDITIONS;
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
        System.out.println("IngredientEnum Text Saver called for " + cvKey + ".");
        ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
        for (int i = 0; i < ingredientTable.getChildCount(); i++) {
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
}
