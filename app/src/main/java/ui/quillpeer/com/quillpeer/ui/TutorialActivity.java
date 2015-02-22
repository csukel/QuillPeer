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
 * Created by loucas on 19/02/2015.
 */
public class TutorialActivity extends FragmentActivity {
    private ViewPager mPager;
    private TutorialPageAdapter mAdapter;
    private PageIndicator mIndicator;
    private MenuItem skipTutorial;

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

    //instantiate a page change listener
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        //when a page is scrolled then do
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get the menu inflater and connect it to the corresponding menu xml file(tutorial.xml under the menu)
        getMenuInflater().inflate(R.menu.tutorial, menu);
        //instantiate the skip menu item
        skipTutorial = menu.findItem(R.id.action_skip);
        return super.onCreateOptionsMenu(menu);
    }

    //when a menu item is selected do...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //if skip menu item is pressed do ..
            case R.id.action_skip:
                //TODO if last activity was the splash screen then by pressing skip proceed to login screen
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
