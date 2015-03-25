package ui.quillpeer.com.quillpeer.ui.people;

import android.app.Activity;
import android.os.Bundle;

import com.qozix.tileview.TileView;

/**
 * This class defines the behaviour of the tile view which represents the map
 * Created on 22/02/2015.
 * @author Loucas Stylianou
 */
public class TileViewActivity extends Activity {

    /** tile view object */
    private TileView tileView;

    /** When this activity is created initialise the tile view */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        tileView = new TileView( this );
        setContentView( tileView );
    }

    /** On pause clear the tile view  */
    @Override
    public void onPause() {
        super.onPause();
        tileView.clear();
    }

    /**
     * On resume restart the tile view
     */
    @Override
    public void onResume() {
        super.onResume();
        tileView.resume();
    }

    /**
     * On destroy, destroy the tile view
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        tileView.destroy();
        tileView = null;
    }

    /**
     * Get the tile view instance
     * @return tileView
     */
    public TileView getTileView(){
        return tileView;
    }

    /**
     * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
     * see https://github.com/moagrius/TileView/wiki/FAQ
     */
    public void frameTo( final double x, final double y ) {
        getTileView().post( new Runnable() {
            @Override
            public void run() {
                getTileView().moveToAndCenter( x, y );
            }
        });
    }
}
