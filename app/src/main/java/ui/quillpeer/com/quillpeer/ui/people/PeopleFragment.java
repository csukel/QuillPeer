package ui.quillpeer.com.quillpeer.ui.people;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import core.FragmentLifecycle;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.MainActivity;
import ui.quillpeer.com.quillpeer.ui.people.PeopleFragmentPageAdapter;

/**
 * Created by loucas on 18/11/2014.
 */
public class PeopleFragment extends Fragment {
    private ViewPager pager;
    private PeopleFragmentPageAdapter pagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.people_pager, container, false);
        pager=(ViewPager)rootView.findViewById(R.id.people_pager);
        //Set the number of pages that should be retained to either side of the current page in the view hierarchy in an idle state
        pager.setOffscreenPageLimit(2);
        pagerAdapter = new PeopleFragmentPageAdapter(getActivity(),getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int newPosition, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {
                FragmentLifecycle fragmentToShow = (FragmentLifecycle)pagerAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment(getActivity());

                FragmentLifecycle fragmentToHide = (FragmentLifecycle)pagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment(getActivity());

                currentPosition = newPosition;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageSelected(int newPosition) {


        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        public void onPageScrollStateChanged(int arg0) { }
    };
}
