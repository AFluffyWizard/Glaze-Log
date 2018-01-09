package nh.glazelog;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import nh.glazelog.database.DbHelper;

/**
 * Created by Nick Hansen on 11/3/2017.
 */

public class RenameDialog extends AlertDialog {

    public interface Action {
        public void action(String newName);
    }

    public RenameDialog(@NonNull Context context, @NonNull final Action onRename) {
        super(context);
        setTitle(R.string.app_name);
        final View newNameView = View.inflate(context.getApplicationContext(),R.layout.dialog_new_name,null);
        setCancelable(true);
        setView(newNameView);

        setButton(DialogInterface.BUTTON_POSITIVE, "Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String newName = ((TextView)newNameView.findViewById(R.id.newNameEditText)).getText().toString();
                onRename.action(newName);
            }
        });
        setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cancel();
            }
        });
    }

    /* EXAMPLE onRename()

    TextView glazeName = (TextView) findViewById(R.id.glazeNameTextView);
    glazeName.setText(newName);
    ContentValues newNameCv = new ContentValues();
    newNameCv.put(DbHelper.CCN_NAME,newName);

     */

}
