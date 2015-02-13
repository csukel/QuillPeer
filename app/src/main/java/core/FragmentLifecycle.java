package core;

import android.support.v4.app.FragmentActivity;

/**
 * Created by loucas on 13/02/2015.
 */
public interface FragmentLifecycle {

    public void onPauseFragment(FragmentActivity activity);
    public void onResumeFragment(FragmentActivity activity);
}
