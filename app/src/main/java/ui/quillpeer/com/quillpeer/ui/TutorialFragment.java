package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private static final String TAG = TutorialFragment.class.getSimpleName();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        backgroundImg = (ImageView) rootView.findViewById(R.id.tutorial_img);
        int position = -1;
        try{
            getActivity().getActionBar().setSubtitle("Tutorial");
        }catch(NullPointerException nex){
            Log.e(TAG,nex.toString());
        }
        try {
            Bundle args = getArguments();
            position = args.getInt("pos");
        } catch (NullPointerException nex){
            Log.e(TAG,nex.toString());
        }



            int resource=-1;
            switch (position){
                case 0:
                    resource = R.drawable.tutorial1;
                    break;
                case 1:
                    resource = R.drawable.tutorial2;
                    break;
                case 2:
                    resource = R.drawable.tutorial3;
                    break;
                case 3:
                    resource = R.drawable.tutorial4;
                    break;
                case 4:
                    resource = R.drawable.tutorial5;
                    break;
                case 5:
                    resource  = R.drawable.tutorial6;
                    break;
                case 6:
                    resource = R.drawable.tutorial7;
                    break;
                case 7:
                    resource = R.drawable.tutorial8;
            }
            backgroundImg.setImageResource(resource);

        return rootView;
    }

    public TutorialFragment(){

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
