package nh.glazelog.glaze;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nick Hansen on 9/8/2017.
 */

public enum Finish {
    NONE ("N/A"),
    MATTE ("Matte"),
    STONY_MATTE ("Stony Matte"),
    DRY_MATTE ("Dry Matte"),
    SEMI_MATTE ("Semi-Matte"),
    SMOOTH_MATTE ("Smooth Matte"),
    SATIN_MATTE ("Satin Matte"),
    SATIN ("Satin"),
    SEMI_GLOSSY ("Semi-Glossy"),
    GLOSSY ("Glossy");

    private String friendlyName;

    Finish(String name) {
        friendlyName = name;
    }

    /*Thank the gods of Stack Overflow*/
    private static final Map<String, Finish> valueMap;
    static {
        final Map<String, Finish> initMap = new HashMap<String, Finish>();
        for (final Finish e : Finish.values())
            initMap.put(e.toString(),e);
        valueMap = initMap;
    }

    public static Finish getEnum(String strVal) {
        if(!valueMap.containsKey(strVal)) {
            throw new IllegalArgumentException("Unknown String Value: " + strVal);
        }
        return valueMap.get(strVal);
    }
    /*Thank the gods of Stack Overflow*/

    public String toString() {
        return friendlyName;
    }
}
