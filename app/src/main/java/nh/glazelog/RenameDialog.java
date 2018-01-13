package nh.glazelog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

//import android.app.AlertDialog;

/**
 * Created by Nick Hansen on 11/3/2017.
 */

public class RenameDialog extends AlertDialog{

    public interface Action {
        public void action(String newName);
    }
    Action onRename;

    /* Yo WTF
     * APPARENTLY
     * If I use a Context it throws an error saying my application needs an AppCompat theme.
     * BUT
     * If I use an Activity, it's totally fine.
     * What.
     * The.
     * Fuck.
     *
     * Nick Hansen - 1/12/18
     */
    public RenameDialog(@NonNull Activity parent, @NonNull final Action onRename) {
        super(parent);
        this.onRename = onRename;
        setTitle(R.string.app_name);
        final View renameView = View.inflate(parent,R.layout.dialog_new_name,null);
        setCancelable(true);
        setView(renameView);

        setButton(DialogInterface.BUTTON_POSITIVE, "Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String newName = ((TextView)renameView.findViewById(R.id.newNameField)).getText().toString().trim();
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
