package ui.quillpeer.com.quillpeer.ui.people;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import core.Person;

/**
 * Created by loucas on 23/11/2014.
 */
public class AllPeopleAdapter extends RecyclerView.Adapter<AllPeopleViewHolder> {

    private List<Person> personList;

    public AllPeopleAdapter(List<Person> personList){
        this.personList = personList;
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}
