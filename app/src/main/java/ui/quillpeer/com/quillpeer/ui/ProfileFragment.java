package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import core.MyApplication;
import core.People.User;
import ui.quillpeer.com.quillpeer.R;


/**
 * Created by loucas on 18/11/2014.
 */
public class ProfileFragment extends Fragment {
    private ImageView profilePicture;
    private TextView profileName;
    private TextView profileUniversity;
    private TextView profileDepartment;
    private TextView profileQualification;
    private TextView profilePaperAbstract;
    private TextView profilePaperAbstractTitle;
    private ImageView profileFavourite;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_cardview, container, false);

        initializeViewResources(rootView);
        return rootView;
    }

    //connect objects to corresponding xml resources
    private void initializeViewResources(View v) {
        User user = User.getInstance();
        profilePicture = (ImageView)v.findViewById(R.id.imgCardProfilePicture);
        try {
            profilePicture.setImageBitmap(user.getProfilePicture());
        }catch(NullPointerException nex){
            Log.e(TAG,nex.toString());
        }
        profilePicture.setOnTouchListener(profilePicTouchListener);

        profileName = (TextView)v.findViewById(R.id.txtCardProfileName);
        profileName.setText(user.getTitle() + " " + user.getName() + " " + user.getSurname());
        profileUniversity = (TextView)v.findViewById(R.id.txtCardProfileUniversity);
        profileUniversity.setText(user.getUniversity());
        profileDepartment = (TextView)v.findViewById(R.id.txtCardProfileDepartment);
        profileDepartment.setText(user.getDepartment());
        profileQualification = (TextView)v.findViewById(R.id.txtCardProfileQualification);
        profileQualification.setText(user.getQualification());
        profilePaperAbstract = (TextView)v.findViewById(R.id.txtCardProfilePaperAbstract);
        profilePaperAbstract.setText(user.getPaperAbstract());
        profilePaperAbstractTitle = (TextView)v.findViewById(R.id.txtCardProfilePaperAbstractTitle);
        profilePaperAbstractTitle.setText(user.getPaperAbstractTitle());
        profileFavourite = (ImageView)v.findViewById(R.id.imgCardProfileFavourite);
        //remove the profile favourit view
        ((LinearLayout)profileFavourite.getParent()).removeView(profileFavourite);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }

    View.OnTouchListener profilePicTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent intent = new Intent(MyApplication.currentActivity(),TakePicActivity.class);
            startActivity(intent);
            return false;
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        profilePicture.setImageBitmap(User.getInstance().getProfilePicture());
    }
}
