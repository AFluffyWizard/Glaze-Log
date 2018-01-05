package nh.glazelog.glaze;

import android.net.Uri;
import java.util.ArrayList;

import nh.glazelog.database.Storable;

/**
 * Created by Nick Hansen on 1/4/2018.
 */

public interface Listable {
    public String getName();
    public String getSecondaryInfo(ArrayList<?> itemInfo);
    public Uri getImageUri();
    public String getDateEditedRaw();
}
