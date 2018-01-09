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

import nh.glazelog.KeyValues;
import nh.glazelog.R;
import nh.glazelog.Util;
import nh.glazelog.database.*;
import nh.glazelog.glaze.*;

public class GlazeList extends AppCompatActivity {


    public class DbLoader extends AsyncTask<String,Void,DbHelper> {
        @Override
        protected DbHelper doInBackground(String... params) {
            return DbHelper.getSingletonInstance(getApplicationContext());
        }
        @Override
        protected void onPostExecute(DbHelper result) {
            dbHelper = result;
            populateList(R.string.list_title_single, Storable.Type.SINGLE);
            populateTemplateSpinner();
        }

    }
    DbLoader glazeDbLoader = new DbLoader();
    DbHelper dbHelper;
    Spinner templateSpinner;
    DrawerLayout navDrawer;
    NavigationView navView;
    AlertDialog newItemDialog;

    Class activityToOpen;
    String itemIntentKey;
    DialogInterface.OnClickListener fabOpenAction;

    ArrayList<ArrayList<Glaze>> glazesWithVersions;
    public static int numInList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate called");
        setContentView(R.layout.activity_glaze_list);

        setSupportActionBar((Toolbar) findViewById(R.id.actionbarDefault));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.show();

        newItemDialog = new AlertDialog.Builder(this).create();
        newItemDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create", fabOpenAction);
        newItemDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                newItemDialog.hide();
            }
        });

        FloatingActionButton addGlazeFab = (FloatingActionButton) findViewById(R.id.fabAddGlaze);
        addGlazeFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newItemDialog.show();
            }
        });
    }


    private <T extends Listable> boolean populateList(@StringRes int listTitleResId, Storable.Type type) {
        // perform init actions
        getSupportActionBar().setTitle(listTitleResId);
        if (navDrawer != null) navDrawer.closeDrawer(Gravity.LEFT,true);
        setFabAction(listTitleResId, type);

        // create a reference to the list
        LinearLayout list = (LinearLayout) findViewById(R.id.layoutList);
        list.removeAllViews();

        // load necessary components
        final ArrayList<ArrayList<T>> itemsWithVersions = new ArrayList<>();
        for (String name : dbHelper.getDistinctNames(type)) {
            itemsWithVersions.add(Util.<T>typeUntypedList(dbHelper.readSingle(type, dbHelper.CCN_NAME,name)));
        }

        // create view for each item
        numInList = 0;
        for (ArrayList<T> itemList : itemsWithVersions) {
            // prevent showing and subsequently allowing editing of the basic blank template
            if (itemList.get(0) instanceof GlazeTemplate && itemList.get(0).getName() == "No Template")
                break;

            // init individual view
            View itemView;
            if (type.hasImage())    itemView = getLayoutInflater().inflate(R.layout.list_item_withimg,list,false);
            else                    itemView = getLayoutInflater().inflate(R.layout.list_item_noimg,list,false);
            itemView.setId(numInList);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openItem(activityToOpen, itemIntentKey, itemsWithVersions.get((v.getId())));
                }
            });


            // place info in view
            Listable rootItem = itemList.get(0);
            Listable currentItem = itemList.get(itemList.size()-1);

            TextView nameLabel = (TextView) itemView.findViewById(R.id.itemNameLabel);
            nameLabel.setText(rootItem.getName());

            TextView editDateLabel = (TextView) itemView.findViewById(R.id.dateEditedLabel);
            editDateLabel.setText(Util.getShortDate(rootItem.getDateEditedRaw()));

            TextView secondaryInfoLabel = (TextView) itemView.findViewById(R.id.secondaryInfoLabel);
            secondaryInfoLabel.setText(currentItem.getSecondaryInfo(itemList));

            if (type.hasImage()) {
                ImageView previewImage = (ImageView) itemView.findViewById(R.id.previewImageView);
                if (currentItem.getImageUri() != Uri.EMPTY) previewImage.setImageURI(currentItem.getImageUri());
            }

            // add view to list and increment counter
            list.addView(itemView);
            numInList++;
        }
        System.out.println("Item list loaded");
        return true;
    }

    private void setFabAction(@StringRes int listTitleResId, Storable.Type type) {
        newItemDialog.setTitle(getString(R.string.dialog_newitem_title) + getString(listTitleResId));
        View dialogView;
        if (type == Storable.Type.SINGLE) {
            dialogView = getLayoutInflater().inflate(R.layout.dialog_new_glaze,null);
            newItemDialog.setView(dialogView);
            final EditText newGlazeNameEditText = (EditText) dialogView.findViewById(R.id.newNameEditText);
            templateSpinner = (Spinner) dialogView.findViewById(R.id.chooseTemplateSpinner);
            populateTemplateSpinner();
            fabOpenAction = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    openNewGlaze((GlazeTemplate) templateSpinner.getSelectedItem(), newGlazeNameEditText.getText().toString());
                }
            };
        }
        else {
            dialogView = getLayoutInflater().inflate(R.layout.dialog_new_name,null);
            newItemDialog.setView(dialogView);
            final EditText itemNameEditText = (EditText) dialogView.findViewById(R.id.nameEditText);
            fabOpenAction = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    openNewItem(activityToOpen,itemNameEditText.getText().toString());
                }
            };
        }
    }


    public void populateTemplateSpinner() {
        ArrayList<GlazeTemplate> allTemplates = Util.<GlazeTemplate>typeUntypedList(dbHelper.readAll(Storable.Type.TEMPLATE));
        templateSpinner.setAdapter(new ArrayAdapter<GlazeTemplate>(this,android.R.layout.simple_spinner_dropdown_item,allTemplates));
        System.out.println("Template list loaded");
    }

    public void openNewGlaze(GlazeTemplate template, String name) {
        Intent newGlazeIntent = new Intent(this, SingleGlaze.class);
        newGlazeIntent.putExtra(KeyValues.KEY_GLAZE_TEMPLATE,template);
        newGlazeIntent.putExtra(KeyValues.KEY_NAME, name);
        startActivity(newGlazeIntent);
    }

    public <T> void openItem (Class activityClass, String key, ArrayList<T> itemToOpen) {
        Intent itemIntent = new Intent(this, activityClass);
        itemIntent.putExtra(key,itemToOpen);
        startActivity(itemIntent);
    }

    public <T> void openNewItem (Class activityClass, String name) {
        Intent itemIntent = new Intent(this, activityClass);
        itemIntent.putExtra(KeyValues.KEY_NAME,name);
        startActivity(itemIntent);
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
                        activityToOpen = SingleGlaze.class;
                        itemIntentKey = KeyValues.KEY_GLAZE_SINGLE;
                        return populateList(R.string.list_title_single, Storable.Type.SINGLE);
                    case R.id.navDrawerCombo:
                        //activityToOpen = SingleGlaze.class;
                        itemIntentKey = KeyValues.KEY_GLAZE_COMBO;
                        return populateList(R.string.list_title_combo, Storable.Type.COMBO);
                    case R.id.navDrawerTemplate:
                        //activityToOpen = SingleGlaze.class;
                        itemIntentKey = KeyValues.KEY_GLAZE_TEMPLATE;
                        return populateList(R.string.list_title_templates, Storable.Type.TEMPLATE);
                    case R.id.navDrawerFiringCycle:
                        activityToOpen = EditFiringCycle.class;
                        itemIntentKey = KeyValues.KEY_FIRING_CYCLE;
                        return populateList(R.string.list_title_firingcycle, Storable.Type.FIRING_CYCLE);
                    case R.id.navDrawerIngredient:
                        //activityToOpen = EditFiringCycle.class;
                        itemIntentKey = KeyValues.KEY_INGREDIENT;
                        return populateList(R.string.list_title_ingredients, Storable.Type.INGREDIENT);
                }
                return false;
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart called");
        initNavDrawer();
        activityToOpen = SingleGlaze.class;
        if (dbHelper == null) {
            glazeDbLoader.execute("");
        }
        else {
            populateList(R.string.list_title_single, Storable.Type.SINGLE);
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
