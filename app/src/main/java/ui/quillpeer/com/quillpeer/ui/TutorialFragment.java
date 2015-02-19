package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.nio.BufferUnderflowException;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 19/02/2015.
 */
public class TutorialFragment extends Fragment {
    private ImageView backgroundImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        try{
            getActivity().getActionBar().setSubtitle("Tutorial");
        }catch(NullPointerException nex){
            nex.printStackTrace();
        }
        backgroundImg = (ImageView)rootView.findViewById(R.id.tutorial_img);
        backgroundImg.setImageResource(R.drawable.ic_launcher);
        return rootView;
    }

    public TutorialFragment(){

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
