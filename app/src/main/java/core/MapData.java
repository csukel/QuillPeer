package core;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
    private MapData(){}

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
                    //TODO check the outome when the api from the server is ready
                    haveMapSize = true;
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
        class SendGetReqAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {

                return ServerComm.getRecommendation();
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
                    //TODO check the outome when the api from the server is ready
                    haveMapSize = true;
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
}
