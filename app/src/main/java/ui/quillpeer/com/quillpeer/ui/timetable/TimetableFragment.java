package ui.quillpeer.com.quillpeer.ui.timetable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.MainActivity;

/**
 * Created by loucas on 18/11/2014.
 * This will be the screen for Timetable
 */
public class TimetableFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timetable_pager, container, false);
        ViewPager pager = (ViewPager)rootView.findViewById(R.id.timetable_pager);
        pager.setAdapter(buildAdapter());
        return rootView;
    }

    private PagerAdapter buildAdapter() {
        return (new TimetableFragmentPageAdapter(getActivity(),getChildFragmentManager()));
    }

    //updates the ActionBar title by using the onSectionAttached method from MainActivity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //getArguments method is used to get arguments attached to the fragment from MainActivity
        ((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }
}
