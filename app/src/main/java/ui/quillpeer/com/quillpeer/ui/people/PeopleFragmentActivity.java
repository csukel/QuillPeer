package ui.quillpeer.com.quillpeer.ui.people;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.MenuItem;

import com.devspark.appmsg.AppMsg;

import core.MyApplication;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the activity which is launched when the user selects the People option from the navigation drawer
 * in the MainActivity
 * Created on 06/03/2015.
 * @author Loucas Stylianou
 */
public class PeopleFragmentActivity extends FragmentActivity {
    /** handler that checks the network state */
    private Handler handleInternetStateMsg = new Handler();

    /**
     * When the activity is created the UI is initialised and the PeopleFragment is launched.
     * @param savedInstance
     */
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_people);

        try{
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException nex){
            nex.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new PeopleFragment())
                .commit();

        //run a handler to check every 1 sec (with initial post delay of 2 secs) the internet state and
        //display to the user the corresponding alert msg
        handleInternetStateMsg.postDelayed(checkInternetState,2000);
    }

    /**
     * OnResume set this activity as the current activity in the MyApplication class
     */
    @Override
    public void onResume(){
        super.onResume();
        MyApplication.setCurrentActivity(this);
    }

    /**
     * When the home button is selected from the options menu close this activity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * display an alert when user is not connected to the internet
     */
    Runnable checkInternetState = new Runnable() {
        @Override
        public void run() {
            if (!ServerComm.isNetworkConnected(getApplicationContext(), PeopleFragmentActivity.this)){
                AppMsg.makeText(PeopleFragmentActivity.this, "You are not connected to the internet...", AppMsg.STYLE_ALERT).setLayoutGravity(Gravity.BOTTOM).show();
            }else {
                AppMsg.cancelAll(PeopleFragmentActivity.this);
            }
            handleInternetStateMsg.postDelayed(this,1000);
        }
    };

    /**
     * When this activity is destroyed, remove any other than the main ui running threads
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        handleInternetStateMsg.removeCallbacks(checkInternetState);
    }
}
