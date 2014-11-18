package ui.quillpeer.com.quillpeer.ui.people;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by loucas on 18/11/2014.
 */
public class PeopleFragmentPageAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    public PeopleFragmentPageAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        android.support.v4.app.Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new MapFragment();
                break;
            case 1:
                fragment = new SuggestionsFragment();
                break;
            case 2:
                fragment = new AllFragment();
                break;
        }
        return(fragment);
    }

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
