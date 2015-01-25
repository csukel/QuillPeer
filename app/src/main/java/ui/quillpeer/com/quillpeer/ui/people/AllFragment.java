package ui.quillpeer.com.quillpeer.ui.people;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import core.Person;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 */
public class AllFragment extends Fragment  {
    private List<Person> peopleList;
    private AllPeopleAdapter allPeopleAdapter=null;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_people_all, container, false);
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardListPeople);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        //allow the fragment to invalidate options menu
        getActivity().supportInvalidateOptionsMenu();
        //allow fragment to have a menu
        setHasOptionsMenu(true);
        //populate list with fake data
        populateList();
        //initialise the adapter
        allPeopleAdapter = new AllPeopleAdapter(peopleList);
        //set the adapter to the recycler view
        recList.setAdapter(allPeopleAdapter);
        return rootView;
    }

    public void populateList(){
        Person person1 = new Person("Mr","Loucas","Stylianou","University of Warwick","Department of Computer Science",false,false);
        Person person2 = new Person("Mr","Hao","Dong","University of Warwick","Department of Computer Science",false,false);
        Person person3 = new Person("Mr","Marios","Chrysanthou","University of Warwick","Department of Computer Science",false,false);
        Person person4 = new Person("Dr","Maria","Liakata","University of Warwick","Department of Computer Science",false,false);
        Person person5 = new Person("Dr","Matt","Leeke","University of Warwick","Department of Computer Science",false,false);
        Person person6 = new Person("Mr","Sirandjivy","Gocouladasse alias Souloramane","University of Warwick","Department of Computer Science",false,false);
        peopleList = new ArrayList<Person>();
        peopleList.add(person1);
        peopleList.add(person2);
        peopleList.add(person3);
        peopleList.add(person4);
        peopleList.add(person5);
        peopleList.add(person6);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    allPeopleAdapter.getFilter().filter(s);
                }
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
    }

}
