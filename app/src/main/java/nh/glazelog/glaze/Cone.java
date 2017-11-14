package nh.glazelog.glaze;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by Nick Hansen on 8/21/2017.
 */

public enum Cone {
    NONE ("N/A",0,0),
    C022 ("022",1112,600),
    C021 ("021",1137,614),
    C020 ("020",1175,635),
    C019 ("019",1261,683),
    C018 ("018",1323,717),
    C017 ("017",1377,747),
    C016 ("016",1458,792),
    C015 ("015",1479,804),
    C014 ("014",1540,838),
    C013 ("013",1566,852),
    C012 ("012",1623,884),
    C011 ("011",1641,894),
    C010 ("010",1641,894),
    C09 ("09",1693,923),
    C08 ("08",1751,955),
    C07 ("07",1803,984),
    C06 ("06",1830,999),
    C05 ("05",1915,1046),
    C04 ("04",1940,1060),
    C03 ("03",2014,1101),
    C02 ("02",2048,1120),
    C01 ("01",2079,1137),
    C1 ("1",2109,1154),
    C2 ("2",2124,1162),
    C3 ("3",2134,1168),
    C4 ("4",2167,1186),
    C5 ("5",2185,1196),
    C6 ("6",2232,1222),
    C7 ("7",2264,1240),
    C8 ("8",2305,1268),
    C9 ("9",2336,1280),
    C10 ("10",2381,1305),
    C11 ("11",2399,1315),
    C12 ("12",2419,1326),
    C13 ("13",2455,1346),
    C14 ("14",1366,2491),
    C15 ("15",2608,1431),
    C16 ("16",2683,1473),
    C17 ("17",2705,1485),
    C18 ("18",2743,1506);

    private final double tempF;
    private final double tempC;
    String friendlyName;
    Cone(String friendlyName, double tempF, double tempC) {
        this.friendlyName = friendlyName;
        this.tempF = tempF;
        this.tempC = tempC;
    }

    public double getTempF () {
        return tempF;
    }
    public double getTempC () {
        return tempC;
    }

    public String toString() {
        return "\u0394" + friendlyName;
    }

    /*Thank the gods of Stack Overflow*/
    private static final Map<String, Cone> valueMap;
    static {
        final Map<String, Cone> initMap = new HashMap<String, Cone>();
        for (final Cone e : Cone.values())
            initMap.put(e.toString(),e);
        valueMap = initMap;
    }

    public static Cone getEnum(String abbr) {
        if(!valueMap.containsKey(abbr)) {
            throw new IllegalArgumentException("Unknown String Value: " + abbr);
        }
        return valueMap.get(abbr);
    }
    /*Thank the gods of Stack Overflow*/

    public static Cone getClosestConeF (double tempF) {
        Cone lowerBound = Cone.C022;
        Cone upperBound = Cone.C18;
        Cone[] allCones = Cone.values();
        for (int i = 0; i < allCones.length; i++) {
            if (allCones[i].getTempF()-tempF > 0) {
                upperBound = allCones[i];
                lowerBound = allCones[i-1];
                break;
            }
        }

        if (Math.abs(upperBound.getTempF()-tempF) > Math.abs(lowerBound.getTempF()-tempF))
            return lowerBound;
        else return upperBound;
    }

    public static Cone getClosestConeC (double tempC) {
        Cone lowerBound = Cone.C022;
        Cone upperBound = Cone.C18;
        Cone[] allCones = Cone.values();
        for (int i = 0; i < allCones.length; i++) {
            if (allCones[i].getTempC()-tempC > 0) {
                upperBound = allCones[i];
                lowerBound = allCones[i-1];
                break;
            }
        }

        if (Math.abs(upperBound.getTempC()-tempC) > Math.abs(lowerBound.getTempC()-tempC))
            return lowerBound;
        else return upperBound;
    }
}
