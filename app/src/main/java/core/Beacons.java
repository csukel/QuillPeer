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

    /*testing vars*/
    private static String dist1;
    private static String dist2;
    private static String dist3;
    private static String dist4;
    private static String dist5;
    private static String dist6;


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
