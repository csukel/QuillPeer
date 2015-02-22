package ui.quillpeer.com.quillpeer.ui.people;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.devspark.appmsg.AppMsg;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.qozix.tileview.TileView;

import core.FragmentLifecycle;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.MapActivity;

/**
 * Created by loucas on 18/11/2014.
 */
public class MapFragment extends Fragment implements FragmentLifecycle {
    private TileView tileView;
    private LinearLayout mapLayout;
    private Handler handlerInfoMsg;
    private AppMsg appMsg;
    private boolean visible = false;
    private static final String TAG = MapFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        tileView = new TileView(getActivity());

        int width = 256 * 10;
        int height = 256 * 7 + 144;

        //tileView.setSize(width, height);
        tileView.setSize( 3090, 2536 );
        tileView.addDetailLevel(1f, "1000_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.5f, "500_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.25f, "250_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.125f, "125_%col%_%row%.png", "downsamples/map.png");

        tileView.setScaleLimits(0, 4);
        tileView.setScale(.5);

        // let's use 0-1 positioning...
        tileView.defineRelativeBounds( 0, 0, 10,  10 );
        // center markers along both axes
        tileView.setMarkerAnchorPoints( -0.5f, -0.5f );

/*        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ((LinearLayout) rootView).addView(tileView, lp);*/

        mapLayout = (LinearLayout)rootView.findViewById(R.id.maplayout);
/*        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        ((LinearLayout) rootView).addView(tileView, lp);*/
        //mapLayout.addView(tileView);
        mapLayout.addView(tileView,0);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(fullScreenOnClickListener);

        // add some pins...
        addPin(5, 5);

        handlerInfoMsg = new Handler();
        handlerInfoMsg.postDelayed(runnableMsgInfo,200);

        return rootView;
    }

    View.OnClickListener fullScreenOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG,"full screen is clicked");
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        }
    };

    //show msg if the fragment is visible
    Runnable runnableMsgInfo = new Runnable() {
        @Override
        public void run() {
            if (visible) {
                final AppMsg.Style style = AppMsg.STYLE_INFO;
                final CharSequence msg = "To enjoy full functionality of the map please tap on the full screen button!";
                try {
                    appMsg = AppMsg.makeText(getActivity(), msg, style);
                    appMsg.setLayoutGravity(Gravity.BOTTOM);
                    appMsg.show();
                }catch (NullPointerException nex){
                    Log.e(TAG,nex.toString());
                }

            }
            handlerInfoMsg.postDelayed(runnableMsgInfo, 50);
        }
    };

    private void addPin( double x, double y ) {
        ImageView imageView = new ImageView(getActivity() );
        imageView.setImageResource( R.drawable.maps_marker_blue);
        tileView.addMarker(imageView, x, y);

    }


    @Override
    public void onPauseFragment(FragmentActivity activity) {

    }

    @Override
    public void onResumeFragment(FragmentActivity activity) {

    }

    //check the fragment's visibility
    @Override
    public void setUserVisibleHint(final boolean visibleHint){
        super.setUserVisibleHint(visibleHint);
        if (visibleHint){
            Log.i(TAG, "visible hint");
            visible = true;

        }else {
            Log.i(TAG,"not visible hint");
            if (appMsg!=null)
                //cancel the msg
                appMsg.cancel();
            visible = false;
        }
    }
}
