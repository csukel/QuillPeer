package core;

import java.util.HashMap;

/**
 * This class captures all info related to the beacons hardware
 * Created on 11/02/2015.
 * @author Loucas Stylianou
 */
public class Beacons {
    /** Mac Address of the beacon */
    private static final String mac1 = "CB:06:80:62:D2:AF";
    /** Mac Address of the beacon */
    private static final String mac2 = "D8:43:98:B3:3D:DC";
    /** Mac Address of the beacon */
    private static final String mac3 = "C8:4F:8E:87:04:EE";
    /** Mac Address of the beacon */
    private static final String mac4 = "C7:2D:CC:B4:CF:B8";
    /** Mac Address of the beacon */
    private static final String mac5 = "E2:C7:B0:CB:D8:68";
    /** Mac Address of the beacon */
    private static final String mac6 = "F4:ED:13:F3:F8:46";

    /*testing vars*/
    private static String dist1;
    private static String dist2;
    private static String dist3;
    private static String dist4;
    private static String dist5;
    private static String dist6;


    /**
     * A hash map that contains beacons mac address and given unique identifier. It is used to simplify the testing process.
     */
    private static HashMap<String,Integer> beaconIndexTable = new HashMap<String,Integer>();

    /**
     * this block is used to store an identifier for each beacon
     */
    static {
       beaconIndexTable.put(mac1,1);
       beaconIndexTable.put(mac2,2);
       beaconIndexTable.put(mac3,3);
       beaconIndexTable.put(mac4,4);
       beaconIndexTable.put(mac5,5);
       beaconIndexTable.put(mac6,6);
    }

    /**
     * Retrieve the hash map that contains beacons mac addresses and unique identifiers
     * @return
     */
    public static HashMap getBeaconsIndices(){
        return beaconIndexTable;
    }

    /*the following methods are created for testing*/
    public static void setDist1(String d){
        dist1 = d;
    }
    public static void setDist2(String d){
        dist2 = d;
    }
    public static void setDist3(String d){
        dist3 = d;
    }
    public static void setDist4(String d){
        dist4 = d;
    }
    public static void setDist5(String d){
        dist5 = d;
    }
    public static void setDist6(String d){
        dist6 = d;
    }

    public static String getDist1(){
        return dist1;
    }
    public static String getDist2(){
        return dist2;
    }
    public static String getDist3(){
        return dist3;
    }
    public static String getDist4(){
        return dist4;
    }
    public static String getDist5(){
        return dist5;
    }
    public static String getDist6(){
        return dist6;
    }
}
