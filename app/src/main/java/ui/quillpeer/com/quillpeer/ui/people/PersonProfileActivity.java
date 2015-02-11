package ui.quillpeer.com.quillpeer.ui.people;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    private TextView profileTitle;
    private TextView profileName;
    private TextView profileUniversity;
    private TextView profileDepartment;
    private TextView profileQualification;
    private TextView profilePaperAbstract;
    private Toast m_currentToast;
    private OtherParticipant person;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.fragment_profile_cardview);
        MyApplication.setCurrentActivity(this);
        initializeViewResources();
    }

    private void initializeViewResources() {
        profilePicture = (ImageView)findViewById(R.id.imgCardProfilePicture);
        profileTitle = (TextView)findViewById(R.id.txtCardProfileTitle);
        profileName = (TextView)findViewById(R.id.txtCardProfileName);
        profileUniversity = (TextView)findViewById(R.id.txtCardProfileUniversity);
        profileDepartment = (TextView)findViewById(R.id.txtCardProfileDepartment);
        profileQualification = (TextView)findViewById(R.id.txtCardProfileQualification);
        profilePaperAbstract = (TextView)findViewById(R.id.txtCardProfilePaperAbstract);
        Intent intent = getIntent();
        if (intent!=null) {
            Bundle bundle = intent.getExtras();
            String person_id = bundle.getString("person_id");
            //TODO: call the get person api
            //get person's data from server
            sendPostRequest(person_id);
        }else Log.e("PersonProfileActivity","NUll intent passed");


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
                                user.getString("department"),user.getString("email"),user.getString("is_speaker").contains("1"),false,user.getString("qualification"));
                        person.setPaperAbstract(jsonObject.getJSONObject("abstract").getString("abstract"));
                        person.setProfilePicture(ImageProcessing.decodeImage(user.getString("picture")));
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
        profileTitle.setText(person.getTitle());
        profileName.setText(person.getName() + " " + person.getSurname());
        profileUniversity.setText(person.getUniversity());
        profileDepartment.setText(person.getDepartment());
        profileQualification.setText(person.getQualification());
        profilePaperAbstract.setText(person.getPaperAbstract());
        if (person.getProfilePicture()!=null){
            profilePicture.setImageBitmap(person.getProfilePicture());
        }

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
}
