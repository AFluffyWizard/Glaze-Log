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
 *
 * See RenameDialog for notes regarding my frustration with this fucking class
 *
 * Nick Hansen - 1/12/18
 */

public class DeleteDialog extends AlertDialog{

    public interface Action {
        public void action();
    }
    Action onDelete;

    public DeleteDialog(@NonNull Activity parent, @NonNull final Action onDelete) {
        super(parent);
        this.onDelete = onDelete;
        setTitle(R.string.app_name);
        setMessage(parent.getString(R.string.dialog_delete_text) + "\n" + parent.getString(R.string.dialog_delete_warning));
        setCancelable(true);

        setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onDelete.action();
            }
        });
        setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    /* EXAMPLE onDelete()

        dbHelper.delete(rootGlaze,true);
        navigateUp();

     */

}
