package ui.quillpeer.com.quillpeer.ui.people;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by loucas on 18/11/2014.
 * This class is used as the custom page adapter for people's screen to control navigation using swipe view
 */
public class PeopleFragmentPageAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    public PeopleFragmentPageAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }
    // return number of tabs
    @Override
    public int getCount() {
        return(3);
    }

    //When the user swipes left or right the position changes hence
    //use this method to return the corresponding (fragment) screen to the user
    @Override
    public Fragment getItem(int position) {
        //android.support.v4.app.Fragment fragment = null;
        switch (position){
            case 0:
                return new MapFragment();
            case 1:
                return new SuggestionsFragment();
            case 2:
                return new AllFragment();

        }
        return null;
    }

    //Set the corresponding tab title for the pager
    @Override
    public String getPageTitle(int position) {
        String title="Error";
        switch (position){
            case 0:
                title = "MapView";
                break;
            case 1:
                title="Suggestions";
                break;
            case 2:
                title = "All People";
                break;
        }
        return(title);
    }


}
