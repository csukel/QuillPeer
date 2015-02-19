package core;

import java.util.HashMap;

/**
 * Created by loucas on 11/02/2015.
 */
public class Beacons {

    private static final String mac1 = "CB:06:80:62:D2:AF";
    private static final String mac2 = "D8:43:98:B3:3D:DC";
    private static final String mac3 = "C8:4F:8E:87:04:EE";
    private static final String mac4 = "C7:2D:CC:B4:CF:B8";
    private static final String mac5 = "E2:C7:B0:CB:D8:68";
    private static final String mac6 = "F4:ED:13:F3:F8:46";

    private static HashMap<String,Integer> beaconIndexTable = new HashMap<String,Integer>();
    static {
       beaconIndexTable.put(mac1,1);
       beaconIndexTable.put(mac2,2);
       beaconIndexTable.put(mac3,3);
       beaconIndexTable.put(mac4,4);
       beaconIndexTable.put(mac5,5);
       beaconIndexTable.put(mac6,6);
    }

    public static HashMap getBeaconsIndices(){
        return beaconIndexTable;
    }
}
