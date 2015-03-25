package ui.quillpeer.com.quillpeer.ui.people;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.List;
import core.People.OtherParticipant;
import core.People.Person;
import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the recycler list
 * Created on 23/11/2014.
 * @author Loucas Stylianou
 */
public class AllPeopleAdapter extends RecyclerView.Adapter<AllPeopleViewHolder>  {
    /** the people list */
    private List<Person> personList;
    private Context mContext;
    /** screen name */
    private String fragment;
    /** class name which is used for testing purposes */
    private static final String TAG = AllPeopleAdapter.class.getSimpleName();

    /**
     * This method defines the behaviour when the view holder is created
     * @param viewGroup
     * @param i
     * @return
     */
    public AllPeopleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_list_people_item, viewGroup, false);
        itemView.setTag(R.id.cardListPeople,personList);

        /*screen name can be SuggestionsFragment or AllFragment*/
        itemView.setTag(R.id.people_pager,fragment);

        return new AllPeopleViewHolder(itemView);
    }

    /**
     * The custom adapter constructor
     * @param personList
     * @param context
     * @param s
     */
    public AllPeopleAdapter(List<Person> personList,Context context,String s){
        this.personList = personList;
        this.mContext = context;
        this.fragment = s;
    }

    /**
     * Retrieve the number of items in the list
     * @return personList.size()
     */
    @Override
    public int getItemCount() {
        return personList.size();
    }

    /**
     * Bind data of people on the cards
     * @param peopleViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(AllPeopleViewHolder peopleViewHolder, int i) {
        OtherParticipant person = (OtherParticipant) personList.get(i);
        String speaker = "";
        if (person.isSpeaker()) speaker = "(Speaker)";
        peopleViewHolder.txtPeoplePersonalDetails.setText(person.getTitle() + " " + person.getName() + " " + person.getSurname() + " " + speaker);
        peopleViewHolder.txtPeopleUniversity.setText(person.getUniversity());
        peopleViewHolder.txtPeopleDepartment.setText(person.getDepartment());
        peopleViewHolder.txtPeopleQualification.setText(person.getQualification());
        //check if the person is favourite or not and set the corresponding picture
        if (person.isFavourite()) {
            peopleViewHolder.imgPeopleFavourite.setImageResource(R.drawable.ic_star_yellow);
            peopleViewHolder.imgPeopleFavourite.setTag(R.drawable.ic_star_yellow);
        } else {
            peopleViewHolder.imgPeopleFavourite.setImageResource(R.drawable.ic_star_white);
            peopleViewHolder.imgPeopleFavourite.setTag(R.drawable.ic_star_white);
        }
        //set the profile picture to the corresponding view
        if (person.getProfilePicture()!=null){
            peopleViewHolder.imgPeopleProfilePic.setImageBitmap(person.getProfilePicture());
        }else {
            peopleViewHolder.imgPeopleProfilePic.setImageResource(R.drawable.ic_action_person);
        }

        /*if the person is not correlated to the user then set the visibility of the corrIndicator image to invisible otherwise set to visible*/
        if (!person.isCorrelated()){
            peopleViewHolder.corrIndicator.setVisibility(View.INVISIBLE);

        }else {
            peopleViewHolder.corrIndicator.setVisibility(View.VISIBLE);
        }


    }
}
