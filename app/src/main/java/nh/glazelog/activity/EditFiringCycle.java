package nh.glazelog.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import nh.glazelog.KeyValues;
import nh.glazelog.DeleteDialog;
import nh.glazelog.R;
import nh.glazelog.RenameDialog;
import nh.glazelog.Util;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.FiringCycleTextSaver;
import nh.glazelog.database.Saver;
import nh.glazelog.glaze.Cone;
import nh.glazelog.glaze.FiringCycle;
import nh.glazelog.glaze.RampHold;

public class EditFiringCycle extends AppCompatActivity {

    DbHelper dbHelper;
    FiringCycle firingCycle;
    RenameDialog renameDialog;
    DeleteDialog deleteDialog;
    TableLayout firingCycleTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_firing_cycle);
        firingCycleTable = (TableLayout) findViewById(R.id.firingcycleTable);

        dbHelper = DbHelper.getSingletonInstance(getApplicationContext());

        Intent intent = getIntent();
        firingCycle = intent.getParcelableExtra(KeyValues.KEY_FIRING_CYCLE);
        if (firingCycle == null){
            String name = intent.getStringExtra(KeyValues.KEY_NAME);
            firingCycle = new FiringCycle(name);
        }

        setSupportActionBar((Toolbar) findViewById(R.id.actionbarDefault));
        ActionBar ab = getSupportActionBar();
        ab.setTitle(firingCycle.getName());
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();

        renameDialog = new RenameDialog(this, new RenameDialog.Action() {
            @Override
            public void action(String newName) {
                getSupportActionBar().setTitle(newName);
                ContentValues newNameCv = new ContentValues();
                newNameCv.put(DbHelper.CCN_NAME,newName);
                dbHelper.append(firingCycle,newNameCv);
            }
        });

        deleteDialog = new DeleteDialog(this, "", new DeleteDialog.Action() {
            @Override
            public void action() {
                dbHelper.delete(firingCycle, false);
                Util.navigateUp(getParent());
            }
        });



        for (RampHold rh : firingCycle.getRampHolds()) {
            addFiringCycleRow(rh);
        }

        Button addLineButton = (Button) findViewById(R.id.firingCycleAddLineButton);
        addLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFiringCycleRow(null);
            }
        });








    }


    private void addFiringCycleRow(RampHold rh) {
        final TableRow firingCycleRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_firingcycle,null);
        final DeleteDialog deleteRowDialog = new DeleteDialog(this, "row",
                new DeleteDialog.Action() {
                    @Override
                    public void action() {
                        firingCycleTable.removeView(firingCycleRow);
                        Saver.firingCycleRampHold(getApplicationContext(),firingCycle,firingCycleTable);
                    }
                });

        final EditText temperature = (EditText) firingCycleRow.findViewById(R.id.temperatureEditText);
        final EditText rate = (EditText) firingCycleRow.findViewById(R.id.rateEditText);
        final EditText hold = (EditText) firingCycleRow.findViewById(R.id.holdEditText);
        ImageView deleteRow = (ImageView) firingCycleRow.findViewById(R.id.deleteRowImageView);
        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRowDialog.show();
            }
        });

        if (rh == null) {
            temperature.setText("");
            rate.setText("");
            hold.setText("");
        }
        else {
            temperature.setText(Double.toString(rh.getTemperature()));
            rate.setText(Double.toString(rh.getRate()));
            hold.setText(Double.toString(rh.getHold()));
        }

        firingCycleRow.postDelayed(new Runnable() {
            @Override
            public void run() {
                temperature.addTextChangedListener(new FiringCycleTextSaver(getApplicationContext(),firingCycle,firingCycleTable));
                rate.addTextChangedListener(new FiringCycleTextSaver(getApplicationContext(),firingCycle,firingCycleTable));
                hold.addTextChangedListener(new FiringCycleTextSaver(getApplicationContext(),firingCycle,firingCycleTable));
            }
        }, Util.CONST_DELAY_ADD_LISTENER);

        firingCycleTable.addView(firingCycleRow);
    }


    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        Cone closestCone = Cone.NONE;
        ArrayList<RampHold> rhList = firingCycle.getRampHolds();
        if (rhList.size() != 0)
            closestCone = Cone.getClosestConeF(RampHold.getHighestTemp(rhList));
        menu.findItem(R.id.text_cone).setTitle(closestCone.toString());
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.actionbuttons_firingcycle,menu);
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
