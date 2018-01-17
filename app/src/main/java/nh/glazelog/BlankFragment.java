package nh.glazelog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nick Hansen on 11/11/2017.
 */

public class BlankFragment extends Fragment {

    AlertDialog addVersionDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View emptyView = inflater.inflate(R.layout.empty,container,false);
        addVersionDialog = new AlertDialog.Builder(getContext()).create();
        addVersionDialog.setMessage(getString(R.string.dialog_addnewversion_text));
        addVersionDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewVersion();
            }
        });
        addVersionDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addVersionDialog.dismiss();
            }
        });
        addVersionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancelAddNewVersion();
            }
        });


        return emptyView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && addVersionDialog != null) {
            addVersionDialog.show();
        }
    }



    VersionPager versionPager;
    VersionPagerAdapter versionPagerAdapter;
    private void addNewVersion() {
        if (versionPager == null) versionPager = (VersionPager) getActivity().findViewById(R.id.versionPager);
        if (versionPagerAdapter == null) versionPagerAdapter = versionPager.getVersionPagerAdapter();
        versionPagerAdapter.addPage();
        versionPager.setCurrentItem(versionPagerAdapter.getCount()-2,true);
    }
    private void cancelAddNewVersion () {
        if (versionPager == null) versionPager = (VersionPager) getActivity().findViewById(R.id.versionPager);
        if (versionPagerAdapter == null) versionPagerAdapter = versionPager.getVersionPagerAdapter();
        versionPager.setCurrentItem(versionPagerAdapter.getCount()-2,true);
    }
}
