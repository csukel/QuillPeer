package ui.quillpeer.com.quillpeer.ui.people;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import core.FragmentLifecycle;
import core.ImageProcessing;
import core.MyApplication;
import core.People.OtherParticipant;
import core.People.Person;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.MainActivity;

import static android.widget.SearchView.OnQueryTextListener;

/**
 * Created by loucas on 18/11/2014.
 */
public class AllFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener, FragmentLifecycle {
    private static List<Person> peopleList;
    private AllPeopleAdapter allPeopleAdapter=null;
    //toast for displaying small messages to the user
    private Toast m_currentToast;
    private View rootView;
    private LinearLayoutManager llm;
    private int startIndex = 0;
    private int indexSearch = 0;
    private static final int numOfPeople = 10;
    //when user has retrieved or people this should be turn to true
    private boolean noMoreData = false;
    private boolean reset = false;
    private SuperRecyclerView recList;
    private boolean isVisible = false;
    private boolean searching = false;
    private String screenName = "AllFragment";
    private String queryString;
    private SearchView searchView;
    private MenuItem searchItem;
    private static final String TAG = AllFragment.class.getSimpleName();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_people_all, container, false);
        recList = (SuperRecyclerView)rootView.findViewById(R.id.cardListPeople);
        //recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        //populate list with fake data
        populateList();
        //initialise the adapter
        allPeopleAdapter = new AllPeopleAdapter(peopleList,getActivity().getApplicationContext(),screenName);
        //set the adapter to the recycler view
        recList.setAdapter(allPeopleAdapter);
        recList.setRefreshListener(this);
        recList.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        recList.setupMoreListener(this, 1);


        return rootView;
    }

    public void onPauseFragment(FragmentActivity activity) {

        Log.i(TAG,"onPauseFragment");
        //isVisible =false;
    }
    @Override
    public void onResume(){
        super.onResume();
        //TODO refresh the list and the UI
        Log.i("AllFragment","on resume");
/*        allPeopleAdapter = new AllPeopleAdapter(peopleList,getActivity().getApplicationContext());
        //set the adapter to the recycler view
        recList.setAdapter(allPeopleAdapter);*/
        allPeopleAdapter.notifyDataSetChanged();
        if (isVisible){

        }
    }

    @Override
    public void onResumeFragment(FragmentActivity activity) {
        Log.i("AllFragment","is visible");
        //isVisible = true;
    }



    private void populateList(){
        //instantiate the list
        peopleList = new ArrayList<Person>();
        //initially fetch 10 people data
        sendPostRequest(Integer.toString(startIndex),Integer.toString(numOfPeople),reset,"");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);
        //initialise the search menu item
        searchItem = menu.findItem(R.id.search);
        //get the action register to the above menu item
        searchView = (SearchView) searchItem.getActionView();
        //set a hint in the search input box
        searchView.setQueryHint("Search for people");
        //assign a query text lister to the menu item
        searchView.setOnQueryTextListener(onQueryTextChange);
        searchView.setIconifiedByDefault(true);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.i("AllFragment","search is closed");
                //check the network state and proceed if there is internet connection
                if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(),getActivity())){
                    reset = true;
                    //reset the list with the first bunch of people data
                    sendPostRequest("0",Integer.toString(numOfPeople),reset,"");
                }else {
                    allPeopleAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
    }

    private OnQueryTextListener onQueryTextChange = new OnQueryTextListener() {
        //when the user submits a query using the button
        @Override
        public boolean onQueryTextSubmit(String s) {
            Log.d("AllFragment","query submitted");
            //TODO send the query to the server
            if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(),getActivity())) {
                searching = true;
                indexSearch =0;
                peopleList.clear();
                queryString = s;
                sendPostRequest(Integer.toString(indexSearch), Integer.toString(numOfPeople), false, s);
            }
            return false;
        }
        //when the query text changes ....
        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };



    private void sendPostRequest(String begin, String size, final boolean reSet,String searchQuery) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            private List<Person> backupList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (reSet) {
                    backupList = peopleList;
                    peopleList.clear();
                    try {
                        allPeopleAdapter.notifyDataSetChanged();
                    } catch (NullPointerException nex) {
                        nex.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {

                String paramStart = params[0];
                String paramSize = params[1];
                String paramSearchQuery = params[2];
                return ServerComm.getPeople(paramStart, paramSize,paramSearchQuery);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject jsonObject=null;
                boolean outcome = false;
                try {
                    if (result != null) {
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");
                        JSONArray jsonArray = jsonObject.getJSONArray("users");

/*                        if (jsonArray.length() == 0)
                            noMoreData = true;*/

                        if (reSet) {
                            peopleList.clear();
                            reset = false;
                            searching = false;
                            //recList.hideProgress();
                            startIndex =0;
                            noMoreData = false;
                        }
                        if (searching){
                            if (indexSearch == 0)
                                noMoreData = false;
                            indexSearch +=numOfPeople;
                        } else{
                            indexSearch = 0;
                        }
                        int remaining = Integer.valueOf(jsonObject.getString("remaining"));
                        if (remaining == 0){
                            noMoreData = true;
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.get(i) instanceof JSONObject) {
                                JSONObject ob = (JSONObject) jsonArray.get(i);
                                OtherParticipant opart = new OtherParticipant(ob.getString("id"), ob.getString("prefix"), ob.getString("first_name"),
                                        ob.getString("last_name"), ob.getString("university"), ob.getString("department"), ob.getString("email"),
                                        ob.getString("is_speaker").contains("1"), ob.getBoolean("favourite"), ob.getString("qualification"));
                                String imageStream = ob.getString("picture");
                                if (!imageStream.equals("null")) {
                                    Bitmap bp = ImageProcessing.decodeImage(ob.getString("picture"));
                                    opart.setProfilePicture(bp);
                                }
                                peopleList.add(opart);
                            }

                        }
                        //increment index for the next time of querying the server
                        if (!searching)
                            startIndex += numOfPeople;
                    }else {
                        if(reset) {
                            peopleList = backupList;
                            try {
                                allPeopleAdapter.notifyDataSetChanged();
                            } catch (NullPointerException nex) {
                                nex.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (outcome && !noMoreData){
                    recList.hideMoreProgress();
                    allPeopleAdapter.notifyDataSetChanged();
/*                    int lastVisibleItem = ((LinearLayoutManager) llm).findLastVisibleItemPosition();
                    recList.smoothScrollToPosition(lastVisibleItem+3);*/
                }
                else if (noMoreData){
                    allPeopleAdapter.notifyDataSetChanged();
                    recList.hideMoreProgress();
                    //showToast("There are no more people to show...",Toast.LENGTH_SHORT);
                }
                else {
                    //mSwipeRefreshLayout.setRefreshing(false);
                    showToast("Fetching data failed...",Toast.LENGTH_SHORT);
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(begin, size,searchQuery);



    }
    //show toasts
    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }

        Context context = getActivity().getApplicationContext();
        if (context!=null){
            m_currentToast = Toast.makeText(context, text,toast_length);
            m_currentToast.show();
        }


    }

/*    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }*/
    @Override
    public void setUserVisibleHint(final boolean visibleHint){
        super.setUserVisibleHint(visibleHint);
        if (visibleHint){
            Log.i(TAG,"visible hint");
            isVisible = true;
/*            if (searching) {
                //searchView.setIconified(true);
                //searchView.onActionViewCollapsed();
                //searchItem.expandActionView();
                //MenuItemCompat.expandActionView(searchItem);

                //searchView.onActionViewExpanded();
                //searchView.setQuery(queryString,false);
               // searchView.invalidate();
            }*/
        }else {
            Log.i(TAG,"not visible hint");
            //if the user was on searching the last time visited this screen then reset the list
            if (searching) {
                resetList();
            }
            isVisible = false;
        }
    }

    @Override
    public void onRefresh() {
        resetList();

    }

    private void resetList() {
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(),getActivity())){
            reset = true;

            sendPostRequest("0",Integer.toString(numOfPeople),reset,"");
        }else {
            allPeopleAdapter.notifyDataSetChanged();
            //showToast("Check your internet connection...",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onMoreAsked(int i, int i2, int i3) {

        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(),getActivity())){
            if (!noMoreData){
                //send a post request to the server to query for a number of people
                if (!searching) {
                    sendPostRequest(Integer.toString(startIndex), Integer.toString(numOfPeople), reset, "");
                }else {
                    sendPostRequest(Integer.toString(indexSearch), Integer.toString(numOfPeople), reset,queryString);
                }

            } else{
                recList.hideMoreProgress();
            }
        }else {
            recList.hideMoreProgress();
            allPeopleAdapter.notifyDataSetChanged();
            //showToast("Check your internet connection...", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("AllFragment","menu item was pressed");
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button

            case android.R.id.home:
                Log.i("AllFragment","home button pressed");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static List<Person> getPeopleList(){
        return peopleList;
    }


/*    @Override
    public void onBackPressed() {

    }*/
}
