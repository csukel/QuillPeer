package ui.quillpeer.com.quillpeer.ui.people;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import core.People.OtherParticipant;
import core.People.Person;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 23/11/2014.
 */
public class AllPeopleAdapter extends RecyclerView.Adapter<AllPeopleAdapter.AllPeopleViewHolder> implements Filterable {

    private List<Person> personList;
    private List<Person> orig;
    private Context mContext;

    @Override
    public AllPeopleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_list_people_item, viewGroup, false);

        return new AllPeopleViewHolder(itemView);
    }

    public AllPeopleAdapter(List<Person> personList,Context context){
        this.personList = personList;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    //bind data of people on the cards
    @Override
    public void onBindViewHolder(AllPeopleViewHolder peopleViewHolder, int i) {
        OtherParticipant person = (OtherParticipant) personList.get(i);
        peopleViewHolder.txtPeoplePersonalDetails.setText(person.getTitle() + " " + person.getName() + " " + person.getSurname());
        peopleViewHolder.txtPeopleUniversity.setText(person.getUniversity());
        peopleViewHolder.txtPeopleDepartment.setText(person.getDepartment());
        if (person.isFavourite()) {
            peopleViewHolder.imgPeopleFavourite.setImageResource(R.drawable.ic_star_yellow);
            peopleViewHolder.imgPeopleFavourite.setTag(R.drawable.ic_star_yellow);
        }




    }
    //define the custom filter

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence query) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Person> results = new ArrayList<Person>();
                if (orig == null)
                    orig = personList;
                if (query != null) {
                    if (orig != null && orig.size() > 0) {
                        //loop through the original list and check objects against the query
                        for (final Person p : orig) {

                            //split the query to two strings ...
                            String[] filterQueryConstraints = query.toString().split("_");
                            //the query
                            String filterQuery = filterQueryConstraints[0];
                            //and the search criteria filter
                            String searchFilter = filterQueryConstraints[1];
                            //if an object is matched according to search filter and the query string then add it to the results list
                            if (searchFilter.equals(mContext.getResources().getString(R.string.sfName)) &&
                                    p.getName().toLowerCase().contains(filterQuery.toLowerCase()))
                                results.add(p);
                            else if (searchFilter.equals(mContext.getResources().getString(R.string.sfSurname)) &&
                                    p.getSurname().toLowerCase().contains(filterQuery.toLowerCase()))
                                results.add(p);
                            else if (searchFilter.equals(mContext.getResources().getString(R.string.sfUniversity)) &&
                                    p.getUniversity().toLowerCase().contains(filterQuery.toLowerCase()))
                                results.add(p);
                            else if (searchFilter.equals(mContext.getResources().getString(R.string.sfDepartment)) &&
                                    p.getDepartment().toLowerCase().contains(filterQuery.toLowerCase()))
                                results.add(p);
                            else if (searchFilter.equals(mContext.getResources().getString(R.string.sfQualification)) &&
                                    p.getQualification().toLowerCase().contains(filterQuery.toLowerCase()))
                                results.add(p);

/*                            if (p.getName().toLowerCase()
                                    .contains(filterQuery.toString().toLowerCase())
                                    || p.getSurname().toLowerCase().contains(filterQuery.toString().toLowerCase()))
                                results.add(p);*/
                        }
                    }
                    //return results of filtering
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                personList= (ArrayList<Person>) results.values;
                //update the adapter
                notifyDataSetChanged();
            }
        };
    }


    public static class AllPeopleViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgPeopleProfilePic;
        protected TextView txtPeoplePersonalDetails;
        protected TextView txtPeopleUniversity;
        protected TextView txtPeopleDepartment;
        protected ImageView imgPeopleFavourite;

        //initialise views
        public AllPeopleViewHolder(View v){
            super(v);
            imgPeopleFavourite = (ImageView)v.findViewById(R.id.imgPeopleFavourite);
            imgPeopleProfilePic = (ImageView)v.findViewById(R.id.imgPeopleProfilePic);

            imgPeopleFavourite.setTag(R.drawable.ic_star_white);
            txtPeoplePersonalDetails = (TextView)v.findViewById(R.id.txtPeoplePersonalDetails);
            txtPeopleDepartment = (TextView)v.findViewById(R.id.txtPeopleDepartment);
            txtPeopleUniversity = (TextView)v.findViewById(R.id.txtPeopleUniversity);

            imgPeopleFavourite.setOnTouchListener(imgStarListener);

        }

        //on favourite image press do ...
        View.OnTouchListener imgStarListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int tag = (Integer)imgPeopleFavourite.getTag();
                if (tag == R.drawable.ic_star_white) {
                    imgPeopleFavourite.setImageResource(R.drawable.ic_star_yellow);
                    imgPeopleFavourite.setTag(R.drawable.ic_star_yellow);
                }
                else {
                    imgPeopleFavourite.setImageResource(R.drawable.ic_star_white);
                    imgPeopleFavourite.setTag(R.drawable.ic_star_white);
                }
                return false;
            }
        };
    }
}
