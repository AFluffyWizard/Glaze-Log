package nh.glazelog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nh.glazelog.activity.GlazeList;
import nh.glazelog.glaze.FiringCycle;

import static android.view.View.GONE;

/**
 * Created by Nick Hansen on 1/9/2018.
 */

public class FiringCycleArrayAdapter<T> extends ArrayAdapter<T> {

    /*
    public FiringCycleArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public FiringCycleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FiringCycleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull T[] objects) {
        super(context, resource, objects);
    }

    public FiringCycleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FiringCycleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }
    */

    public FiringCycleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null) convertView = inflater.inflate(R.layout.spinner_item_firingcycle,parent,false);
        FiringCycle fc = (FiringCycle)getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.firingcycleName);
        name.setText(fc.getName());

        TextView subtitle = (TextView) convertView.findViewById(R.id.firingcycleSubtitle);
        subtitle.setText(fc.getSecondaryInfo(null));

        if (fc.getName().equals(getContext().getString(R.string.glaze_firingcycle_makenew))) {
            name.setTextColor(name.getHintTextColors());
            subtitle.setVisibility(GONE);
        }

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null) convertView = inflater.inflate(R.layout.spinner_item_large,parent,false);
        FiringCycle fc = (FiringCycle)getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.spinnerItem);
        name.setText(fc.getName());

        if (fc.getName().equals(getContext().getString(R.string.glaze_firingcycle_makenew))) {
            name.setTextColor(name.getHintTextColors());
            name.setText(R.string.glaze_firingcycle_spinner_hint);
        }

        return convertView;
    }

}
