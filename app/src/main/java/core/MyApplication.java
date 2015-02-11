package core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by loucas on 10/02/2015.
 */
public class MyApplication extends Application {
    private static Context context;
    private static Activity activity;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public synchronized static Context getAppContext() {
        return MyApplication.context;
    }

    /**
     * setCurrentActivity(null) in onPause() on each activity
     * setCurrentActivity(this) in onResume() on each activity
     *
     */

    public static void setCurrentActivity(Activity currentActivity) {
        activity = currentActivity;
    }

    public static Activity currentActivity() {
        return activity;
    }

}
