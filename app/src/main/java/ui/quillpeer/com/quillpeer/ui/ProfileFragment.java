package ui.quillpeer.com.quillpeer.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.List;

import core.MyApplication;
import core.People.Topic;
import core.People.User;
import ui.quillpeer.com.quillpeer.R;


/**
 * This class defines the behaviour of the user's profile page
 * Created by loucas on 18/11/2014.
 * @author Loucas Stylianou
 */
public class ProfileFragment extends Fragment {
    /** profile picture image view */
    private ImageView profilePicture;
    /** first and last name */
    private TextView profileName;
    /** user's university */
    private TextView profileUniversity;
    /** user's department */
    private TextView profileDepartment;
    /** user's academic qualification */
    private TextView profileQualification;
    /** user's submitted abstract */
    private TextView profilePaperAbstract;
    /** title of user's submitted abstract */
    private TextView profilePaperAbstractTitle;
    /** favourite image view. for this screen is removed from its parent view since it is not needed */
    private ImageView profileFavourite;
    /** the horizontal bar chart which displays the top 5 topics for the user according to his submitted abstract */
    private HorizontalBarChart topicsChart;
    private LinearLayout topicWordsLayout;
    /** set of words which represents a specific topic */
    private TextView txtTopicWords;
    /** the class name which is used for debugging/testing purposes */
    private static final String TAG = ProfileFragment.class.getSimpleName();

    /**
     * This method initialises the view for this screen.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
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
        //if there are data available from topic model then draw the graph

        topicsChart = (HorizontalBarChart) v.findViewById(R.id.topicHorizontalChart);
        topicWordsLayout = (LinearLayout) v.findViewById(R.id.topicWordsLayout);
        topicWordsLayout.setVisibility(View.INVISIBLE);
        txtTopicWords = (TextView) v.findViewById(R.id.txtTopicWords);
        if (!User.getInstance().getTopicList().isEmpty()) {
            initialiseChartView(v);
        }else {

        }
        //remove the profile favourit view
        ((LinearLayout)profileFavourite.getParent()).removeView(profileFavourite);
    }

    /**
     * Initialise the view for the horizontal bar chart that displays the top 5 topics
     * @param v
     */
    private void initialiseChartView(View v) {

        topicsChart.setOnChartValueSelectedListener(topicChartValueSelectedListener);
        // mChart.setHighlightEnabled(false);
        topicsChart.setContentDescription("skjflkjhklsfd");
        topicsChart.setDrawBarShadow(false);

        topicsChart.setDrawValueAboveBar(true);

        topicsChart.setDescription("");


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        topicsChart.setMaxVisibleValueCount(6);

        // scaling can now only be done on x- and y-axis separately
        topicsChart.setPinchZoom(true);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        //topicsChart.setDrawXLabels(true);

        topicsChart.setDrawGridBackground(false);

        // mChart.setDrawYLabels(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = topicsChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);
        // xl.setEnabled(false);

        YAxis yl = topicsChart.getAxisLeft();
        //yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);

        YAxis yr = topicsChart.getAxisRight();
        //yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

        setData(tf);
        topicsChart.animateY(2500);

    }

    /**
     * Set the data to the bar chart
     * @param tf
     */
    private void setData(Typeface tf) {
        List<Topic> topicList = User.getInstance().getTopicList();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < topicList.size(); i++) {
            String xval = topicList.get(i).getTitle();
            xVals.add("Topic " + (i+1));
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < topicList.size(); i++) {
            float val = (float) (topicList.get(i).getWeight())*1000;
            yVals1.add(new BarEntry(val, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Match Rating");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        //data.setValueTypeface(tf);

        topicsChart.setData(data);
        topicsChart.animateY(2500);
        Legend l = topicsChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

    }

    /**
     * When a bar is selected from the chart ....
     */
    OnChartValueSelectedListener topicChartValueSelectedListener = new OnChartValueSelectedListener() {
        /**
         * Display the corresponding set of words which represents the selected topic
         * @param e
         * @param dataSetIndex
         * @param h
         */
        @SuppressLint("NewApi")
        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            try {
                topicWordsLayout.setVisibility(View.VISIBLE);
                txtTopicWords.setText(User.getInstance().getTopicList().get(e.getXIndex()).getTitle().toString());
            } catch (Exception ex){
                Log.e(TAG,ex.toString());
            }
        }

        @Override
        public void onNothingSelected() {

        }
    };

    /**
     * This method defines the fragment's behaviour when it is attached to an activity
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }

    /**
     * This listener defines the behaviour when a user clicks on his profile picture
     */
    View.OnTouchListener profilePicTouchListener = new View.OnTouchListener(){

        /**
         * When the user touches his profile picture, then it launches the screen which is responsible for taking a new profile picture
         * @param v
         * @param event
         * @return
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent intent = new Intent(MyApplication.currentActivity(),TakePicActivity.class);
            startActivity(intent);
            return false;
        }
    };

    /**
     * When the fragment enters the onResume state update the profile picture
     */
    @Override
    public void onResume(){
        super.onResume();
        profilePicture.setImageBitmap(User.getInstance().getProfilePicture());
    }
}
