package ui.quillpeer.com.quillpeer.ui;

import android.app.ActionBar;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import core.Beacons;
import core.MyApplication;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.people.PeopleFragment;
import ui.quillpeer.com.quillpeer.ui.timetable.TimetableFragment;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private BeaconManager beaconManager;
    private boolean  doubleBackToExitPressedOnce;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", null, null, null);
    private Handler handleInternetStateMsg = new Handler();
    private Handler handleBleEnabled = new Handler();
    private BluetoothAdapter bleAdapter ;
    private HashMap<String,ArrayList<Double>> beaconsDistancesList;
    private int measurementsCounter = 0;
    private Toast m_currentToast;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static boolean isPeopleFragmentVisible = false;
    private ArrayList<Beacon> beaconsList = new ArrayList<Beacon>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconsDistancesList = new HashMap<String,ArrayList<Double>>();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        //get bluetooth adapter
        bleAdapter = BluetoothAdapter.getDefaultAdapter();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Configure BeaconManager.
        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(rangingListener);
        //run the checkBleOn thread which intents to enable bluetooth when the user disables it manually
        handleBleEnabled.postDelayed(ckeckBleOn,2000);
        //run a handler to check every 1 sec (with initial post delay of 2 secs) the internet state and
        //display to the user the corresponding alert msg
        handleInternetStateMsg.postDelayed(checkInternetState,2000);
        //start the communication with beacons
        startBeaconsComm();
    }

    Runnable ckeckBleOn = new Runnable(){

        @Override
        public void run() {
            // If Bluetooth is not enabled, let user enable it.
            if (!beaconManager.isBluetoothEnabled()) {
                bleAdapter.enable();
            }
            handleBleEnabled.postDelayed(this,500);
        }
    };

    //display an alert when user is not connected to the internet
    Runnable checkInternetState = new Runnable() {
        @Override
        public void run() {
            if (!ServerComm.isNetworkConnected(getApplicationContext(),MainActivity.this)){
                AppMsg.makeText(MainActivity.this,"You are not connected to the internet...",AppMsg.STYLE_ALERT).show();
            }else {
                AppMsg.cancelAll(MainActivity.this);
            }
            handleInternetStateMsg.postDelayed(this,1000);
        }
    };

    BeaconManager.RangingListener rangingListener = new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
            // Note that results are not delivered on UI thread.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Note that beacons reported here are already sorted by estimated
                    // distance between device and beacon.
                    try {
                        getActionBar().setSubtitle("Found beacons: " + beacons.size());
                    }catch (NullPointerException nex){
                        nex.printStackTrace();
                    }
                    //beaconsList = (ArrayList)beacons;
                    beaconsList = new ArrayList<Beacon>(beacons);

                }
            });
            //if measurements are less than 5 then store the next set of measurements
            if (measurementsCounter < 5) {
                for (Beacon beacon : beaconsList) {
                    String beaconId = beacon.getMacAddress();
                    double distance = Utils.computeAccuracy(beacon);
                    if (beaconsDistancesList.containsKey(beaconId)) {
                        ArrayList<Double> distanceList = beaconsDistancesList.get(beaconId);
                        distanceList.add(distance);
                        Log.d(TAG, "Distance list length: " + distanceList.size());
                    } else {
                        ArrayList<Double> distanceList = new ArrayList<Double>();
                        distanceList.add(distance);
                        beaconsDistancesList.put(beacon.getMacAddress(), distanceList);
                    }
                }
                measurementsCounter++;
            }
            //otherwise iterate through the hash map
            else {
                //do the averaging for each beacon
                Iterator iterator = beaconsDistancesList.keySet().iterator();
                JSONArray jsonArray = new JSONArray();
                while (iterator.hasNext()){
                    String macaddr = (String)iterator.next();
                    ArrayList<Double> distanceList = beaconsDistancesList.get(macaddr);
                    double sum=0.0;
                    for (int i =0;i<distanceList.size(); i++){
                        //calc the sum of the double stored in the array list of distances for each beacon
                        sum += distanceList.get(i);
                    }
                    //calc the average for each one
                    double avgDist = sum/distanceList.size();
                    Log.d(TAG,"Average of beacon: " + macaddr + " is " + (sum/distanceList.size()));
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //wrap them up into a JSONObject
                        jsonObject.put("id", Beacons.getBeaconsIndices().get(macaddr));
                        jsonObject.put("mac_address",macaddr);
                        BigDecimal averageDist = new BigDecimal(avgDist);

                        jsonObject.put("beacon_dist",averageDist.divide(new BigDecimal("1"), 3, BigDecimal.ROUND_HALF_UP));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //add the jsonObject into a json array
                    jsonArray.put(jsonObject);
                }
                //sent the json array to the server
                sendBeaconsToServer(jsonArray);
                Log.d(TAG,"beacon averaging");
                measurementsCounter =0;
                beaconsDistancesList.clear();
            }
            Log.d(TAG,"Beacons ranging continues...");
            //Log.d(TAG,"Beacon 0: " + beacons.get(0).getMacAddress() + " Distance: " + Utils.computeAccuracy(beacons.get(0)));
        }
    };



    protected void startBeaconsComm() {
        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }
        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            bleAdapter.enable();
        }
        connectToService();
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                getActionBar().setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"Activity is onStop");
    }

    @Override
    protected void onDestroy() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
        //when activity is destroyed, disconnect from the beacon service
        beaconManager.disconnect();
        super.onDestroy();
    }
    /*
    * Loucas
    * When on of the navigation drawer item is selected then show the corresponding fragment
    * */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //declare and initialize each fragment object for the main menu screens(Profile, People, Timetable,Settings,About)
        ProfileFragment fragmentProfile = new ProfileFragment();
        PeopleFragment fragmentPeople = new PeopleFragment();
        TimetableFragment fragmentTimetable = new TimetableFragment();
        SettingsFragment fragmentSettings = new SettingsFragment();
        AboutFragment fragmentAbout = new AboutFragment();

        Bundle args = new Bundle();
        args.putInt("Position", position);
        Fragment gFragment = null;

        switch (position) {
            case 0:
                gFragment = fragmentProfile;
                break;
            case 1:
                gFragment = fragmentPeople;
                break;
            case 2:
                gFragment = fragmentTimetable;
                break;
            case 3:
                gFragment = fragmentSettings;
                break;
            case 4:
                gFragment = fragmentAbout;
                break;
        }
        //Store the position of the selected fragment into bundle
        gFragment.setArguments(args);
        //Show gFragment object on the screen
        fragmentManager.beginTransaction()
                .replace(R.id.container, gFragment)
                .commit();
    }

    //Set title on the action bar when a list item is selected from navigation drawer
    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                break;
            case 4:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }catch  (Exception ex){
            Log.e(TAG,"Main activity",ex);
        }
    }

    /* Called whenever invalidateOptionsMenu() is called */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

/*        if(isPeopleFragmentVisible){
            menu.findItem(R.id.search).setVisible(true);
        }else{
            menu.findItem(R.id.search).setVisible(false);
        }*/

        return super.onPrepareOptionsMenu(menu);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.search_menu, menu);

            SearchManager searchManager = (SearchManager)
                    getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchMenuItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) searchMenuItem.getActionView();

            searchView.setSearchableInfo(searchManager.
                    getSearchableInfo(getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            //searchView.setOnQueryTextListener(this);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void connectToService() {
        getActionBar().setSubtitle("Scanning...");
        beaconsList.clear();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e( TAG,"Cannot start ranging",e);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //set this activity as the current activity
        MyApplication.setCurrentActivity(this);
    }

    //create an async task to execute the post request to the server to send beacons' measurements
    private void sendBeaconsToServer(JSONArray jsonArray) {

        class SendPostReqAsyncTask extends AsyncTask<JSONArray, Void, String> {
            @Override
            protected String doInBackground(JSONArray... params) {

                JSONArray paramJArray = params[0];

                return ServerComm.savePosition(paramJArray);
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
                }
                else{
                    //showToast(msg,Toast.LENGTH_SHORT);
                    Log.e(TAG,"Error when sending beacon values");
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getApplicationContext(),this)){
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(jsonArray);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast("Please click BACK again to exit",Toast.LENGTH_SHORT);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    //show toasts
    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(getApplicationContext(), text,toast_length);
        m_currentToast.show();

    }
}
