package core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is used to save the state of the application while is running
 * Created on 10/02/2015.
 * @author Loucas Stylianou
 */
public class MyApplication extends Application {
    /** The app's context */
    private static Context context;
    /** Running activity */
    private static Activity activity;
    /** The shared preferences which are used to store global vars for the app */
    private static SharedPreferences sharedPreferences;

    /**
     * When the app is launched save its context
     */
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    /**
     * Retrieve app's context
     * @return context
     */
    public synchronized static Context getAppContext() {
        return MyApplication.context;
    }

    /*
     * setCurrentActivity(null) in onPause() on each activity
     * setCurrentActivity(this) in onResume() on each activity
     *
     */

    /**
     * Set the current activity when the activity is onResume
     * @param currentActivity
     */
    public static void setCurrentActivity(Activity currentActivity) {
        activity = currentActivity;
    }

    /**
     * Get the running activity
     * @return activity
     */
    public static Activity currentActivity() {
        return activity;
    }

    /**
     * Get the shared preferences
     * @return sharedPreferences
     */
    public static SharedPreferences getPrefs(){
        return sharedPreferences;
    }

    /**
     * Save the application's context
     * @param c Context
     */
    public static void setApplicationContext(Context c){
        context = c;
    }

    /**
     * Store the shared preferences
     * @param preferences Shared preferences
     */
    public static void setPrefs(SharedPreferences preferences){
        sharedPreferences = preferences;
    }

}
