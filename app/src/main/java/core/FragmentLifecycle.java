package core;

import android.support.v4.app.FragmentActivity;

/**
 * Interface which helps to capture the actual life cycle of fragment used in a pager object
 * Created on 13/02/2015.
 * @author Loucas Stylianou
 */
public interface FragmentLifecycle {

    /**
     * Declare the onPauseFragment phase of the fragment's lifecycle
     * @param activity
     */
    public void onPauseFragment(FragmentActivity activity);

    /**
     * Declare the onResumeFragment phase of the fragment's lifecycle
     * @param activity
     */
    public void onResumeFragment(FragmentActivity activity);
}
