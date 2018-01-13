package nh.glazelog.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import nh.glazelog.ConfirmDialog;
import nh.glazelog.DeleteDialog;
import nh.glazelog.KeyValues;
import nh.glazelog.R;
import nh.glazelog.RenameDialog;
import nh.glazelog.Util;
import nh.glazelog.VersionPager;
import nh.glazelog.VersionPagerAdapter;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.SimpleTextSaver;
import nh.glazelog.database.SimpleSpinnerSaver;
import nh.glazelog.glaze.*;

public class SingleGlaze extends AppCompatActivity {

    ArrayList<Glaze> glaze;
    Glaze rootGlaze;
    Glaze currentGlaze;
    DbHelper dbHelper;
    VersionPager versionPager;

    private RenameDialog renameDialog;
    private DeleteDialog deleteDialog;

    private static final int WAIT_ADD_LISTENER = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_glaze);
        dbHelper = DbHelper.getSingletonInstance(getBaseContext());

        Intent intent = getIntent();
        glaze = intent.getParcelableArrayListExtra(KeyValues.KEY_GLAZE_SINGLE);
        if (glaze == null) {
            glaze = new ArrayList<>();
            // TODO - CREATE SOME SYSTEM TO ENSURE THERE ARE NO CONFLICTS IN NAMING (no other items with that name)
            String newGlazeName = intent.getStringExtra(KeyValues.KEY_NAME);
            glaze.add(new Glaze(newGlazeName));
            dbHelper.write(glaze.get(0));
        }
        rootGlaze = glaze.get(0);
        currentGlaze = glaze.get(glaze.size()-1);

        renameDialog = new RenameDialog(this, new RenameDialog.Action() {
            @Override
            public void action(String newName) {
                if (!newName.equals("")) {
                    getSupportActionBar().setTitle(newName);
                    ContentValues newNameCv = new ContentValues();
                    newNameCv.put(DbHelper.CCN_NAME,newName);
                    dbHelper.appendAllVersions(glaze.get(0),newNameCv);
                }
            }
        });

        deleteDialog = new DeleteDialog(this, "glaze", new DeleteDialog.Action() {
            @Override
            public void action() {
                dbHelper.delete(rootGlaze,true);
                navigateUp();
            }
        });


        // android:focusableInTouchMode="true" in the lowest layout (ScrollView here)
        // and the line below prevent the EditText fields from automatically being focused.
        // This somehow also fixed the view scrolling up upon adding a new row to any table
        findViewById(R.id.baseScrollView).requestFocus();

        // enables the action bar
        setSupportActionBar((Toolbar) findViewById(R.id.actionbarDefault));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle(rootGlaze.getName());
        ab.show();

        // fills in the version-independent fields
        final Spinner finishSpinner = (Spinner) findViewById(R.id.finishSpinner);
        finishSpinner.setAdapter(new ArrayAdapter<Finish>(this, R.layout.spinner_item_small, Finish.values()));
        Util.setSpinnerSelection(finishSpinner,rootGlaze.getFinish());
        finishSpinner.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishSpinner.setOnItemSelectedListener(new SimpleSpinnerSaver(getThis(),rootGlaze, DbHelper.SingleCN.FINISH,true));
            }
        },WAIT_ADD_LISTENER);

        final Spinner opacitySpinner = (Spinner) findViewById(R.id.opacitySpinner);
        opacitySpinner.setAdapter(new ArrayAdapter<Opacity>(this, R.layout.spinner_item_small, Opacity.values()));
        Util.setSpinnerSelection(opacitySpinner,rootGlaze.getOpacity());
        opacitySpinner.postDelayed(new Runnable() {
            @Override
            public void run() {
                opacitySpinner.setOnItemSelectedListener(new SimpleSpinnerSaver(getThis(),rootGlaze, DbHelper.SingleCN.OPACITY,true));
            }
        },WAIT_ADD_LISTENER);

        final Spinner atmosSpinner = (Spinner) findViewById(R.id.atmosSpinner);
        atmosSpinner.setAdapter(new ArrayAdapter<Atmosphere>(this, R.layout.spinner_item_small, Atmosphere.values()));
        Util.setSpinnerSelection(atmosSpinner,rootGlaze.getAtmos());
        atmosSpinner.postDelayed(new Runnable() {
            @Override
            public void run() {
                atmosSpinner.setOnItemSelectedListener(new SimpleSpinnerSaver(getThis(),rootGlaze, DbHelper.SingleCN.ATMOSPHERE,true));
            }
        },WAIT_ADD_LISTENER);

        final EditText clayBody = (EditText) findViewById(R.id.bodyTextField);
        clayBody.setText(rootGlaze.getClayBody());
        clayBody.postDelayed(new Runnable() {
            @Override
            public void run() {
                clayBody.addTextChangedListener(new SimpleTextSaver(getThis(),rootGlaze, DbHelper.SingleCN.CLAY_BODY,true));
            }
        },WAIT_ADD_LISTENER);
        
        final EditText application = (EditText) findViewById(R.id.applicationTextField);
        application.setText(rootGlaze.getApplication());
        application.postDelayed(new Runnable() {
            @Override
            public void run() {
                application.addTextChangedListener(new SimpleTextSaver(getThis(),rootGlaze, DbHelper.SingleCN.APPLICATION,true));
            }
        },WAIT_ADD_LISTENER);



        // init the version pager
        versionPager = (VersionPager) findViewById(R.id.versionPager);
        PagerAdapter versionPagerAdapter = new VersionPagerAdapter(getSupportFragmentManager(), this, glaze);
        versionPager.setAdapter(versionPagerAdapter);
        versionPager.setCurrentItem(0);
        versionPager.setCurrentItem(versionPager.getAdapter().getCount()-2,true);

    }

    // these two enable each versionFragment to handle the results on their own (Used for pictures)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.actionbuttons_single_glaze,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: break;
            case android.R.id.home:
                navigateUp();
                return true;
            case R.id.action_copy:
                DbHelper.getSingletonInstance(getApplicationContext()).write(new Glaze(glaze.get(versionPager.getCurrentItem())));
                navigateUp();
                return true;
            case R.id.action_rename:
                renameDialog.show();
                return true;
            case R.id.action_delete:
                deleteDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        Cone closestCone = Cone.NONE;
        if (rootGlaze.getFiringCycle().getRampHolds().size() != 0)
            closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(glaze.get(glaze.size()-1).getFiringCycle().getRampHolds()));
        menu.findItem(R.id.text_cone).setTitle(closestCone.toString());
        return true;
    }

    private void navigateUp() {
        NavUtils.navigateUpFromSameTask(this);
        //NavUtils.navigateUpTo(this,null);
        //this.finish();
    }

    private SingleGlaze getThis() {
        return this;
    }
}
