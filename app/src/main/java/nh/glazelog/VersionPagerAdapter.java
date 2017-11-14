package nh.glazelog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import nh.glazelog.database.DBHelper;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.GlazeTemplate;

/**
 * Created by Nick Hansen on 10/18/2017.
 */

public class VersionPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Glaze> glazeVersions = new ArrayList<Glaze>();
    private Activity parentActivity;
    public static final String KEY_GLAZE_VERSION = "nh.glazelog.GLAZE_VERSION";
    public static final String KEY_GLAZE_VERSION_NUMBER = "nh.glazelog.GLAZE_VERSION_NUMBER";
    private int currentPosition = -1;

    public VersionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public VersionPagerAdapter (FragmentManager fm, Activity parentActivity, ArrayList<Glaze> glazeVersions) {
        super(fm);
        this.parentActivity = parentActivity;
        this.glazeVersions = glazeVersions;
    }


    @Override
    public Fragment getItem(int position) {
        if (position >= getCount()-1) {
            return new BlankFragment();
        }
        else {
            Fragment version = new VersionFragment();
            Bundle args = new Bundle();
            args.putParcelable(KEY_GLAZE_VERSION, glazeVersions.get(position));
            args.putInt(KEY_GLAZE_VERSION_NUMBER, position);
            version.setArguments(args);
            return version;
        }
    }

    @Override
    public int getCount() {
        return glazeVersions.size()+1;
    }



    /*     StackOverflow come thruuuuu     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != currentPosition) {
            Fragment fragment = (Fragment) object;
            VersionPager pager = (VersionPager) container;
            if (fragment != null && fragment.getView() != null) {
                currentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }
    /*     StackOverflow come thruuuuu     */

    /*--------------------ADD AND REMOVE PAGES--------------------*/
    DBHelper dbHelper = DBHelper.getSingletonInstance(parentActivity);

    // when Adapter.notifyDataSetChanged() is called,
    // the ViewPager interrogates the adapter to determine what has changed in terms of positioning.
    // We override this method to say that everything has changed, so reprocess all your view positioning.
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public void addPage() {
        GlazeTemplate newPageTemplate = new GlazeTemplate(glazeVersions.get(glazeVersions.size()-1),"");
        Glaze newGlaze = new Glaze(newPageTemplate);
        newGlaze.setName(glazeVersions.get(0).getName());
        dbHelper.write(newGlaze);
        glazeVersions.add(newGlaze);
        notifyDataSetChanged();
    }

    // it is assumed the index is valid
    public void deletePage(int index) {
        dbHelper.delete(glazeVersions.remove(index),false);
        notifyDataSetChanged();
    }

    public void deletePage(Glaze g) {
        dbHelper.delete(glazeVersions.remove(glazeVersions.indexOf(g)),false);
        notifyDataSetChanged();
    }

}
