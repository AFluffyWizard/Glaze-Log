package nh.glazelog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Nick Hansen on 11/3/2017.
 */

public class DeleteDialog extends AlertDialog {

    Context context;
    public interface Action {
        public void action();
    }
    Action onDeleteAction;

    public DeleteDialog(@NonNull Context context, @NonNull String nameOfItem, @NonNull final Action onDelete) {
        super(context);
        this.context = context;
        setTitle(R.string.app_name);
        setOnDeleteAction(onDelete);
        setNameOfDeletedItem(nameOfItem);
        setCancelable(true);

        setButton(DialogInterface.BUTTON_POSITIVE, "Rename", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onDeleteAction.action();
            }
        });
        setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                cancel();
            }
        });
    }

    public void setNameOfDeletedItem(String nameOfItem) {
        String text = context.getString(R.string.dialog_delete_text) + nameOfItem + "?\n" +
                context.getString(R.string.dialog_delete_warning);
        setMessage(text);
    }

    public void setOnDeleteAction (Action onDelete) {
        onDeleteAction = onDelete;
    }

    /* EXAMPLE onRename()

        getSupportActionBar().setTitle(newName);
        ContentValues newNameCv = new ContentValues();
        newNameCv.put(DbHelper.CCN_NAME,newName);
        dbHelper.append(firingCycle,newNameCv);

     */

}
