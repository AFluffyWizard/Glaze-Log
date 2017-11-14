package nh.glazelog.glaze;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick Hansen on 9/8/2017.
 */

public enum Opacity {
    NONE ("N/A"),
    OPAQUE ("Opaque"),
    SEMI_OPAQUE ("Semi-Opaque"),
    TRANSLUCENT ("Translucent"),
    TRANSPARENT ("Transparent");

    private String friendlyName;

    Opacity(String name) {
        friendlyName = name;
    }

    /*Thank the gods of Stack Overflow*/
    private static final Map<String, Opacity> valueMap;
    static {
        final Map<String, Opacity> initMap = new HashMap<String, Opacity>();
        for (final Opacity e : Opacity.values())
            initMap.put(e.toString(),e);
        valueMap = initMap;
    }

    public static Opacity getEnum(String strVal) {
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
