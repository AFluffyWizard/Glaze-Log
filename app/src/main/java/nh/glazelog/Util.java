package nh.glazelog;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.ArrayList;

import nh.glazelog.database.DBHelper;
import nh.glazelog.database.ParseFromString;
import nh.glazelog.glaze.Cone;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.Ingredient;
import nh.glazelog.glaze.IngredientQuantity;
import nh.glazelog.glaze.RampHold;

/**
 * Created by Nick Hansen on 9/29/2017.
 */

public class Util {

    public static String[] stringToArray(String s, String separator) {
        ArrayList<String> strings = new ArrayList<>();
        while (s.indexOf(separator) != -1) {
            strings.add(s.substring(0,s.indexOf(separator)));
            s = s.substring(s.indexOf(separator)+1, s.length());
        }
        return strings.toArray(new String[strings.size()]);
    }

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

    public static void fixTables (TableLayout... tables) {
        for (TableLayout tb : tables) tb.requestLayout();
    }

    public static <T extends Object> void setSpinnerSelection (Spinner spinner, Class<T> o) {
        int itemPos = ((ArrayAdapter<T>)spinner.getAdapter()).getPosition((T)o);
        spinner.setSelection(itemPos);
    }
    public static <T extends Enum> void setSpinnerSelection (Spinner spinner, Enum o) {
        int itemPos = ((ArrayAdapter<T>)spinner.getAdapter()).getPosition((T)o);
        spinner.setSelection(itemPos);
    }

    public static <T> ArrayList<T> typeUntypedList (ArrayList<?> untyped) {
        ArrayList<T> typed = new ArrayList<>();
        for (int i = 0; i < untyped.size(); i++) {
            typed.add((T) untyped.get(i));
        }
        return typed;
    }

    /*
    public static String toLongString (Object... data) {
        String longString = "";
        for (int i = 0; i < data.length; i++) {
            longString += data[i] + DBHelper.LONG_SEP;
        }
        return longString;
    }

    public static String toLongString (ArrayList<Object> data) {
        String longString = "";
        for (int i = 0; i < data.size(); i++) {
            longString += data.get(i) + DBHelper.LONG_SEP;
        }
        return longString;
    }

    public static <T implements ParseFromString<?>> ArrayList<T> parseFromLongString (String s) {
        String[] separate = Util.stringToArray(s, DBHelper.LONG_SEP);
        ArrayList<T> list = new ArrayList<>();
        for (String str : separate)
            list.add(T.parseFromString(str));
        return list;
    }
    */

}
