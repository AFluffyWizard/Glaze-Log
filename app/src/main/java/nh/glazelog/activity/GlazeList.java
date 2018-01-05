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
    public final static String KEY_FIRING_CYCLE = "nh.glazelog.FIRINGCYCLE";
    public final static String KEY_INGREDIENT = "nh.glazelog.INGREDIENT";


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


    private <T extends Listable> boolean populateList(@StringRes int listTitleResId, Storable.Type type) {
        // perform init actions
        getSupportActionBar().setTitle(listTitleResId);
        if (navDrawer != null) navDrawer.closeDrawer(Gravity.LEFT,true);

        // create a reference to the list
        LinearLayout list = (LinearLayout) findViewById(R.id.layoutList);
        list.removeAllViews();

        // load necessary components
        final ArrayList<ArrayList<T>> itemsWithVersions = new ArrayList<>();
        for (String name : dbHelper.getDistinctNames(type)) {
            itemsWithVersions.add(Util.<T>typeUntypedList(dbHelper.readSingle(type, dbHelper.CCN_NAME,name)));
        }

        // create view for each glaze
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
                    openItem(itemsWithVersions.get((v.getId())));
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
            String secondaryInfo;
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


    public void populateTemplateSpinner() {
        ArrayList<GlazeTemplate> allTemplates = Util.<GlazeTemplate>typeUntypedList(dbHelper.readAll(Storable.Type.TEMPLATE));
        templateSpinner.setAdapter(new ArrayAdapter<GlazeTemplate>(this,android.R.layout.simple_spinner_dropdown_item,allTemplates));
        System.out.println("Template list loaded");
    }

    public void openNewGlaze(GlazeTemplate template) {
        Intent templateIntent = new Intent(this, SingleGlaze.class);
        templateIntent.putExtra(KEY_GLAZE_TEMPLATE,template);
        startActivity(templateIntent);
    }

    public <T> void openItem (ArrayList<T> itemToOpen) {
        Intent itemIntent = new Intent(this, SingleGlaze.class);
        itemIntent.putExtra(KEY_GLAZE_SINGLE,itemToOpen);
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
                        return populateList(R.string.list_title_single, Storable.Type.SINGLE);
                    case R.id.navDrawerCombo:
                        return populateList(R.string.list_title_combo, Storable.Type.COMBO);
                    case R.id.navDrawerTemplate:
                        return populateList(R.string.list_title_templates, Storable.Type.TEMPLATE);
                    case R.id.navDrawerFiringCycle:
                        return populateList(R.string.list_title_firingcycle, Storable.Type.FIRING_CYCLE);
                    case R.id.navDrawerIngredient:
                        return populateList(R.string.list_title_ingredients, Storable.Type.INGREDIENT);
                }
                return false;
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        initNavDrawer();
        System.out.println("onStart called");
        if (dbHelper == null) {
            glazeDbLoader.execute("");
        }
        else {
            populateList(R.string.list_title_single, Storable.Type.SINGLE);
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
