package ui.quillpeer.com.quillpeer.ui.people;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.library21.custom.SwipeRefreshLayoutBottom;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import core.ImageProcessing;
import core.People.OtherParticipant;
import core.People.Person;
import core.People.User;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;
import ui.quillpeer.com.quillpeer.ui.TakePicActivity;

import static android.widget.SearchView.OnQueryTextListener;

/**
 * Created by loucas on 18/11/2014.
 */
public class AllFragment extends Fragment implements SwipeRefreshLayoutBottom.OnRefreshListener {
    static List<Person> peopleList;
    private AllPeopleAdapter allPeopleAdapter=null;
    private String searchFilter="";
    //Set search filter criteria in a string array
    private String[] searchCriteriaList;
    //toast for displaying small messages to the user
    private Toast m_currentToast;
    private View rootView;
    private LinearLayoutManager llm;
    private SwipeRefreshLayoutBottom mSwipeRefreshLayout;
    private FrameLayout mRootLayout;
    private Handler mHandler;
    private int startIndex = 0;
    private static final int numOfPeople = 10;
    private RecyclerView recList;
    //when user has retrieved or people this should be turn to true
    private boolean noMoreData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_people_all, container, false);
        recList = new RecyclerView(getActivity());
        //recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mSwipeRefreshLayout = new SwipeRefreshLayoutBottom(getActivity());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mHandler = new Handler();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.yellow, R.color.blue);
        mSwipeRefreshLayout.addView(recList);
        ((LinearLayout) rootView).addView(mSwipeRefreshLayout);

        //set a scroll listener in order to know when we need to load more data
        //recList.setOnScrollListener(rcOnScrollListener);
        //allow the fragment to invalidate options menu
        getActivity().supportInvalidateOptionsMenu();
        //allow fragment to have a menu
        setHasOptionsMenu(true);
        //populate list with fake data
        populateList();
        //initialise the adapter
        allPeopleAdapter = new AllPeopleAdapter(peopleList,getActivity().getApplicationContext());
        //set the adapter to the recycler view
        recList.setAdapter(allPeopleAdapter);
        //initialise the search filtering options list
        searchCriteriaList= new String[]{getResources().getString(R.string.sfName),getResources().getString(R.string.sfSurname),
                getResources().getString(R.string.sfUniversity),getResources().getString(R.string.sfDepartment),getResources().getString(R.string.sfQualification)};

        //showToast("ALLFragment Created",Toast.LENGTH_SHORT);

        return rootView;
    }



    private void populateList(){
        //instantiate the list
        peopleList = new ArrayList<Person>();
        //initially fetch 10 people data
        sendPostRequest(Integer.toString(startIndex),Integer.toString(numOfPeople));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);
        //initialise the search menu item
        MenuItem searchItem = menu.findItem(R.id.search);
        //get the action register to the above menu item
        SearchView searchView = (SearchView) searchItem.getActionView();
        //set a hint in the search input box
        searchView.setQueryHint("Search for people");
        //assign a query text lister to the menu item
        searchView.setOnQueryTextListener(onQueryTextChange);
        searchView.setIconifiedByDefault(false);

        //get the spinner menu item
        MenuItem spinnerItem = menu.findItem(R.id.search_spinner);
        //spinner instantiation
        Spinner spinner = (Spinner)spinnerItem.getActionView();

        //instantiate array adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item,searchCriteriaList);
        //set dataset adapter for the spinner
        spinner.setAdapter(spinnerAdapter);
        //set the on item selected listener for the spinner
        spinner.setOnItemSelectedListener(onItemSelectedListener);

    }

    //when an item is selected from the spinner(search filter) do...
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.i("AllFragment", "Spinner pos" + position);
            setSearchFilter(searchCriteriaList[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
    }

    private OnQueryTextListener onQueryTextChange = new OnQueryTextListener() {
        //when the user submits a query using the button
        @Override
        public boolean onQueryTextSubmit(String s) {
            onQueryTextChange(s);
            return false;
        }
        //when the query text changes ....
        @Override
        public boolean onQueryTextChange(String s) {
            //if the adapter's object is not null filter the dataset in the adapter
            if (allPeopleAdapter!=null){
                allPeopleAdapter.getFilter().filter(s+"_"+getSearchFilter());
            }
            return false;
        }
    };

    private void setSearchFilter(String s){
        this.searchFilter = s;
    }

    private String getSearchFilter(){
        return this.searchFilter;
    }

    private void sendPostRequest(String givenUsername, String givenPassword) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String paramStart = params[0];
                String paramSize = params[1];

                return ServerComm.getPeople(paramStart, paramSize);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject jsonObject=null;
                boolean outcome = false;
                try {
                    jsonObject = new JSONObject(result);
                    outcome= jsonObject.getBoolean("successful");
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    if (jsonArray.length() ==0)
                        noMoreData = true;
                    for (int i =0; i<jsonArray.length();i++){
                        if ( jsonArray.get(i) instanceof JSONObject){
                            JSONObject ob = (JSONObject) jsonArray.get(i);
                            OtherParticipant opart = new OtherParticipant(ob.getString("id"),ob.getString("prefix"),ob.getString("first_name"),
                                    ob.getString("last_name"),ob.getString("university"),ob.getString("department"),ob.getString("email"),
                                    ob.getString("is_speaker").contains("1"),ob.getBoolean("favourite"),ob.getString("qualification"));
                            Bitmap bp = ImageProcessing.decodeImage(ob.getString("picture"));
                            opart.setProfilePicture(bp);
                            peopleList.add(opart);
                        }

                    }
                    //increment index for the next time of querying the server
                    startIndex += numOfPeople;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (outcome && !noMoreData){

                    mSwipeRefreshLayout.setRefreshing(false);
                    allPeopleAdapter.notifyDataSetChanged();
                    //initialise the adapter
/*                    allPeopleAdapter = new AllPeopleAdapter(peopleList,getActivity().getApplicationContext());
                    //set the adapter to the recycler view
                    recList.setAdapter(allPeopleAdapter);*/
                    //recList.setAdapter();
                    int lastVisibleItem = ((LinearLayoutManager) llm).findLastVisibleItemPosition();
                    recList.smoothScrollToPosition(lastVisibleItem+3);

                    //mSwipeRefreshLayout.isRefreshing()
                }
                else if (noMoreData){
                    mSwipeRefreshLayout.setRefreshing(false);
                    showToast("There are no more people to show...",Toast.LENGTH_SHORT);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showToast("Fetching data failed...",Toast.LENGTH_SHORT);
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(),getActivity())){
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(givenUsername, givenPassword);
        }else {
            mSwipeRefreshLayout.setRefreshing(false);
            showToast("Check your internet connection...",Toast.LENGTH_SHORT);
        }


    }
    //show toasts
    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(getActivity().getApplicationContext(), text,toast_length);
        m_currentToast.show();

    }

/*    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }*/

/*    private RecyclerView.OnScrollListener rcOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);


            int lastVisibleItem = ((LinearLayoutManager) llm).findLastVisibleItemPosition();
            int totalItemCount = llm.getItemCount();

            if (lastVisibleItem >= totalItemCount -1 ) {
                loadMore();
            }
            //showToast("Need to load more",Toast.LENGTH_SHORT);
        }

    };

    private void loadMore() {
        showToast("Need to load more",Toast.LENGTH_SHORT);
    }*/

    @Override
    public void onRefresh() {
        //if there are still data to fetch ...
        if (!noMoreData) {
            //start refreshing
            mSwipeRefreshLayout.setRefreshing(true);
            //send post request to server to get the next bunch of data
            sendPostRequest(Integer.toString(startIndex), Integer.toString(numOfPeople));


        }
        //otherwise
        else {
            //stop refreshing
            mSwipeRefreshLayout.setRefreshing(false);
            //show a message accordingly
            showToast("There are no more people to show...",Toast.LENGTH_SHORT);
        }

    }
}
