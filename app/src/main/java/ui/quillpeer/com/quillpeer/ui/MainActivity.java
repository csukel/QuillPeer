package ui.quillpeer.com.quillpeer.ui;

import android.app.ActionBar;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
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
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;

import core.MyApplication;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.people.PeopleFragment;
import ui.quillpeer.com.quillpeer.ui.timetable.TimetableFragment;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private BeaconManager beaconManager;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", null, null, null);
    private Handler handlerAveragingBeaconsDistance;
    private Handler handleBleEnabled = new Handler();
    private BluetoothAdapter bleAdapter ;


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
        MyApplication.setCurrentActivity(this);
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
/*    protected void checkBleOn(){
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                // If Bluetooth is not enabled, let user enable it.
                if (!beaconManager.isBluetoothEnabled()) {
                    bleAdapter.enable();
                }
            }
        };
        t.start();
    }*/

    BeaconManager.RangingListener rangingListener = new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
            // Note that results are not delivered on UI thread.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Note that beacons reported here are already sorted by estimated
                    // distance between device and beacon.
                    getActionBar().setSubtitle("Found beacons: " + beacons.size());
                    //beaconsList = (ArrayList)beacons;
                    beaconsList = new ArrayList<Beacon>(beacons);
                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
/*            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/
            bleAdapter.enable();
        } else {
            connectToService();
        }
    }

    @Override
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
    }

/*    @Override
    protected void onStop() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }

        super.onStop();
    }*/

    @Override
    protected void onDestroy() {
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


}
