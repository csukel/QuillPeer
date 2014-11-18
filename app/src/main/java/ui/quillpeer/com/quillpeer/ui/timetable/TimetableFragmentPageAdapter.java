package ui.quillpeer.com.quillpeer.ui.timetable;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by loucas on 18/11/2014.
 * This class defines the page adapter for the timetable screens.
 */
public class TimetableFragmentPageAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    public TimetableFragmentPageAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }
    // return number of tabs
    @Override
    public int getCount() {
        return(2);
    }

    //When the user swipes left or right the position changes hence
    //use this method to return the corresponding (fragment) screen to the user
    @Override
    public Fragment getItem(int position) {
        android.support.v4.app.Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new DayView();
                break;
            case 1:
                fragment = new WeekView();
                break;
        }
        return(fragment);
    }

    //Set the corresponding tab title for the pager
    @Override
    public String getPageTitle(int position) {
        String title="Error";
        switch (position){
            case 0:
                title = "DayView";
                break;
            case 1:
                title="WeekView";
                break;
        }
        return(title);
    }

}
