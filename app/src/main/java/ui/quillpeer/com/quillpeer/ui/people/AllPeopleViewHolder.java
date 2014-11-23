package ui.quillpeer.com.quillpeer.ui.people;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 23/11/2014.
 */
public class AllPeopleViewHolder extends RecyclerView.ViewHolder {
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
        txtPeopleDepartment = (TextView)v.findViewById(R.id.txtPeopleUniversity);

    }
}
