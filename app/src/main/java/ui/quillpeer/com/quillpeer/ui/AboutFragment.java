package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the About screen
 * Created by loucas on 18/11/2014.
 * @author Loucas Stylianou
 */
public class AboutFragment extends Fragment {
    /**
     * When this fragment's UI is initialised set the corresponding xml layout file
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        return rootView;
    }

    /**
     * This method determines the behaviour of the fragment when is attached to an activity
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }

}
