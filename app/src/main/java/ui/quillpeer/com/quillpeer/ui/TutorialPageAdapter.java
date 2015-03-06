package ui.quillpeer.com.quillpeer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 19/02/2015.
 */
public class TutorialPageAdapter extends FragmentPagerAdapter {

    public TutorialPageAdapter(FragmentManager mgr){
        super(mgr);
    }
    @Override
    public Fragment getItem(int position) {
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("pos",position);
        tutorialFragment.setArguments(bundle);
        return tutorialFragment;
    }

    @Override
    public int getCount() {
        return 8;
    }
}
