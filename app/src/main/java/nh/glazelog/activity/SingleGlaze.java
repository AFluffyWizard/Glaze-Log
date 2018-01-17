package nh.glazelog.activity;

import android.content.ContentValues;
import android.content.Intent;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import nh.glazelog.DeleteDialog;
import nh.glazelog.KeyValues;
import nh.glazelog.R;
import nh.glazelog.RenameDialog;
import nh.glazelog.VersionPager;
import nh.glazelog.VersionPagerAdapter;
import nh.glazelog.database.DbHelper;
import nh.glazelog.glaze.*;

public class SingleGlaze extends AppCompatActivity {

    ArrayList<Glaze> glaze;
    Glaze rootGlaze;
    Glaze currentGlaze;
    DbHelper dbHelper;
    VersionPager versionPager;

    private RenameDialog renameDialog;
    private DeleteDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_glaze);
        dbHelper = DbHelper.getSingletonInstance(getBaseContext());

        Intent intent = getIntent();
        if (intent.hasExtra(KeyValues.KEY_ITEM_FROM_LIST))
            glaze = intent.getParcelableArrayListExtra(KeyValues.KEY_ITEM_FROM_LIST);
        else {
            glaze = new ArrayList<>();
            String newGlazeName = intent.getStringExtra(KeyValues.KEY_ITEM_NEWNAME);
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

        deleteDialog = new DeleteDialog(this, new DeleteDialog.Action() {
            @Override
            public void action() {
                dbHelper.delete(rootGlaze,true);
                finish();
            }
        });


        // android:focusableInTouchMode="true" in the lowest layout (ScrollView here)
        // and the line below prevent the EditText fields from automatically being focused.
        // This somehow also fixed the view scrolling up upon adding a new row to any table
        findViewById(R.id.baseScrollView).requestFocus();

        // enables the action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle(rootGlaze.getName());
        ab.show();


        // init the version pager
        versionPager = (VersionPager) findViewById(R.id.versionPager);
        PagerAdapter versionPagerAdapter = new VersionPagerAdapter(getSupportFragmentManager(), this, glaze);
        versionPager.setAdapter(versionPagerAdapter);
        versionPager.setCurrentItem(0);
        versionPager.setCurrentItem(versionPager.getAdapter().getCount()-2,true);

    }

    // these two enable each versionFragment to handle activity results on their own (Used primarily for pictures)
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
                finish();
                return true;
            case R.id.action_copy:
                dbHelper.write(new Glaze(glaze.get(versionPager.getCurrentItem())));
                finish();
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
}
