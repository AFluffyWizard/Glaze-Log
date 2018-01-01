package nh.glazelog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nick Hansen on 11/11/2017.
 */

public class BlankFragment extends Fragment {

    ConfirmDialog confirmAddVersionDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View empty = inflater.inflate(R.layout.empty,container,false);
        confirmAddVersionDialog = new ConfirmDialog(getContext(), false, "Would you like to create a new version?",
                new ConfirmDialog.Action() {
                    @Override
                    public void action() {
                        addNewVersion();
                    }
                });
        confirmAddVersionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelAddNewVersion();
            }
        });
        return empty;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && confirmAddVersionDialog != null) {
            confirmAddVersionDialog.show();
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
