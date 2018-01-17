package nh.glazelog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Created by Nick Hansen on 1/12/2018.
 *
 * https://stackoverflow.com/a/11227847/7311664
 */

public class AlwaysSelectSpinner extends android.support.v7.widget.AppCompatSpinner {

    public AlwaysSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (getOnItemSelectedListener() != null)
            getOnItemSelectedListener().onItemSelected(null, null, position, 0);
    }
}