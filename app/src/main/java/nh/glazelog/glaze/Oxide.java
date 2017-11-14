package nh.glazelog.glaze;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick Hansen on 9/22/2017.
 */

public enum Oxide {
    SODIUM ("Sodium Oxide", "Na2O",62.0,0.387,Family.FLUX),
    POTASSIUM ("Potassium Oxide", "K2O",94.2,0.331,Family.FLUX),
    LITHIUM ("Lithium Oxide", "Li2O",29.8,0.068,Family.FLUX),
    CALCIUM_CaO ("Calcium Oxide", "CaO",56.1,0.148,Family.FLUX),
    //CALCIUM_CaCO3 ("Calcium Carbonate", "CaCO3"),
    MAGNESIUM ("Magnesium Oxide", "MgO",40.3,0.026,Family.FLUX),
    BARIUM ("Barium Oxide", "BaO",153.3,0.129,Family.FLUX),
    STRONTIUM ("Strontium Oxide", "SrO",103.6,0.130,Family.FLUX),
    ZINC ("Zinc Oxide", "ZnO",81.4,0.094,Family.FLUX),
    BORON ("Boron Oxide", "B2O3",69.6,0.031,Family.GLASS),
    PHOSPHORUS ("Phosphorus Pentoxide", "P2O5",141.9,0,Family.GLASS),
    SILICON ("Silicon Dioxide", "SiO2",60.1,0.035,Family.GLASS),
    ALUMINUM ("Aluminum Oxide", "Al2O3",102.00,0.063,Family.INTERMEDIATE),
    TIN ("Tin Oxide", "SnO2",150.710,0.020,Family.OPACIFIER),
    TITANIUM ("Titanium Oxide", "TiO2",79.9,0.144,Family.GLASS),
    ZIRCONIUM_ZrO ("Zirconium Oxide", "ZrO",107.200,0.020,Family.OPACIFIER),
    ZIRCONIUM_ZrO2 ("Zirconium Dioxide", "ZrO2",123.220,0.020,Family.OPACIFIER),
    CHROMIUM ("Chromium Oxide", "Cr2O3",152.0,0,Family.COLORANT),
    COBALT_CoO ("Cobalt Oxide", "CoO",74.920,0,Family.COLORANT),
    //COBALT_Co2O3 ("Cobalt Oxide", "Co2O3"),
    //COBALT_Co3O4 ("Cobalt Oxide", "Co3O4"),
    //COBALT_CoCO3 ("Cobalt Carbonate", "CoCO3"),
    COPPER_CuO ("Copper Oxide", "CuO",79.540,0,Family.COLORANT),
    COPPER_Cu2O ("Copper Oxide", "Cu2O",143.0,0,Family.COLORANT),
    //COPPER_Cu4O ("Copper Oxide", "Cu4O"),
    //COPPER_CuCO3 ("Copper Carbonate", "CuCO3"),
    IRON_FeO ("Iron Oxide", "FeO",81.8,0,Family.COLORANT),
    IRON_Fe2O3 ("Iron Oxide", "Fe2O3",160.0,0.125,Family.COLORANT),
    //IRON_Fe3O4 ("Iron Oxide", "Fe3O4"),
    //IRON_Fe2O3_H2O ("Iron Oxide", "Fe2O3*H2O"),
    MANGANESE_MnO ("Manganous Oxide", "MnO",70.9,0.050,Family.COLORANT),
    MANGANESE_MnO2 ("Manganese Dioxide", "MnO2",86.9,0.050,Family.COLORANT),
    //MANGANESE_MnCO3 ("Manganese Carbonate", "MnCO3"),
    NICKEL_NiO ("Nickel Oxide", "NiO",74.7,0,Family.COLORANT),
    //NICKEL_NiO2 ("Nickel Oxide", "NiO2"),
    //NICKEL_Ni2O3 ("Nickel Oxide", "Ni2O3"),
    //NICKEL_Ni3O4 ("Nickel Oxide", "Ni3O4"),
    //PRASEODYMIUM_PrO2 ("Praesodymium Oxide", "PrO2"),
    //PRASEODYMIUM_Pr2O3 ("Praesodymium Oxide", "Pr2O3"),
    VANADIUM ("Vanadium Pentoxide", "V2O5",181.9,0,Family.COLORANT),
    LEAD ("Lead Oxide", "PbO",223.2,0.083,Family.FLUX);

    public enum Family{GLASS,INTERMEDIATE,FLUX,COLORANT,OPACIFIER}
    String abbreviation;
    String friendlyName;
    //TODO - IMPLEMENT
    double molecularMass;
    double cole; // Coefficient of Linear Expansion
    //TODO - ENUM FOR TYPE OF METAL ON PERIODIC TABLE
    Family family;

    Oxide(String friendlyName, String abbreviation, double molecularMass, double cole, Family family) {
        this.friendlyName = friendlyName;
        this.abbreviation = abbreviation;
        this.molecularMass = molecularMass;
        this.cole = cole;
        this.family = family;
    }

    /*Thank the gods of Stack Overflow*/
    private static final Map<String, Oxide> valueMap;
    static {
        final Map<String, Oxide> initMap = new HashMap<String, Oxide>();
        for (final Oxide e : Oxide.values())
            initMap.put(e.abbreviation,e);
        valueMap = initMap;
    }

    public static Oxide getEnum(String abbr) {
        if(!valueMap.containsKey(abbr)) {
            throw new IllegalArgumentException("Unknown String Value: " + abbr);
        }
        return valueMap.get(abbr);
    }
    /*Thank the gods of Stack Overflow*/

    public String toString () {
        return friendlyName;
    }

    public String getAbbreviation () {
        return abbreviation;
    }

}
