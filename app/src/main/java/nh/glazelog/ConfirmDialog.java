package nh.glazelog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by Nick Hansen on 11/3/2017.
 */

public class ConfirmDialog extends AlertDialog {

    public interface Action {
        public void action();
    }
    private Action onConfirm;
    private Action getOnConfirm() {return onConfirm;}
    private void onConfirm() {onConfirm.action();}

    // onConfirm and onCancel are switched in the constructor because normally
    // onCancel will be null, so it makes instantiating the class easier.
    public ConfirmDialog(@NonNull Context context, boolean includePrefix, String text, @Nullable final Action onConfirm) {
        super(context);
        setTitle(R.string.dialog_confirm_title);
        if (includePrefix) setMessage(getContext().getString(R.string.dialog_confirm_pretext) + text);
        else setMessage(text);
        setCancelable(true);

        this.onConfirm = onConfirm;
        setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (getOnConfirm() != null) onConfirm();
            }
        });

        setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /* Okay
                 * For some janky ass reason
                 * just calling hide() doesn't work properly.
                 * If hide() is called, and immediately after show() is called again,
                 * it would not show.
                 * However, calling cancel() then immediately calling show() works perfectly.
                 * So instead of calling hide() I call cancel() here
                 * which serves the same purpose.
                 *
                 * Nick Hansen - 11/11/17
                 */
                cancel();
            }
        });
    }


}
