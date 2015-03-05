package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.ImageProcessing;
import core.MyApplication;
import core.People.Topic;
import core.People.User;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 * This is the activity corresponding to login screen
 */
public class LoginActivity extends Activity {
    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;
    private Toast m_currentToast;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //connect objects to xml resources
        initializeViewResources();

        settings=getSharedPreferences("prefs",0);
        MyApplication.setPrefs(settings);
        boolean credStored = settings.getBoolean("credStored",false);
        //if the credentials are stored then auto login
        if (credStored){
            sendPostRequest(settings.getString("username",""),settings.getString("password",""));
        }
    }

    private void initializeViewResources() {
        //initialize resources objects with the corresponding resources found in login_activity.xml file
        btnLogin = (Button)findViewById(R.id.btnLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword.setOnEditorActionListener(edtPassOnEditorActionListener);
        //assign the click listener for login button
        btnLogin.setOnClickListener(btnLoginClickListener);
    }

    //set on editor action listener to pass edit text in order to handle the enter action
    TextView.OnEditorActionListener edtPassOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                //on enter try to login
                performLoginAction();
            }
            return false;
        }
    };

    private void performLoginAction(){
        //get the username from the input box
        String username = edtUsername.getText().toString();
        //get the password from the input box
        String password = edtPassword.getText().toString();

        if (username.isEmpty()){
            edtUsername.setError("Please fill this field");
        }
        else if (password.isEmpty()){
            edtPassword.setError("Please fill this field");
        }
        else {
            //Authenticate user, display any error messages if there is something wrong with the authentication stage
            //otherwise....
            //if its the first time logging in the application create the account using account manager
            //else if its not then check the credentials against the stored values for this account
            //and if authentication is successful move to the next activity
            sendPostRequest(edtUsername.getText().toString(),edtPassword.getText().toString());
        }
    }

    View.OnClickListener btnLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            performLoginAction();
        }
    };


    /*trigger the authentication api comm between client and server*/
    private void sendPostRequest(String givenUsername, String givenPassword) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            ProgressDialog dialog = ProgressDialog.show(LoginActivity.this,
                    "Connecting...", "Please wait...", false);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String paramUsername = params[0];
                String paramPassword = params[1];

                return ServerComm.login(paramUsername,paramPassword);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                JSONObject jsonObject=null;
                String msg = "Something went wrong";
                boolean outcome = false;
                try {
                    if (result!=null) {
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");
                        msg = jsonObject.getString("msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (outcome){
                    //TODO: create user object, store user details in db,proceed to next activity
                    JSONObject jsonUser;
                    String imageStream=null;
                    try {
                        jsonUser = jsonObject.getJSONObject("user");
                        User.instantiate(jsonUser.getString("prefix"),jsonUser.getString("first_name"),jsonUser.getString("last_name"),jsonUser.getString("university"),
                                jsonUser.getString("department"),jsonUser.getString("email"),jsonUser.getString("is_speaker").contains("1"),jsonUser.getString("qualification"));
                        JSONObject jsonAbstract  = jsonObject.getJSONObject("abstract");
                        User.getInstance().setPaperAbstract(jsonAbstract.getString("abstract"));
                        User.getInstance().setPaperAbstractTitle(jsonAbstract.getString("title"));
                        imageStream = jsonUser.getString("picture");
                        JSONArray topicsArray = jsonObject.getJSONArray("topics");
                        for (int i = 0; i<topicsArray.length();i++){
                            if (topicsArray.get(i) instanceof JSONObject){
                                JSONObject topicObject = topicsArray.getJSONObject(i);
                                //name , weight
                                User.getInstance().addTopicToList(new Topic(topicObject.getString("name")
                                        ,topicObject.getDouble("weight")));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //if the user has already uploaded a picture step to main activity
                    if (!imageStream.equals("null")){
                        User.getInstance().setProfilePicture(ImageProcessing.decodeImage(imageStream));
                        //if the credentials havent been stored then...
                        checkIfCredStored();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        //close activity
                        finish();
                    }
                    //otherwise step to take a pic activity
                    else {
                        checkIfCredStored();
                        //showToast("Authentication successful",Toast.LENGTH_SHORT);
                        Intent intent = new Intent(getApplicationContext(), TakePicActivity.class);
                        intent.putExtra("activity","login");
                        startActivity(intent);
                        finish();
                    }

                }
                else{
                    showToast(msg,Toast.LENGTH_SHORT);
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getApplicationContext(),this)){
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(givenUsername, givenPassword);
        }else showToast("Check your internet connection...",Toast.LENGTH_SHORT);


    }
    //check if the credentials are stored
    private void checkIfCredStored() {
        //if no then store them in shared preferences
        if (!settings.getBoolean("credStored",false)) {
            SharedPreferences.Editor editor = settings.edit();
            //store credentials and the boolean var
            editor.putBoolean("credStored", true);
            editor.putString("username", edtUsername.getText().toString());
            editor.putString("password", edtPassword.getText().toString());
            editor.commit();
        }
    }

    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(this, text,toast_length);
        m_currentToast.show();

    }


}


