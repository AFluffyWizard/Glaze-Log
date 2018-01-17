package nh.glazelog.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nh.glazelog.DeleteDialog;
import nh.glazelog.KeyValues;
import nh.glazelog.R;
import nh.glazelog.RenameDialog;
import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;
import nh.glazelog.glaze.FiringCycle;

public class BasicActivityTemplate extends AppCompatActivity {

    DbHelper dbHelper;
    FiringCycle firingCycle;
    RenameDialog renameDialog;
    DeleteDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_firing_cycle);

        dbHelper = DbHelper.getSingletonInstance(getApplicationContext());

        setSupportActionBar((Toolbar) findViewById(R.id.actionbarDefault));
        ActionBar ab = getSupportActionBar();
        ab.setTitle(firingCycle.getName());
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();

        Intent intent = getIntent();
        firingCycle = intent.getParcelableExtra(KeyValues.KEY_ITEM_FROM_LIST);
        if (firingCycle == null){
            String name = intent.getStringExtra(KeyValues.KEY_ITEM_NEWNAME);
            firingCycle = new FiringCycle(name);
        }

        renameDialog = new RenameDialog(this, new RenameDialog.Action() {
            @Override
            public void action(String newName) {
                getSupportActionBar().setTitle(newName);
                ContentValues newNameCv = new ContentValues();
                newNameCv.put(DbHelper.CCN_NAME,newName);
                dbHelper.append(firingCycle,newNameCv);
            }
        });

        deleteDialog = new DeleteDialog(this, new DeleteDialog.Action() {
            @Override
            public void action() {
                dbHelper.delete(firingCycle, false);
                Util.navigateUp(getParent());
            }
        });


        









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
                Util.navigateUp(this);
                return true;
            case R.id.action_copy:
                dbHelper.write(new FiringCycle(firingCycle));
                Util.navigateUp(this);
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
}
