package ui.quillpeer.com.quillpeer.ui.people;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import core.Person;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 23/11/2014.
 */
public class AllPeopleAdapter extends RecyclerView.Adapter<AllPeopleAdapter.AllPeopleViewHolder> {

    private List<Person> personList;

    @Override
    public AllPeopleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_list_people_item, viewGroup, false);

        return new AllPeopleViewHolder(itemView);
    }

    public AllPeopleAdapter(List<Person> personList){
        this.personList = personList;
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }


    @Override
    public void onBindViewHolder(AllPeopleViewHolder peopleViewHolder, int i) {
        Person person = personList.get(i);
        peopleViewHolder.txtPeoplePersonalDetails.setText(person.getTitle() + " " + person.getName() + " " + person.getSurname());
        peopleViewHolder.txtPeopleUniversity.setText(person.getUniversity());
        peopleViewHolder.txtPeopleDepartment.setText(person.getDepartment());

    }


    public static class AllPeopleViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgPeopleProfilePic;
        protected TextView txtPeoplePersonalDetails;
        protected TextView txtPeopleUniversity;
        protected TextView txtPeopleDepartment;
        protected ImageView imgPeopleFavourite;

        public AllPeopleViewHolder(View v){
            super(v);
            imgPeopleFavourite = (ImageView)v.findViewById(R.id.imgPeopleFavourite);
            imgPeopleProfilePic = (ImageView)v.findViewById(R.id.imgPeopleProfilePic);
            txtPeoplePersonalDetails = (TextView)v.findViewById(R.id.txtPeoplePersonalDetails);
            txtPeopleDepartment = (TextView)v.findViewById(R.id.txtPeopleDepartment);
            txtPeopleUniversity = (TextView)v.findViewById(R.id.txtPeopleUniversity);

        }
    }
}
