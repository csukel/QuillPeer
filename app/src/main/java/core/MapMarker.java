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
    /**
     * The class name
     */
    private static final String TAG = MapMarker.class.getSimpleName();
    /**
     * The person related to the MapMarker object
     */
    private Person person;
    /**
     * The context in which this class will be instantiated
     */
    private Context context;
    /**
     * The UI element which will depict the MapMarker. Circular image
     */
    private CircleImageView markerView;
    /**
     * The x coordinate
     */
    private double x =0.0;
    /**
     * The y coordinate
     */
    private double y=0.0;
    /**
     * Info message
     */
    private Toast m_currentToast;
    /**
     * Screen name
     */
    private String screenName="map";

    /**
     * Class constructor responsible for the instantiation and initialisation of a MapMarker object
     * @param context The context in which the corresponding MapMarker object is created
     * @param p The person object which is related to this MapMarker
     * @param xcoor The x coordinate
     * @param ycoor The y coordinate
     */
    public MapMarker(Context context,Person p,double xcoor,double ycoor ){
        this.person =p;
        this.context = context;
        this.x = xcoor;
        this.y = ycoor;
        createMarkerView(context);
    }

    /**
     * Configure the view of the corresponding marker
     * @param context The context in which the MapMarker object is instantiated
     */
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
        else if (person instanceof User) {
            //markerView.setOnClickListener(userMarkerClickListener);
            markerView.setBorderColor(Color.parseColor("#007373"));
        }


    }

    /*when a marker is clicked go to the corresponding person profile page*/
    /**
     * Handles click events for the marker
     */
    View.OnClickListener markerOnClickListener = new View.OnClickListener() {
        /**
         * When the user clicks on a marker, the application loads the profile page of the corresponding person
         * @param v The view of the UI element which is clicked by the user
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MyApplication.currentActivity(), PersonProfileActivity.class);
            intent.putExtra("person_id", ((OtherParticipant) person).getUserId());
            intent.putExtra("fragment",screenName);
            context.startActivity(intent);
        }
    };

/*
    View.OnClickListener userMarkerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = "x:"+getX()+"\ny:"+getY()+"\nb1:"+Beacons.getDist1()+"\nb2:"+Beacons.getDist2()+
                    "\nb3:"+Beacons.getDist3()+"\nb4:"+Beacons.getDist4()+"\nb5:"+Beacons.getDist5()+"\nb6:"+Beacons.getDist6();
            showToast(v.getContext(),msg,Toast.LENGTH_LONG);
        }
    };
*/

    /**
     * Get the person which is mapped to this marker object
     * @return person object
     */
    public Person getPerson(){
        return this.person;
    }

    /**
     * Get the x coordinate
     * @return X coordinate
     */
    public double getX(){
        return this.x;
    }

    /**
     * Get the y coordinate
     * @return y coordinate
     */
    public double getY(){
        return this.y;
    }

    /**
     * Get the UI element which is mapped to the MapMarker object
     * @return CircleImageView
     */
    public CircleImageView getMarkerView(){
        return this.markerView;
    }

    /**
     * Display info message to the user
     * @param context The context in which the Toast object is instantiated
     * @param text The message which will be displayed
     * @param toast_length The duration of the message that will be displayed
     */
    void showToast(Context context, String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(MyApplication.getAppContext(), text, toast_length);
        m_currentToast.show();

    }
}
