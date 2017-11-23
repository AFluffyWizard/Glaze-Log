package nh.glazelog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import nh.glazelog.database.*;
import nh.glazelog.glaze.*;

public class GlazeList extends AppCompatActivity {

    public final static String KEY_GLAZE_TEMPLATE = "nh.glazelog.TEMPLATE";
    public final static String KEY_GLAZE_SINGLE = "nh.glazelog.SINGLE";
    public final static String KEY_GLAZE_COMBO = "nh.glazelog.COMBO";


    public class DbLoader extends AsyncTask<String,Void,DBHelper> {
        @Override
        protected DBHelper doInBackground(String... params) {
            return DBHelper.getSingletonInstance(getApplicationContext());
        }
        @Override
        protected void onPostExecute(DBHelper result) {
            dbHelper = result;

            populateGlazeList(Storable.Type.SINGLE);
            populateTemplateList();
        }

    }
    DbLoader glazeDbLoader = new DbLoader();
    DBHelper dbHelper;
    Spinner templateSpinner;

    ArrayList<ArrayList<Glaze>> glazesWithVersions;
    int numGlazes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate called");
        setContentView(R.layout.activity_glaze_list);

        /*
        TODO - ENABLE TOOLBAR
        setSupportActionBar((Toolbar) findViewById(R.id.listToolbar));
        ActionBar ab = getSupportActionBar();
        ab.show();
        */




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

    public void populateGlazeList(Storable.Type type) {
        // load necessary components
        LinearLayout list = (LinearLayout) findViewById(R.id.layoutList);
        list.removeAllViews();
        glazesWithVersions = new ArrayList<ArrayList<Glaze>>();
        for (String name : dbHelper.getDistinctNames(type)) {
            glazesWithVersions.add(Util.<Glaze>typeUntypedList(dbHelper.readSingle(type,dbHelper.CCN_NAME,name)));
        }

        // create view for each glaze
        numGlazes = 0;
        for (ArrayList<Glaze> gList : glazesWithVersions) {
            // init individual view
            View singleGlaze = getLayoutInflater().inflate(R.layout.list_item_glaze_single,list,false);
            singleGlaze.setId(numGlazes);
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
            date.setText(Util.getShortDate(rootGlaze.getEditedDateRaw()));
            if (currentGlaze.getImageUri() != Uri.EMPTY) testTileImage.setImageURI(currentGlaze.getImageUri());

            // add view to list and increment counter
            list.addView(singleGlaze);
            numGlazes++;
        }
        System.out.println("Glaze list loaded");
    }

    public void populateTemplateList() {
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


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart called");
        if (dbHelper == null) {
            glazeDbLoader.execute("");
        }
        else {
            populateGlazeList(Storable.Type.SINGLE);
            populateTemplateList();
        }
    }

    /*
    @Override
    public void onRestart() {
        super.onRestart();
        System.out.println("onRestart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume called");
    }
    */


}
