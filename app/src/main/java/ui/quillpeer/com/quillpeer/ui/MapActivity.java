package ui.quillpeer.com.quillpeer.ui;

import android.os.Bundle;

import com.qozix.tileview.TileView;

import ui.quillpeer.com.quillpeer.ui.people.TileViewActivity;

/**
 * Created by loucas on 22/02/2015.
 */
public class MapActivity extends TileViewActivity {
    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        // we'll reference the TileView multiple times
        TileView tileView = getTileView();

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


    }

}
