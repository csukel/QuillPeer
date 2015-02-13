package ui.quillpeer.com.quillpeer.ui.people;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import core.FragmentLifecycle;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 */
public class SuggestionsFragment extends Fragment implements FragmentLifecycle {
    private Toast m_currentToast;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_suggestions, container, false);
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        //showToast("Suggestions onResume",Toast.LENGTH_SHORT);

    }

    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(getActivity().getApplicationContext(), text, toast_length);
        m_currentToast.show();

    }

    @Override
    public void onPauseFragment(FragmentActivity activity) {

    }

    @Override
    public void onResumeFragment(FragmentActivity activity) {

    }
}
