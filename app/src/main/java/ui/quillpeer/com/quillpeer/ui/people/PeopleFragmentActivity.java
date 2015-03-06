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
 * Created by loucas on 06/03/2015.
 */
public class PeopleFragmentActivity extends FragmentActivity {
    private Handler handleInternetStateMsg = new Handler();
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
    @Override
    public void onResume(){
        super.onResume();
        MyApplication.setCurrentActivity(this);
    }

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

    //display an alert when user is not connected to the internet
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        handleInternetStateMsg.removeCallbacks(checkInternetState);
    }
}
