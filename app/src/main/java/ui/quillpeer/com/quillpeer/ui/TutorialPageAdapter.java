package ui.quillpeer.com.quillpeer.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by loucas on 19/02/2015.
 */
public class TutorialPageAdapter extends FragmentPagerAdapter {

    public TutorialPageAdapter(FragmentManager mgr){
        super(mgr);
    }
    @Override
    public Fragment getItem(int position) {
        return new TutorialFragment();
    }

    @Override
    public int getCount() {
        return 8;
    }
}
