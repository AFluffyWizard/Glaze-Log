package nh.glazelog.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import nh.glazelog.R;
import nh.glazelog.Util;
import nh.glazelog.database.*;
import nh.glazelog.glaze.*;

public class GlazeList extends AppCompatActivity {

    public final static String KEY_GLAZE_TEMPLATE = "nh.glazelog.TEMPLATE";
    public final static String KEY_GLAZE_SINGLE = "nh.glazelog.SINGLE";
    public final static String KEY_GLAZE_COMBO = "nh.glazelog.COMBO";


    public class DbLoader extends AsyncTask<String,Void,DbHelper> {
        @Override
        protected DbHelper doInBackground(String... params) {
            return DbHelper.getSingletonInstance(getApplicationContext());
        }
        @Override
        protected void onPostExecute(DbHelper result) {
            dbHelper = result;
            populateList(Storable.Type.SINGLE);
            populateTemplateSpinner();
        }

    }
    DbLoader glazeDbLoader = new DbLoader();
    DbHelper dbHelper;
    Spinner templateSpinner;
    DrawerLayout navDrawer;
    NavigationView navView;

    ArrayList<ArrayList<Glaze>> glazesWithVersions;
    public static int numInList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate called");
        setContentView(R.layout.activity_glaze_list);

        setSupportActionBar((Toolbar) findViewById(R.id.listToolbar));
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.list_title_single);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.show();


        initNavDrawer();


        final AlertDialog newGlazeDialog = new AlertDialog.Builder(this).create();
        newGlazeDialog.setTitle(getString(R.string.dialog_newglaze_title));
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_glaze,null);
        templateSpinner = (Spinner) dialogView.findViewById(R.id.chooseTemplateSpinner);
        final EditText newGlazeNameEditText = (EditText) dialogView.findViewById(R.id.newNameEditText);
        newGlazeDialog.setView(dialogView);
        newGlazeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GlazeTemplate newGlazeTemplate = (GlazeTemplate) templateSpinner.getSelectedItem();
                newGlazeTemplate.setName(newGlazeNameEditText.getText().toString());
                openNewGlaze(newGlazeTemplate);
            }
        });
        newGlazeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                newGlazeDialog.hide();
            }
        });


        FloatingActionButton addGlazeFab = (FloatingActionButton) findViewById(R.id.fabAddGlaze);
        addGlazeFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newGlazeDialog.show();
            }
        });

    }

    private boolean populateList (@StringRes int resId, Storable.Type type) {
        getSupportActionBar().setTitle(resId);
        navDrawer.closeDrawer(Gravity.LEFT,true);
        return true;
    }

    private void populateList(Storable.Type type) {
        // load necessary components
        LinearLayout list = (LinearLayout) findViewById(R.id.layoutList);
        list.removeAllViews();
        glazesWithVersions = new ArrayList<ArrayList<Glaze>>();
        for (String name : dbHelper.getDistinctNames(type)) {
            glazesWithVersions.add(Util.<Glaze>typeUntypedList(dbHelper.readSingle(type, dbHelper.CCN_NAME,name)));
        }

        // create view for each glaze
        numInList = 0;
        for (ArrayList<Glaze> gList : glazesWithVersions) {
            // init individual view
            View singleGlaze = getLayoutInflater().inflate(R.layout.list_item_glaze_single,list,false);
            singleGlaze.setId(numInList);
            singleGlaze.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openGlaze(glazesWithVersions.get((v.getId())));
                }
            });

        // edit view with glaze information
            // create views programmatically
            ImageView testTileImage = (ImageView) singleGlaze.findViewById(R.id.testTileImageView);
            TextView name = (TextView) singleGlaze.findViewById(R.id.glazeNameLabel);
            TextView cone = (TextView) singleGlaze.findViewById(R.id.glazeConeLabel);
            TextView date = (TextView) singleGlaze.findViewById(R.id.glazeDateLabel);

            // prepare information
            Glaze rootGlaze = gList.get(0);
            Glaze currentGlaze = gList.get(gList.size()-1);
            Cone closestCone = Cone.NONE;
            if (rootGlaze.getFiringCycle().size() != 0)
                closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(gList.get(gList.size()-1).getFiringCycle()));

            // add information to views
            name.setText(rootGlaze.getName());
            cone.setText(closestCone.toString());
            date.setText(Util.getShortDate(rootGlaze.getDateEditedRaw()));
            if (currentGlaze.getImageUri() != Uri.EMPTY) testTileImage.setImageURI(currentGlaze.getImageUri());

            // add view to list and increment counter
            list.addView(singleGlaze);
            numInList++;
        }
        System.out.println("Glaze list loaded");
    }

    public void populateTemplateSpinner() {
        ArrayList<GlazeTemplate> allTemplates = Util.<GlazeTemplate>typeUntypedList(dbHelper.readAll(Storable.Type.TEMPLATE));
        templateSpinner.setAdapter(new ArrayAdapter<GlazeTemplate>(this,android.R.layout.simple_spinner_dropdown_item,allTemplates));
        System.out.println("Template lost loaded");
    }

    public void openNewGlaze(GlazeTemplate template) {
        Intent templateIntent = new Intent(this, SingleGlaze.class);
        templateIntent.putExtra(KEY_GLAZE_TEMPLATE,template);
        startActivity(templateIntent);
    }

    public void openGlaze (ArrayList<Glaze> glazeToOpen) {
        Intent glazeIntent = new Intent(this, SingleGlaze.class);
        glazeIntent.putExtra(KEY_GLAZE_SINGLE,glazeToOpen);
        startActivity(glazeIntent);
    }

    private void initNavDrawer() {
        navDrawer = (DrawerLayout) findViewById(R.id.navDrawerLayout);
        navView = (NavigationView) findViewById(R.id.navView);

        //navView.setItemTextAppearance(R.style.StandardText);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    default: break;
                    case R.id.navDrawerSingle:
                        return populateList(R.string.list_title_single, Storable.Type.SINGLE);
                    case R.id.navDrawerCombo:
                        return populateList(R.string.list_title_combo, Storable.Type.COMBO);
                    case R.id.navDrawerTemplate:
                        return populateList(R.string.list_title_templates, Storable.Type.TEMPLATE);
                    case R.id.navDrawerFiringCycle:
                        return populateList(R.string.list_title_firingcycle, Storable.Type.FIRING_CYCLE);
                    case R.id.navDrawerIngredient:
                        //return populateList(R.string.list_title_ingredients, Storable.Type.INGREDIENT);
                        return true;
                }
                return false;
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart called");
        if (dbHelper == null) {
            glazeDbLoader.execute("");
        }
        else {
            populateList(Storable.Type.SINGLE);
            populateTemplateSpinner();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.actionbuttons_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: break;
            case android.R.id.home:
                navDrawer.openDrawer(Gravity.LEFT,true);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_resetdb:
                //TODO - IMPLEMENT, PREFERRABLY IN SETTINGS ACTIVITY AND WITH NESTED CONFIRM DIALOGS
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
