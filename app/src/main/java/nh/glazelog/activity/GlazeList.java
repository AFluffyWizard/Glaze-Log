package nh.glazelog.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nh.glazelog.KeyValues;
import nh.glazelog.R;
import nh.glazelog.Util;
import nh.glazelog.database.*;
import nh.glazelog.glaze.*;

import static android.view.View.GONE;

public class GlazeList extends AppCompatActivity {


    public class DbLoader extends AsyncTask<String,Void,DbHelper> {
        @Override
        protected DbHelper doInBackground(String... params) {
            return DbHelper.getSingletonInstance(getApplicationContext());
        }
        @Override
        protected void onPostExecute(DbHelper result) {
            dbHelper = result;
            populateList(Storable.Type.SINGLE);
        }

    }
    DbLoader glazeDbLoader = new DbLoader();
    DbHelper dbHelper;
    DrawerLayout navDrawer;
    NavigationView navView;
    AlertDialog newItemDialog;

    ArrayList<String> itemNames;
    Class itemActivityClass;
    Storable.Type lastOpenedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate called");
        setContentView(R.layout.activity_glaze_list);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.show();

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_item,null);
        final EditText itemNameField = (EditText) dialogView.findViewById(R.id.itemNameField);
        newItemDialog = new AlertDialog.Builder(this)
            .setPositiveButton(R.string.list_dialog_button_positive,null)
            .setNegativeButton(R.string.list_dialog_button_negative,null)
            .setView(dialogView)
            .create();

        FloatingActionButton addGlazeFab = (FloatingActionButton) findViewById(R.id.fabAddGlaze);
        addGlazeFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newItemDialog.show();
                newItemDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newItemName = itemNameField.getText().toString();
                        if (itemNames.contains(newItemName))
                            Toast.makeText(getApplicationContext(),R.string.list_dialog_failed,Toast.LENGTH_LONG).show();
                        else {
                            openNewItem(newItemName);
                            newItemDialog.dismiss();
                        }
                    }
                });
                newItemDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newItemDialog.dismiss();
                    }
                });
            }
        });
    }

    private <T extends Listable> void populateList(Storable.Type type) {
        // perform init actions
        if (navDrawer != null) navDrawer.closeDrawer(Gravity.LEFT,true);
        this.itemActivityClass = type.getActivityClass();
        getSupportActionBar().setTitle(type.getListTitle());
        newItemDialog.setTitle(type.getDialogTitle());
        itemNames = dbHelper.getDistinctNames(type);
        lastOpenedList = type;

        // obtain a reference to the list
        LinearLayout list = (LinearLayout) findViewById(R.id.layoutList);
        list.removeAllViews();

        // load items
        final ArrayList<ArrayList<T>> itemsWithVersions = new ArrayList<>();
        for (String name : dbHelper.getDistinctNames(type)) {
            ArrayList<T> item = Util.<T>typeUntypedList(dbHelper.readSingle(type, dbHelper.CCN_NAME,name));
            if (item.size() != 0) itemsWithVersions.add(item);
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
                    openItem(item);
                }
            });

            // place info in view
            Listable rootItem = itemList.get(0);
            Listable currentItem = itemList.get(itemList.size()-1);

            TextView nameLabel = (TextView) itemView.findViewById(R.id.itemNameLabel);
            nameLabel.setText(rootItem.getName());

            TextView editDateLabel = (TextView) itemView.findViewById(R.id.dateEditedLabel);
            editDateLabel.setText(Util.getShortDate(Util.getMostRecentEditDate(itemList)));

            TextView secondaryInfoLabel = (TextView) itemView.findViewById(R.id.secondaryInfoLabel);
            secondaryInfoLabel.setText(currentItem.getSecondaryInfo(itemList));
            if (secondaryInfoLabel.getText().equals("GONE")) secondaryInfoLabel.setVisibility(GONE);

            if (type.hasImage()) {
                ImageView previewImage = (ImageView) itemView.findViewById(R.id.previewImageView);
                if (currentItem.getImageUri() != Uri.EMPTY) previewImage.setImageURI(currentItem.getImageUri());
            }

            // add view to list
            list.addView(itemView);
        }
        System.out.println("Item list loaded");
    }

    public <T> void openItem (ArrayList<T> itemToOpen) {
        Intent itemIntent = new Intent(this, itemActivityClass);
        itemIntent.putExtra(KeyValues.KEY_ITEM_FROM_LIST,itemToOpen);
        startActivity(itemIntent);
    }

    public <T> void openNewItem (String name) {
        Intent itemIntent = new Intent(this, itemActivityClass);
        itemIntent.putExtra(KeyValues.KEY_ITEM_NEWNAME,name);
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
                    case R.id.navDrawerSingle:
                        populateList(Storable.Type.SINGLE);
                        return true;
                    case R.id.navDrawerCombo:
                        populateList(Storable.Type.COMBO);
                        return true;
                    case R.id.navDrawerFiringCycle:
                        populateList(Storable.Type.FIRING_CYCLE);
                        return true;
                    case R.id.navDrawerIngredient:
                        populateList(Storable.Type.INGREDIENT);
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
        initNavDrawer();
        if (dbHelper == null) {
            glazeDbLoader.execute("");
        }
        else {
            populateList(lastOpenedList);
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