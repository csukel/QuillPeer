package core;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import core.People.OtherParticipant;
import core.People.User;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.ui.people.MapFragment;
/**
 * Created by loucas on 23/02/2015.
 */
public class MapData {
    private static double maxX;
    private static double maxY;
    public static boolean haveMapSize;
    private static final String TAG = MapData.class.getSimpleName();
    private static List<MapMarker> markerList;
    private MapData(){}
    public static double screenMaxX = 20;
    public static double screenMaxY = 20;
    private static boolean isUpdating = false;

    //trigger the get request from ServerComm responsible for getting the map size
    public static void getMapSize(){
        class SendGetReqAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {

                return ServerComm.getMapSize();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject jsonObject=null;
                //String msg = "Something went wrong";
                boolean outcome = false;
                try {
                    if (result!=null) {
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (outcome){
                    haveMapSize = true;
                    try {
                        maxX = jsonObject.getJSONObject("coordinates").getDouble("x");
                        maxY = jsonObject.getJSONObject("coordinates").getDouble("y");
                    } catch (JSONException e) {
                        Log.e(TAG,e.toString());
                    }

                }
                else{
                    //showToast(msg,Toast.LENGTH_SHORT);
                    Log.e(TAG, "Error when sending beacon values");
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(MyApplication.getAppContext(),MyApplication.currentActivity())){
            SendGetReqAsyncTask sendPostReqAsyncTask = new SendGetReqAsyncTask();
            sendPostReqAsyncTask.execute();
        }

    }


    //trigger the get request from ServerComm responsible for getting the map size
    public static void getRecommendation(){
        isUpdating =true;
        class SendGetReqAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {

                return ServerComm.getRecommendation("20");
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject jsonObject=null;
                //String msg = "Something went wrong";
                boolean outcome = false;
                try {
                    if (result!=null) {
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (outcome){
                    markerList = new ArrayList<MapMarker>();

                    try {
                        markerList.add(new MapMarker(MyApplication.currentActivity(), User.getInstance(),jsonObject.getJSONObject("user").getDouble("x_axis"),
                                jsonObject.getJSONObject("user").getDouble("y_axis")));
                    } catch (JSONException e) {
                        Log.e(TAG,e.toString());
                    }
                    JSONArray jsonArray=null;
                    try {
                        jsonArray = jsonObject.getJSONArray("people");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonArray != null && haveMapSize){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                if (jsonArray.get(i) instanceof JSONObject) {
                                    JSONObject ob = (JSONObject) jsonArray.get(i);
                                    OtherParticipant person = new OtherParticipant(ob.getString("id"),ob.getString("prefix"),
                                            ob.getString("first_name"),ob.getString("last_name"),ob.getString("university"),
                                            ob.getString("department"),ob.getString("email"),ob.getString("is_speaker").equals("1"),false,
                                            ob.getString("qualification"));
                                    String picture = ob.getString("picture");
                                    if (!(picture.equals("null"))){
                                        Bitmap bp = ImageProcessing.decodeImage(ob.getString("picture"));
                                        person.setProfilePicture(bp);
                                    }
                                    double x = ob.getJSONObject("location").getDouble("x_axis");
                                    double y = ob.getJSONObject("location").getDouble("y_axis");
                                    markerList.add(new MapMarker(MyApplication.currentActivity(),person,MapData.calcScreenX(x),MapData.calcScreenY(y)));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            isUpdating = false;
                        }
                    }

                }
                else{
                    //showToast(msg,Toast.LENGTH_SHORT);
                    Log.e(TAG, "Error when sending beacon values");
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(MyApplication.getAppContext(),MyApplication.currentActivity())){
            SendGetReqAsyncTask sendPostReqAsyncTask = new SendGetReqAsyncTask();
            sendPostReqAsyncTask.execute();
        }

    }
    public static double getMaxX(){
        return maxX;
    }

    public static double getMaxY(){
        return maxY;
    }

    public static void setMaxX(double x){
        maxX = x;
    }

    public static void setMaxY(double y){
        maxY = y;
    }

    public static List<MapMarker> getMarkerList(){
        return markerList;
    }

    public static double calcScreenX(double originalX){
        double xcoor = (screenMaxX/getMaxX())*originalX;
        xcoor = screenMaxY/getMaxY()*(originalX);
        int i =1;
        while (xcoor>=screenMaxY-0.5){
            xcoor = screenMaxY/getMaxY()*(originalX-i);
            i++;
        }
        i=1;
        while(xcoor<=0.5){
            xcoor = screenMaxY/getMaxY()*(originalX+i);
            i++;
        }
        return xcoor;
    }

    public static double calcScreenY(double originalY){
        double ycoor = (screenMaxY/getMaxY()*originalY);
        ycoor = screenMaxY/getMaxY()*(originalY);
        int i =1;
        while (ycoor>=screenMaxY-0.5){
            ycoor = screenMaxY/getMaxY()*(originalY-i);
            i++;
        }
        i=1;
        while(ycoor<=0.5){
            ycoor = screenMaxY/getMaxY()*(originalY+i);
            i++;
        }
        return ycoor;
    }

    public static boolean isUpdating(){
        return isUpdating;
    }
}
