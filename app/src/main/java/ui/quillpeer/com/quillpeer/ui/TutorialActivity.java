package ui.quillpeer.com.quillpeer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the Tutorial slideshow screen
 * Created by loucas on 19/02/2015.
 * @author Loucas Stylianou
 */
public class TutorialActivity extends FragmentActivity {
    /** view pager for the slideshow */
    private ViewPager mPager;
    /** page adapter */
    private TutorialPageAdapter mAdapter;
    /** page indicator which is displayed on the bottom of this screen */
    private PageIndicator mIndicator;
    /** menu item for skip/proceed action */
    private MenuItem skipTutorial;

    /**
     * Initialise the UI of the screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the screen UI
        setContentView(R.layout.tutorial);
        //instantiate the page adapter
        mAdapter = new TutorialPageAdapter(getSupportFragmentManager());
        //instantiate the view pager
        mPager = (ViewPager) findViewById(R.id.tutorial_pager);
        //set the adapter to the view pager
        mPager.setAdapter(mAdapter);
        //set and indicator with circles at the bottom of the page
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        //set a page change listener
        mIndicator.setOnPageChangeListener(pageChangeListener);
        //bind the indicator to the pager
        mIndicator.setViewPager(mPager);
    }

    /** instantiate a page change listener */
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        /**
         * When is the last page then change the skip button text to proceed.
         * @param position
         * @param positionOffset
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //if the visible page is the last one ...
            if (position == mAdapter.getCount()-1){
                //change the skip item title to proceed
                skipTutorial.setTitle("Proceed");
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * When the options menu is created initialise the options.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get the menu inflater and connect it to the corresponding menu xml file(tutorial.xml under the menu)
        getMenuInflater().inflate(R.menu.tutorial, menu);
        //instantiate the skip menu item
        skipTutorial = menu.findItem(R.id.action_skip);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method defines the behaviour of this screen when an option is selected from the options menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //if skip menu item is pressed do ..
            case R.id.action_skip:
                Intent prevIntent = getIntent();
                if (prevIntent.getStringExtra("class")!=null && prevIntent.getStringExtra("class").equals("SplashActivity")){
                    Intent intent = new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
