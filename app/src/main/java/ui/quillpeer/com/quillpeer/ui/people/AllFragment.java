package ui.quillpeer.com.quillpeer.ui.people;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import core.Person;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 */
public class AllFragment extends Fragment {
    private List<Person> peopleList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_all, container, false);
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardListPeople);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        populateList();
        AllPeopleAdapter allPeopleAdapter = new AllPeopleAdapter(peopleList);
        recList.setAdapter(allPeopleAdapter);
        return rootView;
    }

    public void populateList(){
        Person person1 = new Person("Mr","Loucas","Stylianou","University of Warwick","Department of Computer Science");
        Person person2 = new Person("Mr","Hao","Dong","University of Warwick","Department of Computer Science");
        Person person3 = new Person("Mr","Marios","Chrysanthou","University of Warwick","Department of Computer Science");
        Person person4 = new Person("Dr","Maria","Liakata","University of Warwick","Department of Computer Science");
        Person person5 = new Person("Dr","Matt","Leeke","University of Warwick","Department of Computer Science");
        Person person6 = new Person("Mr","Sirandjivy","Gocouladasse alias Souloramane","University of Warwick","Department of Computer Science");
        peopleList = new ArrayList<Person>();
        peopleList.add(person1);
        peopleList.add(person2);
        peopleList.add(person3);
        peopleList.add(person4);
        peopleList.add(person5);
        peopleList.add(person6);
    }
}
