package nh.glazelog.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import nh.glazelog.R;
import nh.glazelog.Util;
import nh.glazelog.VersionPager;
import nh.glazelog.VersionPagerAdapter;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.TextSaver;
import nh.glazelog.database.SimpleSpinnerSaver;
import nh.glazelog.glaze.*;

public class SingleGlaze extends AppCompatActivity {

    ArrayList<Glaze> glaze;
    Glaze rootGlaze;
    Glaze currentGlaze;
    DbHelper dbHelper;
    VersionPager versionPager;

    private static final int WAIT_ADD_LISTENER = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_glaze);
        dbHelper = DbHelper.getSingletonInstance(getBaseContext());

        Intent intent = getIntent();
        glaze = intent.getParcelableArrayListExtra(GlazeList.KEY_GLAZE_SINGLE);
        if (glaze == null) {
            GlazeTemplate template = intent.getParcelableExtra(GlazeList.KEY_GLAZE_TEMPLATE);
            glaze = new ArrayList<>();
            glaze.add(new Glaze(template));
            dbHelper.write(glaze.get(0));
        }
        rootGlaze = glaze.get(0);
        currentGlaze = glaze.get(glaze.size()-1);


        // android:focusableInTouchMode="true" in the lowest layout (ScrollView here)
        // and the line below prevent the EditText fields from automatically being focused.
        // This somehow also fixed the view scrolling up upon adding a new row to any table
        findViewById(R.id.baseScrollView).requestFocus();

        // enables the action bar
        setSupportActionBar((Toolbar) findViewById(R.id.glazeToolbar));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("");
        ab.show();

        // fills in the version-independent fields
        TextView glazeName = (TextView) findViewById(R.id.glazeNameTextView);
        glazeName.setText(rootGlaze.getName());

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
                clayBody.addTextChangedListener(new TextSaver(getThis(),rootGlaze, DbHelper.SingleCN.CLAY_BODY,true,false));
            }
        },WAIT_ADD_LISTENER);
        
        final EditText application = (EditText) findViewById(R.id.applicationTextField);
        application.setText(rootGlaze.getApplication());
        application.postDelayed(new Runnable() {
            @Override
            public void run() {
                application.addTextChangedListener(new TextSaver(getThis(),rootGlaze, DbHelper.SingleCN.APPLICATION,true,false));
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
                final AlertDialog renameDialog = new AlertDialog.Builder(this).create();
                renameDialog.setTitle(getString(R.string.dialog_rename_title));
                final View newNameView = View.inflate(getApplicationContext(),R.layout.dialog_new_name,null);
                renameDialog.setView(newNameView);
                renameDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Rename", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = ((TextView)newNameView.findViewById(R.id.newNameEditText)).getText().toString();
                        TextView glazeName = (TextView) findViewById(R.id.glazeNameTextView);
                        glazeName.setText(newName);
                        ContentValues newNameCv = new ContentValues();
                        newNameCv.put(DbHelper.CCN_NAME,newName);
                        dbHelper.appendAllVersions(glaze.get(0),newNameCv);
                    }
                });
                renameDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        renameDialog.hide();
                    }
                });
                renameDialog.show();
                return true;
            case R.id.action_create_template:
                final AlertDialog newTemplateDialog = new AlertDialog.Builder(this).create();
                newTemplateDialog.setTitle(R.string.dialog_newtemplate_title);
                final View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_template,null);
                newTemplateDialog.setView(dialogView);
                newTemplateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String templateName = ((EditText)dialogView.findViewById(R.id.newTemplateEditText)).getText().toString();
                        GlazeTemplate t = new GlazeTemplate(glaze.get(versionPager.getCurrentItem()),templateName);
                        dbHelper.writeTemplate(t);
                    }
                });
                newTemplateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    newTemplateDialog.hide();
                    }
                });
                newTemplateDialog.show();
                return true;
            case R.id.action_delete_glaze:
                ConfirmDialog confirmDelete = new ConfirmDialog(this, true, "to delete this glaze? WARNING: This CANNOT be undone.",
                        new ConfirmDialog.Action() {
                            @Override
                            public void action() {
                                dbHelper.delete(rootGlaze,true);
                                navigateUp();
                            }
                        });
                confirmDelete.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        Cone closestCone = Cone.NONE;
        if (rootGlaze.getFiringCycle().size() != 0)
            closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(glaze.get(glaze.size()-1).getFiringCycle()));
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