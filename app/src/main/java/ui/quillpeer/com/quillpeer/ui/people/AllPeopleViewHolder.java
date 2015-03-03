package ui.quillpeer.com.quillpeer.ui.people;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
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
import de.hdodenhof.circleimageview.CircleImageView;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 15/02/2015.
 */
public class AllPeopleViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imgPeopleProfilePic;
    protected TextView txtPeoplePersonalDetails;
    protected TextView txtPeopleUniversity;
    protected TextView txtPeopleDepartment;
    protected TextView txtPeopleQualification;
    protected ImageView imgPeopleFavourite;
    protected Toast m_currentToast;
    protected CircleImageView corrIndicator;
    //private OtherParticipant op;
    private List<Person> personList;
    private String screenName;

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
        corrIndicator = (CircleImageView)v.findViewById(R.id.corr_indicator_img);
        //set listener to favourite image view
        imgPeopleFavourite.setOnTouchListener(imgStarListener);
        //set tap listener on the profile pic
        imgPeopleProfilePic.setOnClickListener(imgProfPicTapListener);

        personList = (ArrayList<Person>)v.getTag(R.id.cardListPeople);
        /*screen name can be SuggestionsFragment or AllFragment*/
        screenName = (String)v.getTag(R.id.people_pager);

    }

    View.OnClickListener imgProfPicTapListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            OtherParticipant op = (OtherParticipant)personList.get(getPosition());
            if (ServerComm.isNetworkConnected(v.getContext().getApplicationContext(), MyApplication.currentActivity())){
                Intent intent = new Intent(MyApplication.currentActivity(), PersonProfileActivity.class);
                intent.putExtra("person_id",op.getUserId());
                intent.putExtra("fragment",screenName);
                intent.putExtra("position",getPosition());
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
                    String user_id = ((OtherParticipant) personList.get(positionIndex)).getUserId();
                    if (screenName.equals("AllFragment")){
                        /*search for the corresponding person in the person list for suggestions fragment and change favourite option*/
                        for (Person p:SuggestionsFragment.getPeopleList()){
                            if (((OtherParticipant)p).getUserId().equals(user_id)){
                                ((OtherParticipant) p).changeFavouriteStatus();
                            }
                        }

                    }else {
                        /*search for the corresponding person in the person list for suggestions fragment and change favourite option*/
                        for (Person p:AllFragment.getPeopleList()){
                            if (((OtherParticipant)p).getUserId().equals(user_id)){
                                ((OtherParticipant) p).changeFavouriteStatus();
                            }
                        }
                    }
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

