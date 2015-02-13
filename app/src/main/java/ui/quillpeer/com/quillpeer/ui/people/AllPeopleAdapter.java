package ui.quillpeer.com.quillpeer.ui.people;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import core.MyApplication;
import core.People.OtherParticipant;
import core.People.Person;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 23/11/2014.
 */
public class AllPeopleAdapter extends RecyclerView.Adapter<AllPeopleAdapter.AllPeopleViewHolder>  {

    static List<Person> personList;
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

    }


    public static class AllPeopleViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgPeopleProfilePic;
        protected TextView txtPeoplePersonalDetails;
        protected TextView txtPeopleUniversity;
        protected TextView txtPeopleDepartment;
        protected TextView txtPeopleQualification;
        protected ImageView imgPeopleFavourite;
        protected Toast m_currentToast;
        //initialise views
        public AllPeopleViewHolder(View v){
            super(v);
            imgPeopleFavourite = (ImageView)v.findViewById(R.id.imgPeopleFavourite);
            imgPeopleProfilePic = (ImageView)v.findViewById(R.id.imgPeopleProfilePic);
            imgPeopleFavourite.setTag(R.drawable.ic_star_white);
            txtPeoplePersonalDetails = (TextView)v.findViewById(R.id.txtPeoplePersonalDetails);
            txtPeopleDepartment = (TextView)v.findViewById(R.id.txtPeopleDepartment);
            txtPeopleUniversity = (TextView)v.findViewById(R.id.txtPeopleUniversity);
            txtPeopleQualification = (TextView)v.findViewById(R.id.txtPeopleQualification);

            //set listener to favourite image view
            imgPeopleFavourite.setOnTouchListener(imgStarListener);
            //set tap listener on the profile pic
            imgPeopleProfilePic.setOnClickListener(imgProfPicTapListener);

        }

        View.OnClickListener imgProfPicTapListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OtherParticipant op = (OtherParticipant)personList.get(getPosition());
                if (ServerComm.isNetworkConnected(v.getContext().getApplicationContext(),MyApplication.currentActivity())){
                    Intent intent = new Intent(MyApplication.currentActivity(), PersonProfileActivity.class);
                    intent.putExtra("person_id",op.getUserId());
                    v.getContext().startActivity(intent);

                }

            }
        };

        //on favourite image press do ...
        View.OnTouchListener imgStarListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //check the network state and proceed if there is internet connection
                if (ServerComm.isNetworkConnected(v.getContext().getApplicationContext(), MyApplication.currentActivity())){
                    OtherParticipant op = (OtherParticipant) personList.get(getPosition());
                    int tag = (Integer)imgPeopleFavourite.getTag();
                    if (tag == R.drawable.ic_star_white) {
                        sendPostRequest(op.getUserId(),v,"add",getPosition());
                    }
                    else {
                        sendPostRequest(op.getUserId(),v,"remove",getPosition());

                    }
                }

                return false;
            }
        };

        private void sendPostRequest(String userId, final View v,String favouriteAction, final int positionIndex) {

            class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
                ProgressDialog dialog = ProgressDialog.show(MyApplication.currentActivity(),
                        "Posting...", "Please wait...", false);

                @Override
                protected void onPreExecute(){
                    super.onPreExecute();
                    dialog.show();
                }

                @Override
                protected String doInBackground(String... params) {

                    String paramUserId = params[0];
                    String paramAction = params[1];
                    View paramSize = v;

                    if (paramAction.equals("add"))
                        return ServerComm.addFavourite(paramUserId);
                    else
                        return ServerComm.removeFavourite(paramUserId);

                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    dialog.dismiss();
                    JSONObject jsonObject=null;
                    boolean outcome = false;
                    try {
                        if (result!=null) {
                            jsonObject = new JSONObject(result);
                            outcome = jsonObject.getBoolean("successful");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //if the post to the server was successful then ...
                    if (outcome){
                        //change the image
                        int tag = (Integer)imgPeopleFavourite.getTag();
                        if (tag == R.drawable.ic_star_white) {
                            imgPeopleFavourite.setImageResource(R.drawable.ic_star_yellow);
                            imgPeopleFavourite.setTag(R.drawable.ic_star_yellow);
                        }
                        else {
                            imgPeopleFavourite.setImageResource(R.drawable.ic_star_white);
                            imgPeopleFavourite.setTag(R.drawable.ic_star_white);
                        }
                        //change the favourite status
                        ((OtherParticipant)personList.get(positionIndex)).changeFavouriteStatus();
                    }
                    else {
                        showToast("Posting data failed...",Toast.LENGTH_SHORT,v);
                    }
                }
            }
            //check the network state and proceed if there is internet connection
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(userId,favouriteAction);

        }

        //show toasts
        void showToast(String text,int toast_length,View v)
        {
            if(m_currentToast != null)
            {
                m_currentToast.cancel();
            }
            m_currentToast = Toast.makeText(v.getContext().getApplicationContext(), text,toast_length);
            m_currentToast.show();

        }
    }
}
