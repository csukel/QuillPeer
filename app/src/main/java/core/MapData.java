package core;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
/*    public static double screenMaxX = 20;
    public static double screenMaxY = 20;*/
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
                    /*if the result is not a null object*/
                    if (result!=null) {
                        /*get the result returned back from the server api*/
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if the api returns successful call then*/
                if (outcome){
                    /*the map size is retrieved*/
                    haveMapSize = true;
                    /*set the values that have been got back from the server*/
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
                /*get the filter criteria for map which can be found in the settings screen*/
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.currentActivity());
                boolean isProfessorsSelected = preferences.getBoolean("qfilter_professor",true);
                boolean isAssociateSelected = preferences.getBoolean("qfilter_associate",true);
                boolean isAssistantSelected = preferences.getBoolean("qfilter_assistant",true);
                boolean isPostdocSelected = preferences.getBoolean("qfilter_postdoc",true);
                boolean isPhdSelected = preferences.getBoolean("qfilter_phd",true);
                boolean isOtherSelected = preferences.getBoolean("qfilter_other",true);

                /*set the corresponding strings in a json array*/
                JSONArray jsonArray = new JSONArray();
                if (isProfessorsSelected){
                    jsonArray.put("Professor");
                }
                if (isAssociateSelected){
                    jsonArray.put("Associate / Senior lecturer / Reader");
                }
                if (isAssistantSelected){
                    jsonArray.put("Assistant / Lecturer");
                }
                if (isPostdocSelected){
                    jsonArray.put("Postdoc");
                }
                if (isPhdSelected){
                    jsonArray.put("PhD students");
                }
                if (isOtherSelected){
                    jsonArray.put("Other");
                }
                /*send the request to the server*/
                return ServerComm.getRecommendation("20",jsonArray);
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
                        double x = jsonObject.getJSONObject("user").getDouble("x_axis");
                        double y = jsonObject.getJSONObject("user").getDouble("y_axis");
                        markerList.add(new MapMarker(MyApplication.currentActivity(), User.getInstance(),calcScreenX(x),calcScreenY(y)));

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
                                    markerList.add(new MapMarker(MyApplication.currentActivity(),person,calcScreenX(x),calcScreenY(y)));
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
                isUpdating = false;
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

        double i =0.5;
        while (originalX>=maxX-0.5){
            originalX -= i;

        }
        i=0.5;
        while(originalX<=0.5){
            originalX +=i;

        }
        return originalX;
    }

    public static double calcScreenY(double originalY){

        double i =0.5;
        while (originalY>=maxY-0.5){
            originalY -= i;

        }
        i=1;
        while(originalY<=0.5){
            originalY +=i;

        }

        return originalY;
    }

    public static boolean isUpdating(){
        return isUpdating;
    }
}
