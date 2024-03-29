package ui.quillpeer.com.quillpeer.ui.people;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created on 18/11/2014.
 * This class is used as the custom page adapter for people's screen to control navigation using swipe view
 * @author Loucas Stylianou
 */
public class PeopleFragmentPageAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    /**
     * Custom constructor
     * @param ctxt
     * @param mgr
     */
    public PeopleFragmentPageAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }

    /**
     *  return number of tabs
     * @return
     */
    @Override
    public int getCount() {
        return(3);
    }

    /**
     * When the user swipes left or right the position changes hence
     * use this method to return the corresponding (fragment) screen to the user
     * @param position
     * @return
     */
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

    /**
     * Set the corresponding tab title for the pager
     * @param position
     * @return
     */
    @Override
    public String getPageTitle(int position) {
        String title="Error";
        switch (position){
            case 0:
                title = "Map";
                break;
            case 1:
                title="Suggested";
                break;
            case 2:
                title = "Participants";
                break;
        }
        return(title);
    }


}
