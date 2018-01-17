package nh.glazelog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import nh.glazelog.database.DbHelper;
import nh.glazelog.glaze.Glaze;

/**
 * Created by Nick Hansen on 10/18/2017.
 */

public class VersionPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Glaze> glazeVersions;
    private int currentPosition = -1;
    private static DbHelper dbHelper;

    public VersionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public VersionPagerAdapter (FragmentManager fm, Activity parentActivity, ArrayList<Glaze> glazeVersions) {
        super(fm);
        this.glazeVersions = glazeVersions;
        dbHelper = DbHelper.getSingletonInstance(parentActivity);
    }


    @Override
    public Fragment getItem(int position) {
        if (position >= getCount()-1) {
            return new BlankFragment();
        }
        else {
            VersionFragment version = new VersionFragment();
            Bundle args = new Bundle();
            args.putParcelable(KeyValues.KEY_GLAZE_VERSION, glazeVersions.get(position));
            args.putInt(KeyValues.KEY_GLAZE_VERSION_NUMBER, position);
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

    // when Adapter.notifyDataSetChanged() is called,
    // the ViewPager interrogates the adapter to determine what has changed in terms of positioning.
    // We override this method to say that everything has changed, so reprocess all your view positioning.
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public void addPage() {
        Glaze newGlaze = new Glaze(glazeVersions.get(glazeVersions.size()-1));
        newGlaze.setName(glazeVersions.get(0).getName());
        newGlaze.setVersionNotes("");
        newGlaze.setNotes("");
        dbHelper.writeVersion(newGlaze);
        glazeVersions.add(newGlaze);
        notifyDataSetChanged();
    }

    public void deletePage(Glaze g) {
        dbHelper.delete(glazeVersions.remove(glazeVersions.indexOf(g)),false);
        notifyDataSetChanged();
    }

}
