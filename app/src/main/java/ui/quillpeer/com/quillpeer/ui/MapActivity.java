package ui.quillpeer.com.quillpeer.ui;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.widget.LinearLayout;

import com.qozix.tileview.TileView;

import java.util.List;

import core.MapData;
import core.MapMarker;
import core.MyApplication;
import core.People.User;
import ui.quillpeer.com.quillpeer.ui.people.TileViewActivity;

/**
 * Created by loucas on 22/02/2015.
 */
public class MapActivity extends TileViewActivity {
    private TileView tileView;
    private Handler handlerUpdateMap;
    private static List<MapMarker> markerList;
    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        if (!MapData.haveMapSize){
            MapData.getMapSize();
        }else MapData.getRecommendation();
        // we'll reference the TileView multiple times
        tileView = getTileView();

        //tileView.setSize(width, height);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        tileView.setSize( size.x, size.y );
        tileView.addDetailLevel(1f, "1000_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.5f, "500_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.25f, "250_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(.125f, "125_%col%_%row%.png", "downsamples/map.png");

        tileView.setScaleLimits(0, 4);
        tileView.setScale(.5);

        // let's use 0-1 positioning...
        tileView.defineRelativeBounds( MapData.screenMaxX, MapData.screenMaxY,0, 0);
        // center markers along both axes
        tileView.setMarkerAnchorPoints( -0.5f, -0.5f );

        handlerUpdateMap = new Handler();
        handlerUpdateMap.postDelayed(runnableUpdateMap, 1000);

    }
    @Override
    public void onResume(){
        super.onResume();
        MyApplication.setCurrentActivity(this);
    }

    Runnable runnableUpdateMap = new Runnable() {
        @Override
        public void run() {
            if (MapData.haveMapSize){
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

            handlerUpdateMap.postDelayed(runnableUpdateMap,2000);
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        handlerUpdateMap.removeCallbacks(runnableUpdateMap);
    }


}
