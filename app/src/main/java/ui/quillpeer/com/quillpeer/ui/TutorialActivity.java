package ui.quillpeer.com.quillpeer.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 19/02/2015.
 */
public class TutorialActivity extends FragmentActivity {
    private ViewPager mPager;
    private TutorialPageAdapter mAdapter;
    private PageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        mAdapter = new TutorialPageAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.tutorial_pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
