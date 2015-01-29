package ui.quillpeer.com.quillpeer.ui.people;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qozix.tileview.TileView;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 */
public class MapFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        TileView tileView = new TileView(getActivity());

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

/*        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ((LinearLayout) rootView).addView(tileView, lp);*/

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        ((LinearLayout) rootView).addView(tileView, lp);

        return rootView;
    }
}
