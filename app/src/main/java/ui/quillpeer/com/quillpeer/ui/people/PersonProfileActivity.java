package ui.quillpeer.com.quillpeer.ui.people;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import core.ImageProcessing;
import core.MyApplication;
import core.People.OtherParticipant;
import core.People.User;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 11/02/2015.
 */
public class PersonProfileActivity extends Activity {
    private ImageView profilePicture;
    private TextView profileName;
    private TextView profileUniversity;
    private TextView profileDepartment;
    private TextView profileQualification;
    private TextView profilePaperAbstract;
    private TextView profilePaperAbstractTitle;
    private Toast m_currentToast;
    private OtherParticipant person;
    private ImageView profileFavourite;


    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.fragment_profile_cardview);
        MyApplication.setCurrentActivity(this);
        initializeViewResources();
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException nex){
            nex.printStackTrace();
        }
    }

    private void initializeViewResources() {
        profilePicture = (ImageView)findViewById(R.id.imgCardProfilePicture);
        profileName = (TextView)findViewById(R.id.txtCardProfileName);
        profileUniversity = (TextView)findViewById(R.id.txtCardProfileUniversity);
        profileDepartment = (TextView)findViewById(R.id.txtCardProfileDepartment);
        profileQualification = (TextView)findViewById(R.id.txtCardProfileQualification);
        profilePaperAbstract = (TextView)findViewById(R.id.txtCardProfilePaperAbstract);
        profilePaperAbstractTitle = (TextView)findViewById(R.id.txtCardProfilePaperAbstractTitle);
        profileFavourite = (ImageView)findViewById(R.id.imgCardProfileFavourite);
        Intent intent = getIntent();
        if (intent!=null) {
            Bundle bundle = intent.getExtras();
            String person_id = bundle.getString("person_id");
            //get person's data from server
            sendPostRequest(person_id);
        }else Log.e("PersonProfileActivity","NUll intent passed");


    }

    View.OnTouchListener profileFavouriteOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Send post request to the server
            //check the network state and proceed if there is internet connection
            if (ServerComm.isNetworkConnected(v.getContext().getApplicationContext(), MyApplication.currentActivity())){

                int tag = (Integer)profileFavourite.getTag();
                if (tag == R.drawable.ic_star_white) {
                    sendPostRequestFavourite(person.getUserId(),v,"add");
                }
                else {
                    sendPostRequestFavourite(person.getUserId(),v,"remove");

                }
            }
            return false;
        }
    };

    //send a post request to the server to add or remove the person from user favourite's list
    private void sendPostRequestFavourite(String userId, final View v,String favouriteAction) {

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
                    int tag = (Integer)profileFavourite.getTag();
                    if (tag == R.drawable.ic_star_white) {
                        profileFavourite.setImageResource(R.drawable.ic_star_yellow);
                        profileFavourite.setTag(R.drawable.ic_star_yellow);
                    }
                    else {
                        profileFavourite.setImageResource(R.drawable.ic_star_white);
                        profileFavourite.setTag(R.drawable.ic_star_white);
                    }
                    //change the favourite status
                    person.changeFavouriteStatus();
                }
                else {
                    showToast("Posting data failed...",Toast.LENGTH_SHORT);
                }
            }
        }
        //check the network state and proceed if there is internet connection
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(userId,favouriteAction);

    }


    //send a post request to server to get the person's data
    private void sendPostRequest(String userId) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            ProgressDialog dialog = ProgressDialog.show(MyApplication.currentActivity(),
                    "Fetching profile data...", "Please wait...", false);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String paramUserId = params[0];

                return ServerComm.getPerson(paramUserId);

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                JSONObject jsonObject=null;
                boolean outcome = false;
                try {
                    if (result!=null) {
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");
                    }else {
                        showToast("Network error",Toast.LENGTH_SHORT);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (outcome) {
                    try {
                        JSONObject user = jsonObject.getJSONObject("user");
                        person = new OtherParticipant(user.getString("id"),user.getString("prefix"),user.getString("first_name"),user.getString("last_name"),user.getString("university"),
                                user.getString("department"),user.getString("email"),user.getString("is_speaker").contains("1"),user.getBoolean("favourite"),user.getString("qualification"));
                        person.setPaperAbstract(jsonObject.getJSONObject("abstract").getString("abstract"));
                        person.setProfilePicture(ImageProcessing.decodeImage(user.getString("picture")));
                        person.setPaperAbstractTitle(jsonObject.getJSONObject("abstract").getString("title"));
                        setViewValues();
                        new Handler(){
                        }.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },500);
                        //dialog.dismiss();
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
                else {
                    dialog.dismiss();
                    showToast("Fetching profile data failed...", Toast.LENGTH_SHORT);
                }
            }
        }
        if (ServerComm.isNetworkConnected(getApplicationContext(),this)){
        //check the network state and proceed if there is internet connection
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(userId);
        }else {
            showToast("Network error",Toast.LENGTH_SHORT);
            finish();
        }

    }

    private void setViewValues() {
        String speaker = "";
        if (person.isSpeaker()) speaker = "(Speaker)";
        profileName.setText(person.getTitle() + " " + person.getName() + " " + person.getSurname() + " " + speaker);
        profileUniversity.setText(person.getUniversity());
        profileDepartment.setText(person.getDepartment());
        profileQualification.setText(person.getQualification());
        profilePaperAbstract.setText(person.getPaperAbstract());
        if (person.getProfilePicture()!=null){
            profilePicture.setImageBitmap(person.getProfilePicture());
        }
        profilePaperAbstractTitle.setText(person.getPaperAbstractTitle());

        if (person.isFavourite()) {
            profileFavourite.setImageResource(R.drawable.ic_star_yellow);
            profileFavourite.setTag(R.drawable.ic_star_yellow);
        } else {
            profileFavourite.setImageResource(R.drawable.ic_star_white);
            profileFavourite.setTag(R.drawable.ic_star_white);
        }
        profileFavourite.setOnTouchListener(profileFavouriteOnTouchListener);

    }

    //show toasts
    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(this.getApplicationContext(), text,toast_length);
        m_currentToast.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
