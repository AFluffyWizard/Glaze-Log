package nh.glazelog.glaze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick Hansen on 10/16/2017.
 */

public enum Ingredient {
    NONE("",new ArrayList<OxideQuantity>()),
    ALBANY_SLIP("Albany Slip", "SiO2:58:|Al2O3:14:|NA2O:0.8:|K2O:3.25:|MgO:2.2:|CaO:6:|Fe2O3:5.2:|TiO2:0.8:|"),
    ALBERTA_SLIP("Alberta Slip","SiO2:53.54:|Al2O3:15.2:|Na2O:2.2:|K2O:3.5:|MgO:3.9:|CaO:5.9:|Fe2O3:4.5:|TiO2:0.3:|"),
    ALFRED_SHALE("Alfred Shale","SiO2:57.56:|Al2O3:18.9:|Na2O:0.46:|K2O:4.41:|MgO:1.8:|CaO:0.36:|Fe2O3:8.19:|TiO2:1:|"),
    ALUMINA_HYDRATE("Alumina Hydrate","Al2O3:65.39:|"),
    ALUMINA_OXIDE("Alumina Oxide","Al2O3:100:|"),
    ALUMINUM_METALLIC("Metallic Aluminum","Al2O3:188.94:|"),
    AMADOR_BALL_CLAY("Amador Ball Clay","SiO2:54:|Al2O3:29.5:|Na2O:0.2:|K2O:0.25:|MgO:0.35:|CaO:0.2:|Fe2O3:1.9:|TiO2:1.8:|"),
    AMTALC("Amtalc","SiO2:53.5:|Al2O3:0.3:|K2O:0.3:|MgO:29.5:|CaO:3.5:|Fe2O3:0.5:|TiO2:0.1:|"),
    AP_GREEN_MISSOURI("AP Green Missouri","SiO2:52:|Al2O3:30:|Na2O:0.05:|K2O:0.2:|MgO:0.03:|CaO:0.05:|Fe2O3:1:|TiO2:1.5:|"),
    BARIUM_CARBONATE("Barium Carbonate","BaO:77.89:|"),
    BARNARD_BLACKBIRD("Barnard Blackbird Slip","SiO2:45.6:|Al2O3:15.81:|Na2O:0.03:|K2O:2.63:|MgO:1.07:|CaO:0.13:|Fe2O3:19.39:|TiO2:0.8:|"),
    BARNARD_BLACKBIRD_SUBSTITUTE("Barnard Blackbird Substitude","SiO2:30.37:|Al2O3:20.52:|Na2O:0.52:|K2O:1.68:|MgO:1.07:|CaO:0.59:|Fe2O3:26.27:|TiO2:1.06:|"),
    BENTONITE("Bentonite","SiO2:64.3:|Al2O3:20.7:|Na2O:2.9:|MgO:2.3:|CaO:0.45:|Fe2O3:0.11:|TiO2:0.11:|"),
    BONE_ASH("Bone Ash","SiO2:0.58:|Al2O3:0.07:|Na2O:0.06:|K2O:0.04:|MgO:0.06:|CaO:39.85:|Fe2O3:0.04:|TiO2:0.1:|"),
    BORAX("Borax","SiO2:0.23:|B2O3:13.42:|Al2O3:0.05:|Na2O:20.27:|K2O:0.02:|MgO:0.01:|CaO:0.01:|SrO:0.001:|BaO:0.0014:|"),
    BORIC_ACID("Boric Acid","SiO2:0.16:|B2O3:16.98:|Al2O3:0.03:|Na2O:0.01:|K2O:0.02:|MgO:0.05:|CaO:0.04:|SrO:0.0018:|BaO:0.0029:|Fe2O3:0.04:|TiO2:0.01:|"),
    CnC_BALL_CLAY("C&C Ball Clay","SiO2:53.96:|Al2O3:29.64:|Na2O:0.12:|K2O:0.25:|MgO:0.3:|CaO:0.37:|Fe2O3:0.98:|TiO2:1.64:|"),
    CADYCAL("Cadycal","SiO2:0.5:|B2O3:47:|Al2O3:0.1:|Na2O:0.1:|K2O:0.01:|MgO:0.24:|CaO:26:|SrO:0.03:|"),
    CALCINED_KAOLIN("Calcined Kaolin","SiO2:52.29:|Al2O3:43.8:|Na2O:0.14:|K2O:0.05:|MgO:0.03:|CaO:0.07:|Fe2O3:0.44:|TiO2:1.92:|"),
        //CALCINED_ZINC("Calcined Zinc","SiO2::|Al2O3::|Na2O::|K2O::|MgO::|CaO::|Fe2O3::|TiO2::|"),
        // TODO - FIND MAKEUP OF CALCINED ZINC FROM MR R
    CALCIUM_BORATE("Calcium Borate","B2O3:62.09:|CaO:16.6:|"),
    CORNWALL_STONE("Cornwall Stone","SiO2:74.73:|Al2O3:14.28:|Na2O:2.95:|K2O:3.72:|MgO:0.11:|CaO:1.47:|Fe2O3:0.2:|TiO2:0.04:|ZrO2:0.0029:|"),
    CRYOLITE("Cryolite","SiO2:0.32:|Al2O3:26.27:|Na2O:39.47:|K2O:0.04:|MgO:0.07:|CaO:0.74:|Fe2O3:0.06:|"),
    CUSTER_FELDSPAR("Custer Feldspar","SiO2:69.6:|Al2O3:16.24:|Na2O:2.1:|K2O:10.33:|MgO:0.08:|CaO:0.32:|Fe2O3:0.26:|TiO2:0.03:|"),
    DOLOMITE("Dolomite","MgO:21.85:|CaO:30.41:|"),
    EPK("EPK","SiO2:49.44:|Al2O3:35.46:|K2O:0.4:|MgO:0.16:|CaO:0.05:|Fe2O3:0.51:|TiO2:0.36:|"),
    FRIT_3110("Ferro Frit 3110", "SiO2:69.43337:|B2O3:2.59:|Al2O3:3.92712:|Na2O:15.5171:|K2O:2.17694:|CaO:6.26399:|"),
    FRIT_3124("Ferro Frit 3124", "SiO2:55.30583:|B2O3:13.7666:|Al2O3:9.89743:|Na2O:6.23934:|K2O:0.67734:|CaO:14.1135:|"),
    FRIT_3134("Ferro Frit 3134", "SiO2:47.41935:|B2O3:20.3226:|Na2O:10.3226:|CaO:21.9355:|"),
    FRIT_3185("Ferro Frit 3185", "SiO2:54.1:|B2O3:38.2:|Na2O:7.7:|"),
    FRIT_3195("Ferro Frit 3195", "SiO2:47.44:|B2O3:8.69565:|Al2O3:10.8696:|Na2O:6.52174:|CaO:15.2174:|"),
    FRIT_3221("Ferro Frit 3221", "B2O3:55.4:|CaO:44.6:|"),
    FRIT_3248("Ferro Frit 3248", "SiO2:64.39:|B2O3:4.7:|Al2O3:15.1:|Na2O:1.63:|K2O:7:|MgO:1.99:|CaO:5.2:|"),
    FRIT_3336("Ferro Frit 3336", "SiO2:59.41:|B2O3:11.96:|Al2O3:7.14:|Na2O:5.76:|K2O:0.2:|CaO:5.83:|ZnO:1.03:|ZrO2:8.62:|"),
    FERRO_CC_263("Ferro CC 263","SiO2:50:|B2O3:20:|Al2O3:11:|Na2O:2.5:|K2O:3:|MgO:1:|CaO:9:|SrO:3:|TiO2:0.5:|"),
    FLINT("Flint","SiO2:98.54:|Al2O3:0.42:|MgO:0.01:|CaO:0.01:|Fe2O3:0.06:|TiO2:0.06:|"),
    FLUROSPAR("Flurospar","SiO2:1.06:|Al2O3:0.23:|Na2O:0.16:|K2O:0.06:|MgO:0.04:|CaO:75.21:|Fe2O3:0.07:|FeO:0.02:|TiO2:0.06:|"),
    FOUNDRY_HILL_CREME("Foundry Hill Creme","SiO2:58.4:|Al2O3:24.8:|Na2O:0.18:|K2O:2.3:|MgO:0.31:|CaO:0.04:|Fe2O3:4.7:|TiO2:1.1:|"),
    G200_EU("G200 EU","SiO2:68.58:|Al2O3:17:|Na2O:2.1:|K2O:11:|MgO:0.08:|CaO:0.5:|Fe2O3:0.1:|FeO:1.48:|MnO:0.01:|TiO2:0.03:|"),
    G200_HP("G200 HP","SiO2:65.9:|Al2O3:18.2:|Na2O:1.52:|K2O:13.2:|CaO:0.75:|Fe2O3:0.09:|"),
    GERSTLEY_BORATE("Gerstley Borate", "SiO2:14.8:|B2O3:26.8:|Al2O3:1.32:|Na2O:4.33:|K2O:0.42:|MgO:3.69:|CaO:19.39:|SrO:0.3422:|BaO:0.0376:|Fe2O3:0.39:|TiO2:0.05:|"),
    GLEASON_BALL("Gleason Ball Clay","SiO2:58.8:|Al2O3:26.28:|Na2O:0.07:|K2O:0.32:|MgO:0.04:|CaO:0.09:|Fe2O3:1.41:|TiO2:1.48:|"),
    GLOMAX("Glomax","SiO2:53.15:|Al2O3:44.4:|Na2O:0.3:|K2O:0.41:|CaO:0.05:|Fe2O3:0.42:|TiO2:0.94:|"),
    GOLD_ART("Gold Art","SiO2:55.53:|Al2O3:27.19:|Na2O:0.16:|K2O:1.27:|MgO:0.46:|CaO:0.2:|Fe2O3:1.31:|TiO2:1.98:|ZrO2:0.0223:|"),
    GREENSTRIPE_FIRE_CLAY("Greensripe Fire Clay","SiO2:56.7:|Al2O3:28:|Na2O:0.3:|K2O:0.3:|MgO:0.4:|CaO:0.2:|Fe2O3:2.2:|TiO2:1.7:|"),
    GROLLEG("Grolleg","SiO2:43.45:|Al2O3:32.17:|Na2O:0.2:|K2O:1.61:|MgO:0.32:|CaO:0.12:|Fe2O3:0.74:|TiO2:0.05:|"),
    HAWTHORN_BOND("Harthorn Bond","SiO2:55.1:|Al2O3:39.11:|Na2O:0.24:|K2O:1.24:|MgO:0.85:|CaO:0.15:|Fe2O3:1.02:|TiO2:2.08:|"),
    HELMAR("Helmar","SiO2:46.7:|Al2O3:35.4:|Na2O:0.08:|K2O:0.53:|MgO:0.26:|CaO:0.48:|Fe2O3:1.3:|TiO2:1.13:|"),
    IONE_KAOLIN("Ione Kaolin","SiO2:47.25:|Al2O3:36.33:|Na2O:0.25:|MgO:0.03:|CaO:0.09:|Fe2O3:0.6:|TiO2:2.1:|"),
    IRON_OXIDE_BLACK("Black Iron Oxide","Fe2O3:63.4:|FeO:28.5:|"),
    IRON_OXIDE_RED("Red Iron Oxide","Fe2O3:100:|"),
    IRON_OXIDE_RED_SPANISH("Spanish Red Iron Oxide","SiO2:5:|Fe2O3:87:|"),
    IRON_OXIDE_YELLOW("Yellow Iron Oxide","Fe2O3:88:|"),
    IRON_SULFATE("Iron Sulfate","Fe2O3:57.5:|"),
    K_200("K-200","SiO2:68.12:|Al2O3:17.62:|Na2O:3:|K2O:10.98:|CaO:0.23:|Fe2O3:0.05:|"),
    KAOPAQUE("Kaopaque","SiO2:45.92:|Al2O3:37.57:|Na2O:0.08:|K2O:0.23:|MgO:0.04:|CaO:0.08:|Fe2O3:0.36:|TiO2:1.13:|"),
    KONA_F4("Kona F4 Feldspar","SiO2:68.5:|Al2O3:18.56:|Na2O:6.22:|K2O:4.61:|MgO:0.01:|CaO:1.45:|Fe2O3:0.07:|"),
    LAGUNA_BORATE("Laguna Borate","SiO2:23.33:|B2O3:6.98:|Al2O3:8.52:|Na2O:3.95:|K2O:1.33:|MgO:2.6:|CaO:17.36:|SrO:0.0163:|BaO:0.0026:|Fe2O3:0.23:|TiO2:0.05:|"),
    LATERITE("Laterite","SiO2:32.5:|Al2O3:29.3:|Na2O:0.01:|K2O:0.01:|MgO:1.5:|CaO:0.3:|Fe2O3:24:|TiO2:1.5:|"),
    LITHIUM_CARBONATE("Lithium Carbonate","Li2O:40.37:|"),
    MAGNESIUM_CARBONATE("Magnesium Carbonate","MgO:48.99:|"),
    MAHAVIR_FELDSPAR("Mahavir Feldspar","SiO2:67:|Al2O3:17.5:|Na2O:3:|K2O:11.5:|MgO:0.15:|CaO:0.15:|Fe2O3:0.08:|"),
    MINSPAR("Minspar","SiO2:69.1:|Al2O3:18.65:|Na2O:6.24:|K2O:4.04:|MgO:0.01:|CaO:1.31:|SrO:0.0216:|BaO:0.0489:|Fe2O3:0.07:|TiO2:0.01:|"),
    MANGANESE_CARBONATE("Manganese Carbonate","MnO:61.62:|"),
    MANGANESE_OXIDE("Manganese Oxide","MnO:81.5:|"),
    MANGANESE_DIOXIDE("Manganese Dioxide","MnO2:100:|"),
    MULLITE("Mullite","SiO2:41.1:|B2O3:55.78:|Na2O:0.06:|K2O:0.04:|MgO:0.03:|CaO:0.06:|Fe2O3:0.91:|TiO2:1.13:|"),
    NC_4_BALL_CLAY("NC-4 Ball Clay","SiO2:69.94:|Al2O3:17.47:|Na2O:6.37:|K2O:3.81:|MgO:0.02:|CaO:1.38:|Fe2O3:0.1:|TiO2:0.02:|"),
    NEPHELINE_SYENITE("Nepheline Syenite","SiO2:61.95:|Al2O3:22.1:|Na2O:10.29:|K2O:4.4:|MgO:0.03:|CaO:0.36:|Fe2O3:0.04:|TiO2:0.01:|"),
    NEWMAN_RED_CLAY("Newman Red Clay","SiO2:59.66:|Al2O3:16.64:|Na2O:0.23:|MgO:1.35:|Fe2O3:5.58:|TiO2:2.05:|"),
    NY_TALC("NY Talc","SiO2:50.76:|Al2O3:0.23:|Na2O:0.14:|K2O:0.15:|MgO:30.93:|CaO:7.67:|Fe2O3:0.06:|TiO2:0.03:|"),
    OLD_HICKORY_M23("Old Hickory M-23","SiO2:57.64:|Al2O3:28.72:|Na2O:0.08:|K2O:0.36:|MgO:0.35:|CaO:0.1:|Fe2O3:1.27:|TiO2:1.85:|"),
    OM4_BALL_CLAY("OM4 Ball Clay","SiO2:53:|Al2O3:30.8:|Na2O:0.3:|K2O:2.84:|MgO:0.3:|CaO:0.4:|Fe2O3:0.8:|TiO2:1.6:|"),
    PEARL_ASH("Pearl Ash","K2O:68:|"),
    PEERLESS("Peerless","SiO2:44.6:|Al2O3:39.5:|Na2O:0.04:|K2O:0.52:|MgO:0.07:|CaO:0.05:|Fe2O3:0.45:|TiO2:1.4:|"),
    PETALITE("Petalite","SiO2:75.47:|Al2O3:16.1:|Li2O:2.25:|Na2O:0.28:|K2O:0.15:|MgO:0.05:|CaO:0.11:|SrO:0.0019:|BaO0.0005:|Fe2O3:0.06:|TiO2:0.01:|"),
    PIONEER_TALC("Pioneer Talc","SiO2:53.5:|Al2O3:0.5:|K2O:0.3:|MgO:29.5:|CaO:3.5:|Fe2O3:0.5:|TiO2:0.1:|"),
    PLASTIC_VITROX("Plastic Vitrox","SiO2:77.15:|Al2O3:12.92:|Na2O:1.5:|K2O:4.9:|MgO:0.07:|CaO:0.5:|Fe2O3:0.23:|TiO2:0.37:|"),
    PYRAX("Pyrax","SiO2:76.52:|Al2O3:16.31:|Na2O:0.19:|K2O:2.37:|MgO:0.01:|CaO:0.02:|Fe2O3:0.36:|TiO2:0.21:|"),
    RED_ART("Red Art","SiO2:54.3:|Al2O3:16.4:|Na2O:0.4:|K2O:4.07:|MgO:1.55:|CaO:0.23:|Fe2O3:7.04:|TiO2:1.06:|ZrO2:0.0223:|"),
    SALT_LICK("Salt Lick","SiO2:58.36:|Al2O3:25.19:|Na2O:0.19:|K2O:2.84:|MgO:0.62:|CaO:0.17:|Fe2O3:1.38:|TiO2:1.49:|"),
    SILICA_200M("Silica (200 Mesh)","SiO2:100:|"),
    SILICA_325M("Silica (325 Mesh)","SiO2:100:|"),
    SILVERLINE_TALC("Silverline Talc","SiO2:62:|Al2O3:0.5:|MgO:31:|CaO:1:|Fe2O3:0.5:|"),
    SODA_ASH("Soda Ash","Na2O:58:|"),
    SPODUMENE("Spodumene","SiO2:63.02:|Al2O3:25.19:|Na2O:0.19:|K2O:2.84:|MgO:0.62:|CaO:0.17:|Fe2O3:1.38:|TiO2:1.43:|"),
    STRONTIUM_CARBONATE("Strontium Carbonate","SrO:70.19:|"),
    TENN_10("Tenn #10","SiO2:54.2:|Al2O3:30.59:|Na2O:0.11:|K2O:0.34:|MgO:0.18:|CaO:0.5:|Fe2O3:0.78:|TiO2:2.32:|"),
    TILE_6_CLAY("Tile 6 Clay","SiO2:46.9:|Al2O3:38.2:|Na2O:0.04:|MgO:0.58:|CaO:0.43:|Fe2O3:0.35:|TiO2:1.42:|"),
    VEEGUM("VeeGum","SiO2:54.92:|Al2O3:0.99:|Li2O:1:|Na2O:1.78:|K2O:0.11:|MgO:24.34:|CaO:4.46:|Fe2O3:0.14:|"),
    WHITING("Whiting", "CaO:56.03:|"),
    WOLLASTONITE("Wollastonite", "SiO2:50.47:|Al2O3:0.82:|Na2O:0.1:|K2O:0.12:|MgO:1.12:|CaO:43.53:|Fe2O3:0.46:|TiO2:0.02:|"),
    XX_SAGGAR("XX Saggar","SiO2:57.4:|Al2O3:28.9:|Na2O:0.3:|K2O:0.9:|MgO:0.3:|CaO:0.5:|Fe2O3:0.7:|TiO2:1.7:|"),
    YELLOW_BANKS_101("Yellow Banks 101","SiO2:57.76:|Al2O3:25.21:|Na2O:0.25:|K2O:2.79:|MgO:0.71:|CaO:0.23:|SrO:0.0137:|BaO:0.0528:|Fe2O3:2.29:|TiO2:1.26:|"),
    YELLOW_BANKS_401("Yellow Banks 401","SiO2:54.61:|Al2O3:30.91:|Na2O:0.03:|K2O:0.01:|MgO:0.37:|CaO:0.38:|Fe2O3:1.46:|TiO2:0.82:|"),
    YELLOW_OCHRE("Yellow Ochre","SiO2:57.78:|B2O3:19.92:|MgO:0.06:|CaO:0.23:|Fe2O3:22.01:|"),
    ZINC_OXIDE("Zinc Oxide","ZnO:100:|");


    String name;
    ArrayList<OxideQuantity> oxides;

    Ingredient(String friendlyName, ArrayList<OxideQuantity> oxides) {
        name = friendlyName;
        this.oxides = oxides;
    }

    Ingredient(String friendlyName, OxideQuantity... oxides) {
        name = friendlyName;
        Collections.addAll(this.oxides,oxides);
    }

    Ingredient(String friendlyName, String oxidesLongString) {
        name = friendlyName;
        oxides = OxideQuantity.parseFromLongString(oxidesLongString);
    }


    /*Thank the gods of Stack Overflow*/
    private static final Map<String, Ingredient> valueMap;
    static {
        final Map<String, Ingredient> initMap = new HashMap<String, Ingredient>();
        for (final Ingredient e : Ingredient.values())
            initMap.put(e.name,e);
        valueMap = initMap;
    }

    public static Ingredient getEnum(String name) {
        if(!valueMap.containsKey(name)) {
            throw new IllegalArgumentException("Unknown String Value: " + name);
        }
        return valueMap.get(name);
    }
    /*Thank the gods of Stack Overflow*/

    public String toString () {
        return name;
    }
}
