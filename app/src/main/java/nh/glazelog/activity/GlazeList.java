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
        }

    }
    DbLoader glazeDbLoader = new DbLoader();
    DbHelper dbHelper;
    DrawerLayout navDrawer;
    NavigationView navView;
    AlertDialog newItemDialog;

    Class itemActivityClass;
    String itemIntentKey;

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

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_name,null);
        final EditText itemNameEditText = (EditText) dialogView.findViewById(R.id.nameEditText);
        newItemDialog = new AlertDialog.Builder(this).create();
        newItemDialog.setView(dialogView);
        newItemDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                newItemDialog.hide();
            }
        });
        newItemDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openNewItem(itemNameEditText.getText().toString());
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
        newItemDialog.setTitle(getString(R.string.dialog_newitem_title) + getString(listTitleResId));

        // create a reference to the list
        LinearLayout list = (LinearLayout) findViewById(R.id.layoutList);
        list.removeAllViews();

        // load necessary components
        final ArrayList<ArrayList<T>> itemsWithVersions = new ArrayList<>();
        for (String name : dbHelper.getDistinctNames(type)) {
            itemsWithVersions.add(Util.<T>typeUntypedList(dbHelper.readSingle(type, dbHelper.CCN_NAME,name)));
        }

        // create view for each item
        for (ArrayList<T> itemList : itemsWithVersions) {
            // init individual view
            View itemView;
            if (type.hasImage())    itemView = getLayoutInflater().inflate(R.layout.list_item_withimg,list,false);
            else                    itemView = getLayoutInflater().inflate(R.layout.list_item_noimg,list,false);
            final ArrayList<T> item = itemList;
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openItem(item.get(0).getName(), itemIntentKey, item);
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

            // add view to list
            list.addView(itemView);
        }
        System.out.println("Item list loaded");
        return true;
    }

    public <T> void openItem (String name, String key, ArrayList<T> itemToOpen) {
        Intent itemIntent = new Intent(this, itemActivityClass);
        itemIntent.putExtra(key,itemToOpen);
        itemIntent.putExtra(KeyValues.KEY_NAME,name);
        startActivity(itemIntent);
    }

    public <T> void openNewItem (String name) {
        Intent itemIntent = new Intent(this, itemActivityClass);
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
                        itemActivityClass = SingleGlaze.class;
                        itemIntentKey = KeyValues.KEY_GLAZE_SINGLE;
                        return populateList(R.string.list_title_single, Storable.Type.SINGLE);
                    case R.id.navDrawerCombo:
                        //itemActivityClass = SingleGlaze.class;
                        itemIntentKey = KeyValues.KEY_GLAZE_COMBO;
                        return populateList(R.string.list_title_combo, Storable.Type.COMBO);
                    case R.id.navDrawerFiringCycle:
                        itemActivityClass = EditFiringCycle.class;
                        itemIntentKey = KeyValues.KEY_FIRING_CYCLE;
                        return populateList(R.string.list_title_firingcycle, Storable.Type.FIRING_CYCLE);
                    case R.id.navDrawerIngredient:
                        //itemActivityClass = EditFiringCycle.class;
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
        itemActivityClass = SingleGlaze.class;
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