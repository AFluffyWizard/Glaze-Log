package nh.glazelog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import nh.glazelog.KeyValues;
import nh.glazelog.DeleteDialog;
import nh.glazelog.R;
import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.IngredientSpinnerSaver;
import nh.glazelog.database.IngredientTextSaver;
import nh.glazelog.database.Saver;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.IngredientEnum;
import nh.glazelog.glaze.IngredientQuantity;

public class EditRecipe extends AppCompatActivity {

    DbHelper dbHelper;
    Glaze parentGlaze;
    TableLayout materialsTable;
    TableLayout additionsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        materialsTable = (TableLayout)findViewById(R.id.recipeMaterialsTable);
        additionsTable = (TableLayout)findViewById(R.id.recipeAdditionsTable);

        dbHelper = DbHelper.getSingletonInstance(getApplicationContext());

        Intent intent = getIntent();
        parentGlaze = intent.getParcelableExtra(KeyValues.KEY_GLAZE_EDITRECIPE);

        setSupportActionBar((Toolbar) findViewById(R.id.actionbarDefault));
        ActionBar ab = getSupportActionBar();
        ab.setTitle(parentGlaze.getName() + " Recipe");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();


        for (IngredientQuantity iq : parentGlaze.getMaterials())
            addRecipeRow(iq,materialsTable,true);

        for (IngredientQuantity iq : parentGlaze.getAdditions())
            addRecipeRow(iq,additionsTable,false);


        Button materialsAddLineButton = (Button) findViewById(R.id.materialAddLineButton);
        materialsAddLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipeRow(null,materialsTable,true);
            }
        });

        Button additionsAddLineButton = (Button) findViewById(R.id.additionsAddLineButton);
        additionsAddLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipeRow(null,additionsTable,true);
            }
        });


    }


    private void addRecipeRow (IngredientQuantity iq, final TableLayout recipeTable, final boolean isMaterials) {
        final TableRow recipeRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_recipe,null);
        final DeleteDialog deleteDialog = new DeleteDialog(this, "row", new DeleteDialog.Action() {
            @Override
            public void action() {
                recipeTable.removeView(recipeRow);
                Saver.ingredientWithoutInstance(getApplicationContext(), parentGlaze,recipeTable,isMaterials);
                fixTables();
            }
        });

        final SearchableSpinner ingredient = (SearchableSpinner) recipeRow.findViewById(R.id.ingredientSpinner);
        ingredient.setAdapter(new ArrayAdapter<IngredientEnum>(this.getApplicationContext(),/*android.R.layout.select_dialog_item*/R.layout.spinner_item_small, IngredientEnum.values()));
        final EditText amount = (EditText) recipeRow.findViewById(R.id.amountEditText);
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("FOCUS: " + hasFocus);
                if (hasFocus) {
                    amount.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            amount.setSelection(amount.getText().length());
                        }
                    },50);
                }
            }
        });

        ImageView deleteRow = (ImageView) recipeRow.findViewById(R.id.deleteRowImageView);
        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });

        if (iq == null) {
            amount.setText("");
            amount.setHint("0");
        }
        else {
            Util.setSpinnerSelection(ingredient,iq.getIngredientEnum());
            amount.setText(new Double(iq.getAmount()).toString());
        }


        recipeRow.postDelayed(new Runnable() {
            @Override
            public void run() {
                ingredient.setOnItemSelectedListener(new IngredientSpinnerSaver(getApplicationContext(), parentGlaze,recipeTable,isMaterials));
                amount.addTextChangedListener(new IngredientTextSaver(getApplicationContext(), parentGlaze,recipeTable,isMaterials));
            }
        }, Util.CONST_DELAY_ADD_LISTENER);

        recipeTable.addView(recipeRow);
    }

    /* For some ungodly reason
     * whenever I add rows to any of the tables
     * the others glitch the fuck out.
     * I thought this was an emulator issue
     * but it also happened on my phone,
     * so I had to implement this to fix the issue.
     * When I originally implemented the tables
     * this wasn't a problem.
     * I have no idea where it came from
     * and no idea how to actually fix the root cause of it.
     * This is more of a quick fix, but it works.
     *
     * Quote me on that.
     *
     * Nick Hansen 11/12/17
     *
     *
     *
     * Apparently it occurs not just when adding rows
     * but whenever I edit any table at ALL
     * (i.e. removing rows causes it as well).
     * I've added this to the remove table image buttons as well.
     *
     * Nick Hansen 11/13/17
     */
    void fixTables () {
        materialsTable.requestLayout();
        additionsTable.requestLayout();
    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.actionbuttons_recipe,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: break;
            case android.R.id.home:
                Util.navigateUp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
