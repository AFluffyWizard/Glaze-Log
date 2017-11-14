package nh.glazelog.glaze;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick Hansen on 9/8/2017.
 */

public enum Atmosphere {
    NONE ("N/A"),
    OXIDATION ("Oxidation"),
    NEUTRAL ("Neutral"),
    REDUCTION ("Reduction"),
    SODA ("Soda/Salt"),
    WOOD ("Wood"),
    RAKU ("Raku"),
    LUSTER ("Luster");

    private String friendlyName;

    Atmosphere(String name) {
        friendlyName = name;
    }

    /*Thank the gods of Stack Overflow*/
    private static final Map<String, Atmosphere> valueMap;
    static {
        final Map<String, Atmosphere> initMap = new HashMap<String, Atmosphere>();
        for (final Atmosphere e : Atmosphere.values())
            initMap.put(e.toString(),e);
        valueMap = initMap;
    }

    public static Atmosphere getEnum(String strVal) {
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
