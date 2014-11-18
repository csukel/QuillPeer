package ui.quillpeer.com.quillpeer.ui.timetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 */
public class WeekView extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable_weekview, container, false);

        return rootView;
    }

}
