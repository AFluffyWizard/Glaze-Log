package nh.glazelog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import java.text.ParseException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import nh.glazelog.database.DbHelper;
import nh.glazelog.glaze.Listable;

/**
 * Created by Nick Hansen on 9/29/2017.
 */

public class Util {

    public static final int CONST_DELAY_ADD_LISTENER = 50;

    public static String[] stringToArray(String s, String separator) {
        ArrayList<String> strings = new ArrayList<>();
        while (s.indexOf(separator) != -1) {
            strings.add(s.substring(0,s.indexOf(separator)));
            s = s.substring(s.indexOf(separator)+1, s.length());
        }
        return strings.toArray(new String[strings.size()]);
    }

    public static <T> ArrayList<T> typeUntypedList (ArrayList<?> untyped) {
        ArrayList<T> typed = new ArrayList<>();
        for (int i = 0; i < untyped.size(); i++) {
            typed.add((T) untyped.get(i));
        }
        return typed;
    }

    public static <T extends Listable> String getMostRecentEditDate (ArrayList<T> itemList) {
        ArrayList<Date> dateList = new ArrayList<>();
        for (Listable item : itemList) {
            try {
                dateList.add(new SimpleDateFormat("yyyyMMdd_HHmmss").parse(item.getDateEditedRaw()));
            } catch (ParseException e) {e.printStackTrace();};
        }
        Collections.sort(dateList,Collections.<Date>reverseOrder());
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(dateList.get(0));
    }

    // should really be replaced, but it works for now
    public static String getFriendlyDate(String d, boolean includeTime) {
        final String year = d.substring(0,4);
        String month = d.substring(4,6);
        final String day = d.substring(6,8);
        final String hour = d.substring(9,11);
        final String minute = d.substring(11,13);
        final String seconds = d.substring(13,15);

        switch(month) {
            default: break;
            case "01": month = "Jan"; break;
            case "02": month = "Feb"; break;
            case "03": month = "Mar"; break;
            case "04": month = "Apr"; break;
            case "05": month = "May"; break;
            case "06": month = "Jun"; break;
            case "07": month = "Jul"; break;
            case "08": month = "Aug"; break;
            case "09": month = "Sept"; break;
            case "10": month = "Oct"; break;
            case "11": month = "Nov"; break;
            case "12": month = "Dec"; break;
        }

        if (includeTime) return month + " " + Integer.parseInt(day) + ", " + year + " at " + hour + ":" + minute;
        else return month + Integer.parseInt(day) + ", " + year;
    }

    // should really be replaced, but works for now
    public static String getShortDate(String d) {
        final String year = d.substring(0,4);
        final String shortYear = d.substring(2,4);
        String month = d.substring(4,6);
        final String day = d.substring(6,8);
        final String hour = d.substring(9,11);
        final String minute = d.substring(11,13);
        final String seconds = d.substring(13,15);

        return month + "/" + day + "/" + shortYear + " at " + hour + ":" + minute;
    }

    public static String getDateTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static void fixTables (TableLayout... tables) {
        for (TableLayout tb : tables) tb.requestLayout();
    }

    public static void navigateUp (Activity activity) {
        NavUtils.navigateUpFromSameTask(activity);
    }

    public static Uri getVersionSpecificUri(Context context, File file) {
        Uri u;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            u = Uri.fromFile(file);
            System.out.println("Uses old Uri format: " + u);
        } else {
            u = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            System.out.println("Uses Android N+ Uri format: " + u);
        }
        return u;
    }

    public static boolean checkStoragePermissions(Activity activity) {
        //final boolean checkReadPermission = ContextCompat.checkSelfPermission(activity,
        //        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        final boolean checkWritePermission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return checkWritePermission;
    }

    public static final int PERMISSION_USE_INTERNAL_STORAGE = 6564;
    public static void requestStoragePermissions(Activity activity) {
        //Manifest.permission.READ_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_USE_INTERNAL_STORAGE);
    }

    public static <T extends Object> boolean setSpinnerSelection (Spinner spinner, Object o) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().equals(o.toString())) {
                spinner.setSelection(i);
                return true;
            }
        }
        System.out.println("COULD NOT FIND OBJ \"" + o + "\" IN SPINNER");
        return false;
    }

    public static <T extends Enum> void setSpinnerSelection (Spinner spinner, Enum o) {
        int itemPos = ((ArrayAdapter<T>)spinner.getAdapter()).getPosition((T)o);
        //System.out.println("INDEX OF " + o + " IN SPINNER: " + itemPos);
        spinner.setSelection(itemPos);
    }

}
