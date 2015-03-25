package ui.quillpeer.com.quillpeer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the adapter which is used for the Tutorial slide show.
 * Created by loucas on 19/02/2015.
 * @author Loucas Stylianou
 */
public class TutorialPageAdapter extends FragmentPagerAdapter {

    /**
     * The default constructor
     * @param mgr
     */
    public TutorialPageAdapter(FragmentManager mgr){
        super(mgr);
    }

    /**
     * When an item is needed to be retrieved store its position to a bundle of arguments. This is very important part
     * of the logic for the slideshow cause it is used to recognised which image should be displayed while the user
     * swipes left or right.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("pos",position);
        tutorialFragment.setArguments(bundle);
        return tutorialFragment;
    }

    /**
     * Get the number of the slides/screens.
     * @return size
     */
    @Override
    public int getCount() {
        return 8;
    }
}
