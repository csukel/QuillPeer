package core;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import core.People.Person;
import core.People.User;
import de.hdodenhof.circleimageview.CircleImageView;
import ui.quillpeer.com.quillpeer.R;

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
        markerView.setScaleX(0.75f);
        markerView.setScaleY(0.75f);
        markerView.setBorderColor(Color.parseColor("#9B30FF"));
        markerView.setBorderWidth(15);
    }
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
}
