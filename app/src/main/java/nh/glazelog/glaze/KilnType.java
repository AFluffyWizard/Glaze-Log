package nh.glazelog.glaze;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick Hansen on 1/5/2018.
 */

public enum KilnType {
    NONE("N/A"),
    MANUAL("Manual"),
    ELECTRIC("Electric"),
    GAS("Gas"),
    GAS_PROG("Programmable Gas"),
    OTHER("Other");

    String friendlyName;

    private KilnType(String friendlyName) {this.friendlyName = friendlyName;}

    public String toString() {return friendlyName;}

    /*Thank the gods of Stack Overflow*/
    private static final Map<String, KilnType> valueMap;
    static {
        final Map<String, KilnType> initMap = new HashMap<String, KilnType>();
        for (final KilnType e : KilnType.values())
            initMap.put(e.friendlyName,e);
        valueMap = initMap;
    }

    public static KilnType getEnum(String abbr) {
        if(!valueMap.containsKey(abbr)) {
            throw new IllegalArgumentException("Unknown String Value: " + abbr);
        }
        return valueMap.get(abbr);
    }
    /*Thank the gods of Stack Overflow*/
}
