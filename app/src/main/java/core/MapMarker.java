package core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import core.People.OtherParticipant;
import core.People.Person;
import core.People.User;
import de.hdodenhof.circleimageview.CircleImageView;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.people.PersonProfileActivity;

/**
 * Created by loucas on 01/03/2015.
 */
public class MapMarker {
    private static final String TAG = MapMarker.class.getSimpleName();
    private Person person;
    private Context context;
    private CircleImageView markerView;
    private double x =0.0;
    private double y=0.0;
    private Toast m_currentToast;
    private String screenName="map";

    public MapMarker(Context context,Person p,double xcoor,double ycoor ){
        this.person =p;
        this.context = context;
        this.x = xcoor;
        this.y = ycoor;
        createMarkerView(context);
    }

    private void createMarkerView(Context context){
        this.markerView = new CircleImageView(context);

        try {
            if (getPerson().getProfilePicture()!=null) {
                markerView.setImageBitmap(getPerson().getProfilePicture());
            }else markerView.setImageResource(R.drawable.ic_action_person);
        }catch (NullPointerException nex){
            Log.e(TAG,nex.toString());
        }
/*        markerView.setScaleX(0.75f);
        markerView.setScaleY(0.75f);*/
        markerView.setScaleX(0.60f);
        markerView.setScaleY(0.60f);
        markerView.setBorderColor(Color.parseColor("#9B30FF"));
        markerView.setBorderWidth(15);
        /*user can only click on other people markers*/
        if (person instanceof OtherParticipant)
            markerView.setOnClickListener(markerOnClickListener);
        else if (person instanceof User)
            markerView.setOnClickListener(userMarkerClickListener);
    }

    /*when a marker is clicked go to the corresponding person profile page*/
    View.OnClickListener markerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MyApplication.currentActivity(), PersonProfileActivity.class);
            intent.putExtra("person_id", ((OtherParticipant) person).getUserId());
            intent.putExtra("fragment",screenName);
            context.startActivity(intent);
        }
    };

    //TODO click on marker corresponding to user-testing purposes-should be removed later on
    View.OnClickListener userMarkerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = "x:"+getX()+"\ny:"+getY()+"\nb1:"+Beacons.getDist1()+"\nb2:"+Beacons.getDist2()+
                    "\nb3:"+Beacons.getDist3()+"\nb4:"+Beacons.getDist4()+"\nb5:"+Beacons.getDist5()+"\nb6:"+Beacons.getDist6();
            showToast(v.getContext(),msg,Toast.LENGTH_LONG);
        }
    };

    /*get the person object*/
    public Person getPerson(){
        return this.person;
    }
    /*get x coordinate*/
    public double getX(){
        return this.x;
    }
    /*get y coordinate*/
    public double getY(){
        return this.y;
    }
    /*return the image view*/
    public CircleImageView getMarkerView(){
        return this.markerView;
    }

    void showToast(Context context, String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(context, text, toast_length);
        m_currentToast.show();

    }
}
