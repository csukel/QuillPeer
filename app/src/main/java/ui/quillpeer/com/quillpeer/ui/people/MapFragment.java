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

import java.util.List;

import core.FragmentLifecycle;
import core.MapData;
import core.MapMarker;
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
    private Handler handlerUpdateMap;
    private static List<MapMarker> markerList;
    private boolean visible = false;
    private static final String TAG = MapFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        MapData.getMapSize();
        tileView = new TileView(getActivity());

        //tileView.setSize(width, height);
        tileView.setSize( 2536, 2536 );
        tileView.addDetailLevel(1f, "1000_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.5f, "500_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.25f, "250_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.125f, "125_%col%_%row%.png", "downsamples/map.png");

        tileView.setScaleLimits(0.5, 0.5);
        tileView.setScale(0.5);

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


/*        handlerInfoMsg = new Handler();
        handlerInfoMsg.postDelayed(runnableMsgInfo,200);

        handlerUpdateMap = new Handler();
        handlerUpdateMap.postDelayed(runnableUpdateMap, 100);*/

        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        handlerInfoMsg = new Handler();
        handlerInfoMsg.postDelayed(runnableMsgInfo,200);

        handlerUpdateMap = new Handler();
        handlerUpdateMap.postDelayed(runnableUpdateMap, 100);
    }
    @Override
    public void onStop(){
        super.onStop();
        handlerInfoMsg.removeCallbacks(runnableMsgInfo);
        handlerUpdateMap.removeCallbacks(runnableUpdateMap);
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        //prevent runnable from running again after this fragment is destroyed
        handlerInfoMsg.removeCallbacks(runnableMsgInfo);
        /*when this screen is destroyed remove runnable update map from handler to avoid running forever*/
        handlerUpdateMap.removeCallbacks(runnableUpdateMap);
    }

    Runnable runnableUpdateMap = new Runnable() {
        @Override
        public void run() {
            if (MapData.haveMapSize){
                tileView.defineRelativeBounds( 0, 0,MapData.getMaxX(), MapData.getMaxY());
                if ( !MapData.isUpdating() && MapData.getMarkerList()!=null){
                    //TODO get data from list and show the markers
                    updateMap();
                    MapData.getRecommendation();
                }else if (!MapData.isUpdating()) {
                    MapData.getRecommendation();
                }
            }else {
                MapData.getMapSize();
            }

            handlerUpdateMap.postDelayed(runnableUpdateMap,3000);
        }
    };

    /*update the map with adding removing markers*/
    private void updateMap() {
        /*is not null then remove every existing marker on the map*/
        if (markerList!=null){
            for (MapMarker marker: markerList ){
                tileView.removeMarker(marker.getMarkerView());
            }

        }
        /*add the new markers on the map*/
        markerList = MapData.getMarkerList();
        for (MapMarker marker: markerList){
            tileView.addMarker(marker.getMarkerView(),marker.getX(),marker.getY());
        }
        tileView.refresh();

    }

}
