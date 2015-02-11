package core;

import java.util.HashMap;

/**
 * Created by loucas on 11/02/2015.
 */
public class Beacons {

    private static final String mac1 = "CB:06:80:62:D2:AF";
    private static final String mac2 = "D8:43:98:B3:3D:DC";
    private static final String mac3 = "C8:4F:8E:87:04:EE";
    private static HashMap<String,Integer> beaconIndexTable = new HashMap<String,Integer>();
    static {
       beaconIndexTable.put(mac1,1);
       beaconIndexTable.put(mac2,2);
       beaconIndexTable.put(mac3,3);
    }

    public static HashMap getBeaconsIndices(){
        return beaconIndexTable;
    }
}
