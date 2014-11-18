package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.people.PeopleFragmentPageAdapter;

/**
 * Created by loucas on 18/11/2014.
 */
public class PeopleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.people_pager, container, false);
        ViewPager pager=(ViewPager)rootView.findViewById(R.id.people_pager);
        pager.setAdapter(buildAdapter());
        return rootView;
    }

    private PagerAdapter buildAdapter() {
        return (new PeopleFragmentPageAdapter(getActivity(),getChildFragmentManager()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }
}
